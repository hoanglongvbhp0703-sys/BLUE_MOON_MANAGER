package vn.bluemoon.service;

import vn.bluemoon.exception.DbException;
import vn.bluemoon.model.entity.FeeCollection;
import vn.bluemoon.repository.FeeCollectionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for Fee Collection management
 */
public class FeeCollectionService {
    private final FeeCollectionRepository feeRepository = new FeeCollectionRepository();
    
    /**
     * Get all fee collections
     */
    public List<FeeCollection> getAllFeeCollections() throws DbException {
        return feeRepository.findAll();
    }
    
    /**
     * Search fee collections
     */
    public List<FeeCollection> searchFeeCollections(String apartmentCode, String householdCode, 
                                                    String ownerName, Integer month, Integer year, 
                                                    String status) throws DbException {
        return feeRepository.search(apartmentCode, householdCode, ownerName, month, year, status);
    }
    
    /**
     * Get fee collections by household
     */
    public List<FeeCollection> getFeeCollectionsByHousehold(Integer householdId) throws DbException {
        return feeRepository.findByHouseholdId(householdId);
    }
    
    /**
     * Get fee collections by month and year
     */
    public List<FeeCollection> getFeeCollectionsByMonthYear(Integer month, Integer year) throws DbException {
        return feeRepository.findByMonthYear(month, year);
    }
    
    /**
     * Create fee collection for a household
     * Luôn tạo mới fee collection (không UPDATE fee cũ)
     * Cho phép có nhiều fee collection cho cùng tháng/năm nếu cần
     */
    public FeeCollection createFeeCollection(Integer householdId, Integer month, Integer year, 
                                             BigDecimal amount) throws DbException {
        // Luôn tạo mới fee collection, không kiểm tra duplicate
        // Điều này cho phép admin thêm nhiều khoản thu phí cho cùng tháng/năm nếu cần
        FeeCollection fee = new FeeCollection();
        fee.setHouseholdId(householdId);
        fee.setMonth(month);
        fee.setYear(year);
        fee.setAmount(amount != null ? amount : BigDecimal.ZERO);
        fee.setStatus("unpaid");
        fee.setPaidAmount(BigDecimal.ZERO);
        fee.setFeeType("periodic");
        return feeRepository.create(fee);
    }
    
    /**
     * Mark fee collection as paid
     */
    public void markAsPaid(Integer feeId, LocalDate paymentDate, String paymentMethod) throws DbException {
        feeRepository.markAsPaid(feeId, paymentDate, paymentMethod);
    }
    
    /**
     * Update fee collection
     */
    public void updateFeeCollection(FeeCollection fee) throws DbException {
        feeRepository.update(fee);
    }
}


