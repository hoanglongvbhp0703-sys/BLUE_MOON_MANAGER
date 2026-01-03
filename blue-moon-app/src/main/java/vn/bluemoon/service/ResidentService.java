package vn.bluemoon.service;

import vn.bluemoon.exception.DbException;
import vn.bluemoon.model.entity.Resident;
import vn.bluemoon.repository.ResidentRepository;
import vn.bluemoon.repository.FeeCollectionRepository;

import java.util.List;

/**
 * Service for Resident management
 */
public class ResidentService {
    private final ResidentRepository residentRepository = new ResidentRepository();
    private final FeeCollectionRepository feeCollectionRepository = new FeeCollectionRepository();
    
    /**
     * Get all residents
     */
    public List<Resident> getAllResidents() throws DbException {
        return residentRepository.findAll();
    }
    
    /**
     * Search residents
     */
    public List<Resident> searchResidents(String name, String apartmentCode, String householdCode) throws DbException {
        return residentRepository.search(name, apartmentCode, householdCode);
    }
    
    /**
     * Get resident by ID
     */
    public Resident getResidentById(Integer id) throws DbException {
        return residentRepository.findById(id);
    }
    
    /**
     * Delete resident and related data
     * - If resident is "Chủ hộ", delete all fee collections for the household
     * - Delete the resident
     * - If household has no other residents, delete the household
     */
    public void deleteResident(Integer residentId) throws DbException {
        // Get resident info before deleting
        Resident resident = residentRepository.findById(residentId);
        if (resident == null) {
            throw new DbException("Không tìm thấy nhân khẩu");
        }
        
        Integer householdId = resident.getHouseholdId();
        boolean isChuHo = "Chủ hộ".equals(resident.getRelationship());
        
        // If resident is "Chủ hộ", delete all fee collections for the household
        if (isChuHo) {
            feeCollectionRepository.deleteByHouseholdId(householdId);
        }
        
        // Delete the resident
        residentRepository.delete(residentId);
        
        // Check if household has any other residents
        boolean hasOtherResidents = residentRepository.hasOtherResidents(householdId, residentId);
        
        // If no other residents, delete the household
        if (!hasOtherResidents) {
            deleteHousehold(householdId);
        }
    }
    
    /**
     * Delete household by ID
     */
    private void deleteHousehold(Integer householdId) throws DbException {
        String sql = "DELETE FROM households WHERE id = ?";
        try (java.sql.Connection conn = vn.bluemoon.util.JdbcUtils.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, householdId);
            stmt.executeUpdate();
        } catch (java.sql.SQLException e) {
            throw new DbException("Error deleting household: " + e.getMessage(), e);
        }
    }
}


