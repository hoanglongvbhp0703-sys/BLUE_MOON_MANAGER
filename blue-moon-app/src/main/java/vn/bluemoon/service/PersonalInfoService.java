package vn.bluemoon.service;

import vn.bluemoon.exception.DbException;
import vn.bluemoon.model.dto.PersonalInfoRequest;
import vn.bluemoon.model.entity.Resident;
import vn.bluemoon.model.entity.User;
import vn.bluemoon.repository.ResidentRepository;
import vn.bluemoon.repository.UserRepository;
import vn.bluemoon.util.JdbcUtils;
import vn.bluemoon.validation.ValidationException;
import vn.bluemoon.validation.Validators;

import java.sql.*;
import java.time.LocalDate;

/**
 * Service for managing personal information (đăng ký/cập nhật thông tin cá nhân)
 */
public class PersonalInfoService {
    private final ResidentRepository residentRepository = new ResidentRepository();
    private final UserRepository userRepository = new UserRepository();
    
    /**
     * Register or update personal information for a user
     */
    public void registerOrUpdatePersonalInfo(Integer userId, PersonalInfoRequest request) 
            throws DbException, ValidationException {
        
        // Validate input
        Validators.validateRequired(request.getFullName(), "Họ và tên");
        Validators.validateRequired(request.getIdCard(), "CMND/CCCD");
        
        // Get user
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new ValidationException("Người dùng không tồn tại");
        }
        
        // Check if user already has resident record
        Resident existingResident = residentRepository.findByUserId(userId);
        
        // Find or create household
        Integer householdId = findOrCreateHousehold(request, user);
        
        // Create or update resident
        if (existingResident == null) {
            // Create new resident
            Resident resident = new Resident();
            resident.setHouseholdId(householdId);
            resident.setUserId(userId);
            resident.setFullName(request.getFullName());
            resident.setIdCard(request.getIdCard());
            resident.setDateOfBirth(request.getDateOfBirth());
            resident.setGender(request.getGender());
            resident.setRelationship(request.getRelationship() != null ? request.getRelationship() : "Chủ hộ");
            resident.setPhone(request.getPhone() != null ? request.getPhone() : user.getPhone());
            resident.setEmail(request.getEmail() != null ? request.getEmail() : user.getEmail());
            resident.setOccupation(request.getOccupation());
            resident.setPermanentAddress(request.getPermanentAddress());
            resident.setTemporaryAddress(request.getTemporaryAddress());
            resident.setStatus("active");
            
            residentRepository.create(resident);
        } else {
            // Update existing resident
            existingResident.setHouseholdId(householdId);
            existingResident.setFullName(request.getFullName());
            existingResident.setIdCard(request.getIdCard());
            existingResident.setDateOfBirth(request.getDateOfBirth());
            existingResident.setGender(request.getGender());
            existingResident.setRelationship(request.getRelationship() != null ? request.getRelationship() : existingResident.getRelationship());
            existingResident.setPhone(request.getPhone() != null ? request.getPhone() : existingResident.getPhone());
            existingResident.setEmail(request.getEmail() != null ? request.getEmail() : existingResident.getEmail());
            existingResident.setOccupation(request.getOccupation());
            existingResident.setPermanentAddress(request.getPermanentAddress());
            existingResident.setTemporaryAddress(request.getTemporaryAddress());
            
            residentRepository.update(existingResident);
        }
        
        // Update user info if changed
        boolean userUpdated = false;
        if (request.getFullName() != null && !request.getFullName().equals(user.getFullName())) {
            user.setFullName(request.getFullName());
            userUpdated = true;
        }
        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            user.setPhone(request.getPhone());
            userUpdated = true;
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            user.setEmail(request.getEmail());
            userUpdated = true;
        }
        if (userUpdated) {
            userRepository.update(user);
        }
    }
    
    /**
     * Get personal information for a user
     */
    public Resident getPersonalInfo(Integer userId) throws DbException {
        return residentRepository.findByUserId(userId);
    }
    
    /**
     * Find or create household based on apartment_code or household_code
     */
    private Integer findOrCreateHousehold(PersonalInfoRequest request, User user) throws DbException {
        Integer householdId = null;
        
        // Try to find household by household_code
        if (request.getHouseholdCode() != null && !request.getHouseholdCode().trim().isEmpty()) {
            householdId = findHouseholdByCode(request.getHouseholdCode());
        }
        
        // Try to find household by apartment_code
        if (householdId == null && request.getApartmentCode() != null && !request.getApartmentCode().trim().isEmpty()) {
            householdId = findHouseholdByApartmentCode(request.getApartmentCode());
        }
        
        // If not found, create a default household
        if (householdId == null) {
            householdId = createDefaultHousehold(request, user);
        }
        
        return householdId;
    }
    
    /**
     * Find household by household_code
     */
    private Integer findHouseholdByCode(String householdCode) throws DbException {
        String sql = "SELECT id FROM households WHERE household_code = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, householdCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new DbException("Error finding household by code: " + e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Find household by apartment_code
     */
    private Integer findHouseholdByApartmentCode(String apartmentCode) throws DbException {
        String sql = "SELECT h.id FROM households h " +
                     "JOIN apartments a ON h.apartment_id = a.id " +
                     "WHERE a.apartment_code = ? AND h.status = 'active' " +
                     "LIMIT 1";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, apartmentCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new DbException("Error finding household by apartment code: " + e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Create a default household for user
     */
    private Integer createDefaultHousehold(PersonalInfoRequest request, User user) throws DbException {
        // Create a default apartment if needed
        Integer apartmentId = createDefaultApartment(request);
        
        // Create household
        String householdCode = "USER-" + user.getId() + "-" + System.currentTimeMillis();
        String sql = "INSERT INTO households (apartment_id, household_code, owner_name, owner_phone, owner_email, " +
                     "registration_date, status) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, apartmentId);
            stmt.setString(2, householdCode);
            stmt.setString(3, request.getFullName() != null ? request.getFullName() : user.getFullName());
            stmt.setString(4, request.getPhone() != null ? request.getPhone() : user.getPhone());
            stmt.setString(5, request.getEmail() != null ? request.getEmail() : user.getEmail());
            stmt.setDate(6, Date.valueOf(LocalDate.now()));
            stmt.setString(7, "active");
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new DbException("Failed to create household");
        } catch (SQLException e) {
            throw new DbException("Error creating household: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a default apartment if needed
     */
    private Integer createDefaultApartment(PersonalInfoRequest request) throws DbException {
        // Try to find existing apartment first
        if (request.getApartmentCode() != null && !request.getApartmentCode().trim().isEmpty()) {
            String sql = "SELECT id FROM apartments WHERE apartment_code = ?";
            try (Connection conn = JdbcUtils.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, request.getApartmentCode());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            } catch (SQLException e) {
                throw new DbException("Error finding apartment: " + e.getMessage(), e);
            }
        }
        
        // Create default apartment
        String apartmentCode = "DEFAULT-" + System.currentTimeMillis();
        String sql = "INSERT INTO apartments (building_number, floor_number, room_number, apartment_code, area, number_of_rooms, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "A");
            stmt.setInt(2, 1);
            stmt.setString(3, "01");
            stmt.setString(4, apartmentCode);
            stmt.setBigDecimal(5, new java.math.BigDecimal("60.00"));
            stmt.setInt(6, 2);
            stmt.setString(7, "occupied");
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new DbException("Failed to create apartment");
        } catch (SQLException e) {
            throw new DbException("Error creating apartment: " + e.getMessage(), e);
        }
    }
}

