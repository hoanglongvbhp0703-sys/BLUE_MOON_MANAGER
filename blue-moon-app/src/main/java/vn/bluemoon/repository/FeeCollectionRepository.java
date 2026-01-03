package vn.bluemoon.repository;

import vn.bluemoon.exception.DbException;
import vn.bluemoon.model.entity.FeeCollection;
import vn.bluemoon.util.JdbcUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for FeeCollection entity
 */
public class FeeCollectionRepository {
    
    /**
     * Find all fee collections with household and apartment info
     * CHỈ LẤY CÁC HỘ CÓ CHỦ HỘ (relationship = 'Chủ hộ')
     * Đảm bảo mỗi household chỉ có 1 chủ hộ được hiển thị
     * Lấy owner_name từ residents table để đảm bảo đồng bộ
     */
    public List<FeeCollection> findAll() throws DbException {
        List<FeeCollection> fees = new ArrayList<>();
        String sql = "SELECT DISTINCT ON (fc.id) fc.*, " +
                     "a.apartment_code, " +
                     "h.household_code, " +
                     "COALESCE(r.full_name, h.owner_name) as owner_name " +
                     "FROM fee_collections fc " +
                     "JOIN households h ON fc.household_id = h.id " +
                     "JOIN apartments a ON h.apartment_id = a.id " +
                     "LEFT JOIN residents r ON r.household_id = h.id AND r.relationship = 'Chủ hộ' " +
                     "WHERE EXISTS (SELECT 1 FROM residents r2 WHERE r2.household_id = h.id AND r2.relationship = 'Chủ hộ') " +
                     "ORDER BY fc.id, fc.year DESC, fc.month DESC, fc.created_at DESC";
        
        try (Connection conn = JdbcUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                fees.add(mapResultSetToFeeCollection(rs));
            }
        } catch (SQLException e) {
            throw new DbException("Error finding all fee collections: " + e.getMessage(), e);
        }
        return fees;
    }
    
    /**
     * Find fee collections by user ID (through resident)
     * CHỈ LẤY CÁC FEE_COLLECTIONS CỦA CHỦ HỘ
     * Lấy owner_name từ residents table để đảm bảo đồng bộ
     * Tìm trực tiếp qua household_id của resident có user_id
     */
    public List<FeeCollection> findByUserId(Integer userId) throws DbException {
        List<FeeCollection> fees = new ArrayList<>();
        // Query đơn giản hơn: Tìm fee collections của household mà user là chủ hộ
        String sql = "SELECT DISTINCT ON (fc.id) fc.*, " +
                     "a.apartment_code, " +
                     "h.household_code, " +
                     "COALESCE(r.full_name, h.owner_name) as owner_name " +
                     "FROM fee_collections fc " +
                     "JOIN households h ON fc.household_id = h.id " +
                     "JOIN apartments a ON h.apartment_id = a.id " +
                     "LEFT JOIN residents r ON r.household_id = h.id AND r.relationship = 'Chủ hộ' " +
                     "WHERE fc.household_id IN (SELECT household_id FROM residents WHERE user_id = ? AND relationship = 'Chủ hộ') " +
                     "ORDER BY fc.id, fc.year DESC, fc.month DESC";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fees.add(mapResultSetToFeeCollection(rs));
            }
            
            // Debug log
            System.out.println("Query fee collections for user ID: " + userId);
            System.out.println("Found " + fees.size() + " fee collections");
            if (fees.size() > 0) {
                System.out.println("First fee: household_id=" + fees.get(0).getHouseholdId() + 
                                 ", month=" + fees.get(0).getMonth() + 
                                 ", year=" + fees.get(0).getYear() + 
                                 ", status=" + fees.get(0).getStatus());
            } else {
                System.out.println("No fee collections found. Checking if user has resident record...");
                // Kiểm tra xem user có resident record không
                String checkResidentSql = "SELECT household_id FROM residents WHERE user_id = ? AND relationship = 'Chủ hộ'";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkResidentSql)) {
                    checkStmt.setInt(1, userId);
                    ResultSet checkRs = checkStmt.executeQuery();
                    if (checkRs.next()) {
                        int householdId = checkRs.getInt("household_id");
                        System.out.println("User has resident record with household_id: " + householdId);
                        // Kiểm tra xem có fee collections nào cho household này không
                        String checkFeeSql = "SELECT COUNT(*) FROM fee_collections WHERE household_id = ?";
                        try (PreparedStatement feeStmt = conn.prepareStatement(checkFeeSql)) {
                            feeStmt.setInt(1, householdId);
                            ResultSet feeRs = feeStmt.executeQuery();
                            if (feeRs.next()) {
                                int feeCount = feeRs.getInt(1);
                                System.out.println("Found " + feeCount + " fee collections for household " + householdId);
                            }
                        }
                    } else {
                        System.out.println("User does NOT have resident record with relationship = 'Chủ hộ'");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in findByUserId query: " + e.getMessage());
            e.printStackTrace();
            throw new DbException("Error finding fee collections by user: " + e.getMessage(), e);
        }
        return fees;
    }
    
    /**
     * Find fee collection by ID
     * Lấy owner_name từ residents table để đảm bảo đồng bộ
     */
    public FeeCollection findById(Integer id) throws DbException {
        String sql = "SELECT fc.*, " +
                     "a.apartment_code, " +
                     "h.household_code, " +
                     "COALESCE(r.full_name, h.owner_name) as owner_name " +
                     "FROM fee_collections fc " +
                     "JOIN households h ON fc.household_id = h.id " +
                     "JOIN apartments a ON h.apartment_id = a.id " +
                     "LEFT JOIN residents r ON r.household_id = h.id AND r.relationship = 'Chủ hộ' " +
                     "WHERE fc.id = ?";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToFeeCollection(rs);
            }
        } catch (SQLException e) {
            throw new DbException("Error finding fee collection by ID: " + e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Find fee collections by household ID
     * CHỈ LẤY CÁC HỘ CÓ CHỦ HỘ (relationship = 'Chủ hộ')
     * Lấy owner_name từ residents table để đảm bảo đồng bộ
     */
    public List<FeeCollection> findByHouseholdId(Integer householdId) throws DbException {
        List<FeeCollection> fees = new ArrayList<>();
        String sql = "SELECT DISTINCT ON (fc.id) fc.*, " +
                     "a.apartment_code, " +
                     "h.household_code, " +
                     "COALESCE(r.full_name, h.owner_name) as owner_name " +
                     "FROM fee_collections fc " +
                     "JOIN households h ON fc.household_id = h.id " +
                     "JOIN apartments a ON h.apartment_id = a.id " +
                     "LEFT JOIN residents r ON r.household_id = h.id AND r.relationship = 'Chủ hộ' " +
                     "WHERE fc.household_id = ? " +
                     "AND EXISTS (SELECT 1 FROM residents r2 WHERE r2.household_id = h.id AND r2.relationship = 'Chủ hộ') " +
                     "ORDER BY fc.id, fc.year DESC, fc.month DESC";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, householdId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fees.add(mapResultSetToFeeCollection(rs));
            }
        } catch (SQLException e) {
            throw new DbException("Error finding fee collections by household: " + e.getMessage(), e);
        }
        return fees;
    }
    
    /**
     * Find fee collection by month and year
     * CHỈ LẤY CÁC HỘ CÓ CHỦ HỘ (relationship = 'Chủ hộ')
     * Lấy owner_name từ residents table để đảm bảo đồng bộ
     */
    public List<FeeCollection> findByMonthYear(Integer month, Integer year) throws DbException {
        List<FeeCollection> fees = new ArrayList<>();
        String sql = "SELECT DISTINCT ON (fc.id) fc.*, " +
                     "a.apartment_code, " +
                     "h.household_code, " +
                     "COALESCE(r.full_name, h.owner_name) as owner_name " +
                     "FROM fee_collections fc " +
                     "JOIN households h ON fc.household_id = h.id " +
                     "JOIN apartments a ON h.apartment_id = a.id " +
                     "LEFT JOIN residents r ON r.household_id = h.id AND r.relationship = 'Chủ hộ' " +
                     "WHERE fc.month = ? AND fc.year = ? " +
                     "AND EXISTS (SELECT 1 FROM residents r2 WHERE r2.household_id = h.id AND r2.relationship = 'Chủ hộ') " +
                     "ORDER BY fc.id, fc.created_at DESC";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fees.add(mapResultSetToFeeCollection(rs));
            }
        } catch (SQLException e) {
            throw new DbException("Error finding fee collections by month/year: " + e.getMessage(), e);
        }
        return fees;
    }
    
    /**
     * Search fee collections
     * CHỈ LẤY CÁC HỘ CÓ CHỦ HỘ (relationship = 'Chủ hộ')
     * Đảm bảo mỗi household chỉ có 1 chủ hộ được hiển thị
     * Lấy owner_name từ residents table để đảm bảo đồng bộ
     */
    public List<FeeCollection> search(String apartmentCode, String householdCode, String ownerName, 
                                      Integer month, Integer year, String status) throws DbException {
        List<FeeCollection> fees = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT DISTINCT ON (fc.id) fc.*, " +
            "a.apartment_code, " +
            "h.household_code, " +
            "COALESCE(r.full_name, h.owner_name) as owner_name " +
            "FROM fee_collections fc " +
            "JOIN households h ON fc.household_id = h.id " +
            "JOIN apartments a ON h.apartment_id = a.id " +
            "INNER JOIN residents r ON r.household_id = h.id AND r.relationship = 'Chủ hộ' " +
            "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();
        
        // Thêm điều kiện tìm kiếm theo tên chủ hộ - filter trực tiếp trên JOIN
        if (ownerName != null && !ownerName.trim().isEmpty()) {
            sql.append(" AND r.full_name ILIKE ?");
            params.add("%" + ownerName.trim() + "%");
        }
        
        // Các điều kiện tìm kiếm khác
        if (apartmentCode != null && !apartmentCode.trim().isEmpty()) {
            sql.append(" AND a.apartment_code ILIKE ?");
            params.add("%" + apartmentCode.trim() + "%");
        }
        if (householdCode != null && !householdCode.trim().isEmpty()) {
            sql.append(" AND h.household_code ILIKE ?");
            params.add("%" + householdCode.trim() + "%");
        }
        if (month != null) {
            sql.append(" AND fc.month = ?");
            params.add(month);
        }
        if (year != null) {
            sql.append(" AND fc.year = ?");
            params.add(year);
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND fc.status = ?");
            params.add(status);
        }
        
        sql.append(" ORDER BY fc.id, fc.year DESC, fc.month DESC, fc.created_at DESC");
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fees.add(mapResultSetToFeeCollection(rs));
            }
        } catch (SQLException e) {
            throw new DbException("Error searching fee collections: " + e.getMessage(), e);
        }
        return fees;
    }
    
    /**
     * Create fee collection
     */
    public FeeCollection create(FeeCollection fee) throws DbException {
        // Kiểm tra xem database có các cột mới chưa
        boolean hasFeeTypeColumn = checkColumnExists("fee_collections", "fee_type");
        
        String sql;
        if (hasFeeTypeColumn) {
            sql = "INSERT INTO fee_collections (household_id, month, year, amount, paid_amount, status, " +
                  "fee_type, reason, payment_date, payment_method, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                  "RETURNING id";
        } else {
            // Fallback cho database cũ chưa có fee_type và reason
            sql = "INSERT INTO fee_collections (household_id, month, year, amount, paid_amount, status, " +
                  "payment_date, payment_method, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                  "RETURNING id";
        }
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fee.getHouseholdId());
            
            if (fee.getMonth() != null) {
                stmt.setInt(2, fee.getMonth());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            
            if (fee.getYear() != null) {
                stmt.setInt(3, fee.getYear());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            
            stmt.setBigDecimal(4, fee.getAmount());
            stmt.setBigDecimal(5, fee.getPaidAmount() != null ? fee.getPaidAmount() : BigDecimal.ZERO);
            stmt.setString(6, fee.getStatus() != null ? fee.getStatus() : "unpaid");
            
            int paramIndex = 7;
            if (hasFeeTypeColumn) {
                stmt.setString(paramIndex++, fee.getFeeType() != null ? fee.getFeeType() : "periodic");
                stmt.setString(paramIndex++, fee.getReason());
            }
            
            if (fee.getPaymentDate() != null) {
                stmt.setDate(paramIndex++, Date.valueOf(fee.getPaymentDate()));
            } else {
                stmt.setNull(paramIndex++, Types.DATE);
            }
            
            stmt.setString(paramIndex++, fee.getPaymentMethod());
            stmt.setString(paramIndex++, fee.getNotes());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                fee.setId(rs.getInt(1));
            }
            return fee;
        } catch (SQLException e) {
            throw new DbException("Error creating fee collection: " + e.getMessage(), e);
        }
    }
    
    /**
     * Kiểm tra xem cột có tồn tại trong bảng không
     */
    private boolean checkColumnExists(String tableName, String columnName) {
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT COUNT(*) FROM information_schema.columns " +
                 "WHERE table_name = ? AND column_name = ?")) {
            stmt.setString(1, tableName);
            stmt.setString(2, columnName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            // Nếu không kiểm tra được, giả sử cột không tồn tại
            return false;
        }
        return false;
    }
    
    /**
     * Update fee collection
     */
    public void update(FeeCollection fee) throws DbException {
        String sql = "UPDATE fee_collections SET amount = ?, paid_amount = ?, status = ?, fee_type = ?, reason = ?, " +
                     "payment_date = ?, payment_method = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, fee.getAmount());
            stmt.setBigDecimal(2, fee.getPaidAmount() != null ? fee.getPaidAmount() : BigDecimal.ZERO);
            stmt.setString(3, fee.getStatus());
            stmt.setString(4, fee.getFeeType() != null ? fee.getFeeType() : "periodic");
            stmt.setString(5, fee.getReason());
            
            if (fee.getPaymentDate() != null) {
                stmt.setDate(6, Date.valueOf(fee.getPaymentDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }
            
            stmt.setString(7, fee.getPaymentMethod());
            stmt.setString(8, fee.getNotes());
            stmt.setInt(9, fee.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Error updating fee collection: " + e.getMessage(), e);
        }
    }
    
    /**
     * Mark as paid
     */
    public void markAsPaid(Integer id, LocalDate paymentDate, String paymentMethod) throws DbException {
        String sql = "UPDATE fee_collections SET status = 'paid', payment_date = ?, " +
                     "payment_method = ? WHERE id = ?";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(paymentDate));
            stmt.setString(2, paymentMethod);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Error marking fee as paid: " + e.getMessage(), e);
        }
    }
    
    /**
     * Delete all fee collections for a household
     */
    public void deleteByHouseholdId(Integer householdId) throws DbException {
        String sql = "DELETE FROM fee_collections WHERE household_id = ?";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, householdId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Error deleting fee collections by household: " + e.getMessage(), e);
        }
    }
    
    private FeeCollection mapResultSetToFeeCollection(ResultSet rs) throws SQLException {
        FeeCollection fee = new FeeCollection();
        fee.setId(rs.getInt("id"));
        fee.setHouseholdId(rs.getInt("household_id"));
        
        // Month và year có thể NULL cho thu phí không định kỳ
        int month = rs.getInt("month");
        if (!rs.wasNull()) {
            fee.setMonth(month);
        }
        
        int year = rs.getInt("year");
        if (!rs.wasNull()) {
            fee.setYear(year);
        }
        
        BigDecimal amount = rs.getBigDecimal("amount");
        fee.setAmount(amount != null ? amount : BigDecimal.ZERO);
        
        BigDecimal paidAmount = rs.getBigDecimal("paid_amount");
        fee.setPaidAmount(paidAmount != null ? paidAmount : BigDecimal.ZERO);
        
        fee.setStatus(rs.getString("status"));
        
        // Fee type và reason
        try {
            fee.setFeeType(rs.getString("fee_type"));
        } catch (SQLException e) {
            // Column might not exist in older databases
            fee.setFeeType("periodic");
        }
        
        try {
            fee.setReason(rs.getString("reason"));
        } catch (SQLException e) {
            // Column might not exist in older databases
            fee.setReason(null);
        }
        
        Date paymentDate = rs.getDate("payment_date");
        if (paymentDate != null) {
            fee.setPaymentDate(paymentDate.toLocalDate());
        }
        
        fee.setPaymentMethod(rs.getString("payment_method"));
        fee.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            fee.setCreatedAt(createdAt.toLocalDateTime().toLocalDate());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            fee.setUpdatedAt(updatedAt.toLocalDateTime().toLocalDate());
        }
        
        // Join fields
        fee.setApartmentCode(rs.getString("apartment_code"));
        fee.setHouseholdCode(rs.getString("household_code"));
        fee.setOwnerName(rs.getString("owner_name"));
        
        return fee;
    }
}


