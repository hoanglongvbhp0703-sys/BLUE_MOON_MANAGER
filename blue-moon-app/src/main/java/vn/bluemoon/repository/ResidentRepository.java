package vn.bluemoon.repository;

import vn.bluemoon.exception.DbException;
import vn.bluemoon.model.entity.Resident;
import vn.bluemoon.util.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Resident entity
 */
public class ResidentRepository {
    
    /**
     * Find all residents with household and apartment info
     * CHỈ LẤY CHỦ HỘ (relationship = 'Chủ hộ')
     */
    public List<Resident> findAll() throws DbException {
        List<Resident> residents = new ArrayList<>();
        String sql = "SELECT r.*, " +
                     "a.apartment_code, " +
                     "h.household_code, " +
                     "h.owner_name " +
                     "FROM residents r " +
                     "JOIN households h ON r.household_id = h.id " +
                     "JOIN apartments a ON h.apartment_id = a.id " +
                     "WHERE r.relationship = 'Chủ hộ' " +
                     "ORDER BY r.created_at DESC";
        
        try (Connection conn = JdbcUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                residents.add(mapResultSetToResident(rs));
            }
        } catch (SQLException e) {
            throw new DbException("Error finding all residents: " + e.getMessage(), e);
        }
        return residents;
    }
    
    /**
     * Search residents by name, apartment code, or household code
     * CHỈ LẤY CHỦ HỘ (relationship = 'Chủ hộ')
     */
    public List<Resident> search(String name, String apartmentCode, String householdCode) throws DbException {
        List<Resident> residents = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT r.*, " +
            "a.apartment_code, " +
            "h.household_code, " +
            "h.owner_name " +
            "FROM residents r " +
            "JOIN households h ON r.household_id = h.id " +
            "JOIN apartments a ON h.apartment_id = a.id " +
            "WHERE r.relationship = 'Chủ hộ'"
        );
        List<Object> params = new ArrayList<>();
        
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND r.full_name ILIKE ?");
            params.add("%" + name + "%");
        }
        if (apartmentCode != null && !apartmentCode.trim().isEmpty()) {
            sql.append(" AND a.apartment_code ILIKE ?");
            params.add("%" + apartmentCode + "%");
        }
        if (householdCode != null && !householdCode.trim().isEmpty()) {
            sql.append(" AND h.household_code ILIKE ?");
            params.add("%" + householdCode + "%");
        }
        
        sql.append(" ORDER BY r.created_at DESC");
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                residents.add(mapResultSetToResident(rs));
            }
        } catch (SQLException e) {
            throw new DbException("Error searching residents: " + e.getMessage(), e);
        }
        return residents;
    }
    
    /**
     * Find resident by ID
     */
    public Resident findById(Integer id) throws DbException {
        String sql = "SELECT r.*, " +
                     "a.apartment_code, " +
                     "h.household_code, " +
                     "h.owner_name " +
                     "FROM residents r " +
                     "JOIN households h ON r.household_id = h.id " +
                     "JOIN apartments a ON h.apartment_id = a.id " +
                     "WHERE r.id = ?";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToResident(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException("Error finding resident by id: " + e.getMessage(), e);
        }
    }
    
    /**
     * Find resident by user_id
     */
    public Resident findByUserId(Integer userId) throws DbException {
        String sql = "SELECT r.*, " +
                     "a.apartment_code, " +
                     "h.household_code, " +
                     "h.owner_name " +
                     "FROM residents r " +
                     "JOIN households h ON r.household_id = h.id " +
                     "JOIN apartments a ON h.apartment_id = a.id " +
                     "WHERE r.user_id = ?";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToResident(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException("Error finding resident by user_id: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create new resident
     */
    public Integer create(Resident resident) throws DbException {
        String sql = "INSERT INTO residents (household_id, user_id, full_name, id_card, date_of_birth, " +
                     "gender, relationship, phone, email, occupation, permanent_address, temporary_address, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, resident.getHouseholdId());
            if (resident.getUserId() != null) {
                stmt.setInt(2, resident.getUserId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, resident.getFullName());
            stmt.setString(4, resident.getIdCard());
            
            if (resident.getDateOfBirth() != null) {
                stmt.setDate(5, Date.valueOf(resident.getDateOfBirth()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            
            stmt.setString(6, resident.getGender());
            stmt.setString(7, resident.getRelationship());
            stmt.setString(8, resident.getPhone());
            stmt.setString(9, resident.getEmail());
            stmt.setString(10, resident.getOccupation());
            stmt.setString(11, resident.getPermanentAddress());
            stmt.setString(12, resident.getTemporaryAddress());
            stmt.setString(13, resident.getStatus() != null ? resident.getStatus() : "active");
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new DbException("Failed to create resident");
        } catch (SQLException e) {
            throw new DbException("Error creating resident: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update resident
     */
    public void update(Resident resident) throws DbException {
        String sql = "UPDATE residents SET " +
                     "household_id = ?, full_name = ?, id_card = ?, date_of_birth = ?, " +
                     "gender = ?, relationship = ?, phone = ?, email = ?, occupation = ?, " +
                     "permanent_address = ?, temporary_address = ?, status = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, resident.getHouseholdId());
            stmt.setString(2, resident.getFullName());
            stmt.setString(3, resident.getIdCard());
            
            if (resident.getDateOfBirth() != null) {
                stmt.setDate(4, Date.valueOf(resident.getDateOfBirth()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            
            stmt.setString(5, resident.getGender());
            stmt.setString(6, resident.getRelationship());
            stmt.setString(7, resident.getPhone());
            stmt.setString(8, resident.getEmail());
            stmt.setString(9, resident.getOccupation());
            stmt.setString(10, resident.getPermanentAddress());
            stmt.setString(11, resident.getTemporaryAddress());
            stmt.setString(12, resident.getStatus() != null ? resident.getStatus() : "active");
            stmt.setInt(13, resident.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Error updating resident: " + e.getMessage(), e);
        }
    }
    
    private Resident mapResultSetToResident(ResultSet rs) throws SQLException {
        Resident resident = new Resident();
        resident.setId(rs.getInt("id"));
        resident.setHouseholdId(rs.getInt("household_id"));
        
        int userId = rs.getInt("user_id");
        if (!rs.wasNull()) {
            resident.setUserId(userId);
        }
        
        resident.setFullName(rs.getString("full_name"));
        resident.setIdCard(rs.getString("id_card"));
        
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) {
            resident.setDateOfBirth(dob.toLocalDate());
        }
        
        resident.setGender(rs.getString("gender"));
        resident.setRelationship(rs.getString("relationship"));
        resident.setPhone(rs.getString("phone"));
        resident.setEmail(rs.getString("email"));
        resident.setOccupation(rs.getString("occupation"));
        resident.setPermanentAddress(rs.getString("permanent_address"));
        resident.setTemporaryAddress(rs.getString("temporary_address"));
        resident.setStatus(rs.getString("status"));
        resident.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            resident.setCreatedAt(createdAt.toLocalDateTime().toLocalDate());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            resident.setUpdatedAt(updatedAt.toLocalDateTime().toLocalDate());
        }
        
        // Join fields
        resident.setApartmentCode(rs.getString("apartment_code"));
        resident.setHouseholdCode(rs.getString("household_code"));
        resident.setOwnerName(rs.getString("owner_name"));
        
        return resident;
    }
}

