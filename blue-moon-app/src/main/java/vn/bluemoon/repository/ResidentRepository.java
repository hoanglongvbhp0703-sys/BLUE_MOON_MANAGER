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
            "WHERE 1=1"
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
    
    private Resident mapResultSetToResident(ResultSet rs) throws SQLException {
        Resident resident = new Resident();
        resident.setId(rs.getInt("id"));
        resident.setHouseholdId(rs.getInt("household_id"));
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

