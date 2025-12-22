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
     */
    public List<FeeCollection> findAll() throws DbException {
        List<FeeCollection> fees = new ArrayList<>();
        String sql = "SELECT fc.*, " +
                     "a.apartment_code, " +
                     "h.household_code, " +
                     "h.owner_name " +
                     "FROM fee_collections fc " +
                     "JOIN households h ON fc.household_id = h.id " +
                     "JOIN apartments a ON h.apartment_id = a.id " +
                     "ORDER BY fc.year DESC, fc.month DESC, fc.created_at DESC";
        
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
     */
    public List<FeeCollection> findByUserId(Integer userId) throws DbException {
        List<FeeCollection> fees = new ArrayList<>();
        String sql = "SELECT fc.*, " +
                     "a.apartment_code, " +
                     "h.household_code, " +
                     "h.owner_name " +
                     "FROM fee_collections fc " +
                     "JOIN households h ON fc.household_id = h.id " +
                     "JOIN apartments a ON h.apartment_id = a.id " +
                     "JOIN residents r ON r.household_id = h.id " +
                     "WHERE r.user_id = ? " +
                     "ORDER BY fc.year DESC, fc.month DESC";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fees.add(mapResultSetToFeeCollection(rs));
            }
        } catch (SQLException e) {
            throw new DbException("Error finding fee collections by user: " + e.getMessage(), e);
        }
        return fees;
    }
    
    /**
     * Find fee collection by ID
     */
    public FeeCollection findById(Integer id) throws DbException {
        String sql = "SELECT fc.*, " +
                     "a.apartment_code, " +
                     "h.household_code, " +
                     "h.owner_name " +
                     "FROM fee_collections fc " +
                     "JOIN households h ON fc.household_id = h.id " +
                     "JOIN apartments a ON h.apartment_id = a.id " +
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
     */
    public List<FeeCollection> findByHouseholdId(Integer householdId) throws DbException {
        List<FeeCollection> fees = new ArrayList<>();
        String sql = "SELECT fc.*, " +
                     "a.apartment_code, " +
                     "h.household_code, " +
                     "h.owner_name " +
                     "FROM fee_collections fc " +
                     "JOIN households h ON fc.household_id = h.id " +
                     "JOIN apartments a ON h.apartment_id = a.id " +
                     "WHERE fc.household_id = ? " +
                     "ORDER BY fc.year DESC, fc.month DESC";
        
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
     */
    public List<FeeCollection> findByMonthYear(Integer month, Integer year) throws DbException {
        List<FeeCollection> fees = new ArrayList<>();
        String sql = "SELECT fc.*, " +
                     "a.apartment_code, " +
                     "h.household_code, " +
                     "h.owner_name " +
                     "FROM fee_collections fc " +
                     "JOIN households h ON fc.household_id = h.id " +
                     "JOIN apartments a ON h.apartment_id = a.id " +
                     "WHERE fc.month = ? AND fc.year = ? " +
                     "ORDER BY fc.created_at DESC";
        
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
     */
    public List<FeeCollection> search(String apartmentCode, String householdCode, String ownerName, 
                                      Integer month, Integer year, String status) throws DbException {
        List<FeeCollection> fees = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT fc.*, " +
            "a.apartment_code, " +
            "h.household_code, " +
            "h.owner_name " +
            "FROM fee_collections fc " +
            "JOIN households h ON fc.household_id = h.id " +
            "JOIN apartments a ON h.apartment_id = a.id " +
            "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();
        int paramIndex = 1;
        
        if (apartmentCode != null && !apartmentCode.trim().isEmpty()) {
            sql.append(" AND a.apartment_code ILIKE ?");
            params.add("%" + apartmentCode + "%");
        }
        if (householdCode != null && !householdCode.trim().isEmpty()) {
            sql.append(" AND h.household_code ILIKE ?");
            params.add("%" + householdCode + "%");
        }
        if (ownerName != null && !ownerName.trim().isEmpty()) {
            sql.append(" AND h.owner_name ILIKE ?");
            params.add("%" + ownerName + "%");
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
        
        sql.append(" ORDER BY fc.year DESC, fc.month DESC, fc.created_at DESC");
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (Object param : params) {
                stmt.setObject(paramIndex++, param);
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
        String sql = "INSERT INTO fee_collections (household_id, month, year, amount, paid_amount, status, " +
                     "payment_date, payment_method, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "RETURNING id";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fee.getHouseholdId());
            stmt.setInt(2, fee.getMonth());
            stmt.setInt(3, fee.getYear());
            stmt.setBigDecimal(4, fee.getAmount());
            stmt.setBigDecimal(5, fee.getPaidAmount() != null ? fee.getPaidAmount() : BigDecimal.ZERO);
            stmt.setString(6, fee.getStatus());
            
            if (fee.getPaymentDate() != null) {
                stmt.setDate(7, Date.valueOf(fee.getPaymentDate()));
            } else {
                stmt.setNull(7, Types.DATE);
            }
            
            stmt.setString(8, fee.getPaymentMethod());
            stmt.setString(9, fee.getNotes());
            
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
     * Update fee collection
     */
    public void update(FeeCollection fee) throws DbException {
        String sql = "UPDATE fee_collections SET amount = ?, paid_amount = ?, status = ?, payment_date = ?, " +
                     "payment_method = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, fee.getAmount());
            stmt.setBigDecimal(2, fee.getPaidAmount() != null ? fee.getPaidAmount() : BigDecimal.ZERO);
            stmt.setString(3, fee.getStatus());
            
            if (fee.getPaymentDate() != null) {
                stmt.setDate(4, Date.valueOf(fee.getPaymentDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            
            stmt.setString(5, fee.getPaymentMethod());
            stmt.setString(6, fee.getNotes());
            stmt.setInt(7, fee.getId());
            
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
    
    private FeeCollection mapResultSetToFeeCollection(ResultSet rs) throws SQLException {
        FeeCollection fee = new FeeCollection();
        fee.setId(rs.getInt("id"));
        fee.setHouseholdId(rs.getInt("household_id"));
        fee.setMonth(rs.getInt("month"));
        fee.setYear(rs.getInt("year"));
        
        BigDecimal amount = rs.getBigDecimal("amount");
        fee.setAmount(amount != null ? amount : BigDecimal.ZERO);
        
        BigDecimal paidAmount = rs.getBigDecimal("paid_amount");
        fee.setPaidAmount(paidAmount != null ? paidAmount : BigDecimal.ZERO);
        
        fee.setStatus(rs.getString("status"));
        
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


