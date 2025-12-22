package vn.bluemoon.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity for Fee Collection (Thu phí)
 */
public class FeeCollection {
    private Integer id;
    private Integer householdId;
    private Integer month;
    private Integer year;
    private BigDecimal amount;
    private BigDecimal paidAmount; // Số tiền đã nộp
    private String status; // unpaid, paid, partial_paid, overpaid
    private LocalDate paymentDate;
    private String paymentMethod;
    private String notes;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    
    // Thông tin từ household và apartment (join)
    private String apartmentCode;
    private String householdCode;
    private String ownerName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(Integer householdId) {
        this.householdId = householdId;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount != null ? paidAmount : BigDecimal.ZERO;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getApartmentCode() {
        return apartmentCode;
    }

    public void setApartmentCode(String apartmentCode) {
        this.apartmentCode = apartmentCode;
    }

    public String getHouseholdCode() {
        return householdCode;
    }

    public void setHouseholdCode(String householdCode) {
        this.householdCode = householdCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    
    public String getStatusDisplay() {
        if (status == null) return "";
        switch (status.toLowerCase()) {
            case "paid": return "Đã thu phí";
            case "unpaid": return "Chưa thu phí";
            case "partial_paid": return "Đã thanh toán 1 phần";
            case "overpaid": return "Nộp dư";
            default: return status;
        }
    }
    
    /**
     * Tính số tiền còn lại cần đóng (có thể âm nếu nộp dư)
     */
    public BigDecimal getRemainingAmount() {
        BigDecimal totalAmount = amount != null ? amount : BigDecimal.ZERO;
        BigDecimal paid = paidAmount != null ? paidAmount : BigDecimal.ZERO;
        return totalAmount.subtract(paid);
    }
    
    public String getMonthYearDisplay() {
        if (month == null || year == null) return "";
        return String.format("%02d/%d", month, year);
    }
}


