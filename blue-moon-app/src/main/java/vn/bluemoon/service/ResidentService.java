package vn.bluemoon.service;

import vn.bluemoon.exception.DbException;
import vn.bluemoon.model.entity.Resident;
import vn.bluemoon.repository.ResidentRepository;
import vn.bluemoon.repository.FeeCollectionRepository;
import vn.bluemoon.validation.ValidationException;
import vn.bluemoon.validation.Validators;

import java.time.LocalDate;
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
    
    /**
     * Đăng ký tạm trú cho cư dân
     */
    public void registerTemporaryResident(Integer residentId, LocalDate fromDate, LocalDate toDate, String reason) 
            throws DbException, ValidationException {
        if (fromDate == null) {
            throw new ValidationException("Ngày bắt đầu tạm trú không được để trống");
        }
        if (toDate == null) {
            throw new ValidationException("Ngày kết thúc tạm trú không được để trống");
        }
        
        if (toDate.isBefore(fromDate)) {
            throw new ValidationException("Ngày kết thúc phải sau ngày bắt đầu");
        }
        
        Resident resident = residentRepository.findById(residentId);
        if (resident == null) {
            throw new ValidationException("Không tìm thấy cư dân");
        }
        
        resident.setStatus("temporary_resident");
        resident.setTemporaryResidentFrom(fromDate);
        resident.setTemporaryResidentTo(toDate);
        resident.setTemporaryReason(reason);
        // Clear tạm vắng nếu có
        resident.setTemporaryAbsentFrom(null);
        resident.setTemporaryAbsentTo(null);
        
        residentRepository.update(resident);
    }
    
    /**
     * Đăng ký tạm vắng cho cư dân
     */
    public void registerTemporaryAbsent(Integer residentId, LocalDate fromDate, LocalDate toDate, String reason) 
            throws DbException, ValidationException {
        if (fromDate == null) {
            throw new ValidationException("Ngày bắt đầu tạm vắng không được để trống");
        }
        if (toDate == null) {
            throw new ValidationException("Ngày kết thúc tạm vắng không được để trống");
        }
        
        if (toDate.isBefore(fromDate)) {
            throw new ValidationException("Ngày kết thúc phải sau ngày bắt đầu");
        }
        
        Resident resident = residentRepository.findById(residentId);
        if (resident == null) {
            throw new ValidationException("Không tìm thấy cư dân");
        }
        
        resident.setStatus("temporary_absent");
        resident.setTemporaryAbsentFrom(fromDate);
        resident.setTemporaryAbsentTo(toDate);
        resident.setTemporaryReason(reason);
        // Clear tạm trú nếu có
        resident.setTemporaryResidentFrom(null);
        resident.setTemporaryResidentTo(null);
        
        residentRepository.update(resident);
    }
    
    /**
     * Hủy tạm trú/tạm vắng, trở về trạng thái active
     */
    public void cancelTemporaryStatus(Integer residentId) throws DbException, ValidationException {
        Resident resident = residentRepository.findById(residentId);
        if (resident == null) {
            throw new ValidationException("Không tìm thấy cư dân");
        }
        
        resident.setStatus("active");
        resident.setTemporaryResidentFrom(null);
        resident.setTemporaryResidentTo(null);
        resident.setTemporaryAbsentFrom(null);
        resident.setTemporaryAbsentTo(null);
        resident.setTemporaryReason(null);
        
        residentRepository.update(resident);
    }
}


