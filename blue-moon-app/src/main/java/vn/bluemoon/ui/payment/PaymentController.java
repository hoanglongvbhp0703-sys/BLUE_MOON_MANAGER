package vn.bluemoon.ui.payment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import vn.bluemoon.exception.DbException;
import vn.bluemoon.model.entity.FeeCollection;
import vn.bluemoon.model.entity.User;
import vn.bluemoon.security.SessionManager;
import vn.bluemoon.service.PaymentService;
import vn.bluemoon.util.ErrorDialog;

import java.math.BigDecimal;

/**
 * Controller for payment view
 */
public class PaymentController {
    @FXML
    private Label totalRemainingLabel;
    
    @FXML
    private TableView<FeeCollection> feeTable;
    
    @FXML
    private TableColumn<FeeCollection, String> monthYearColumn;
    
    @FXML
    private TableColumn<FeeCollection, String> amountColumn;
    
    @FXML
    private TableColumn<FeeCollection, String> paidAmountColumn;
    
    @FXML
    private TableColumn<FeeCollection, String> remainingColumn;
    
    @FXML
    private TableColumn<FeeCollection, String> statusColumn;
    
    @FXML
    private ComboBox<FeeCollection> feeComboBox;
    
    @FXML
    private TextField amountField;
    
    @FXML
    private ComboBox<String> paymentMethodComboBox;
    
    @FXML
    private Button payButton;
    
    @FXML
    private Label selectedFeeInfoLabel;
    
    private PaymentService paymentService = new PaymentService();
    private ObservableList<FeeCollection> feeList = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        setupTableColumns();
        setupPaymentMethodComboBox();
        loadUnpaidFees();
        setupFeeComboBox();
    }
    
    private void setupTableColumns() {
        monthYearColumn.setCellValueFactory(param -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> param.getValue().getMonthYearDisplay()
            )
        );
        
        amountColumn.setCellValueFactory(param -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> formatCurrency(param.getValue().getAmount())
            )
        );
        
        paidAmountColumn.setCellValueFactory(param -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> formatCurrency(param.getValue().getPaidAmount())
            )
        );
        
        remainingColumn.setCellValueFactory(param -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> {
                    BigDecimal remaining = param.getValue().getRemainingAmount();
                    String formatted = formatCurrency(remaining.abs());
                    if (remaining.compareTo(BigDecimal.ZERO) < 0) {
                        return "-" + formatted + " (dư)";
                    }
                    return formatted;
                }
            )
        );
        
        statusColumn.setCellValueFactory(param -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> param.getValue().getStatusDisplay()
            )
        );
    }
    
    private void setupPaymentMethodComboBox() {
        paymentMethodComboBox.setItems(FXCollections.observableArrayList(
            "Tiền mặt", "Chuyển khoản", "Thẻ tín dụng"
        ));
        paymentMethodComboBox.getSelectionModel().selectFirst();
    }
    
    private void setupFeeComboBox() {
        feeComboBox.setItems(feeList);
        feeComboBox.setCellFactory(param -> new ListCell<FeeCollection>() {
            @Override
            protected void updateItem(FeeCollection item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    BigDecimal remaining = item.getRemainingAmount();
                    String remainingText = remaining.compareTo(BigDecimal.ZERO) < 0 
                        ? formatCurrency(remaining.abs()) + " (dư)" 
                        : formatCurrency(remaining);
                    setText(String.format("%s - Còn lại: %s", 
                        item.getMonthYearDisplay(), remainingText));
                }
            }
        });
        
        feeComboBox.setButtonCell(new ListCell<FeeCollection>() {
            @Override
            protected void updateItem(FeeCollection item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    BigDecimal remaining = item.getRemainingAmount();
                    String remainingText = remaining.compareTo(BigDecimal.ZERO) < 0 
                        ? formatCurrency(remaining.abs()) + " (dư)" 
                        : formatCurrency(remaining);
                    setText(String.format("%s - Còn lại: %s", 
                        item.getMonthYearDisplay(), remainingText));
                }
            }
        });
        
        feeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateSelectedFeeInfo(newVal);
            }
        });
    }
    
    private void updateSelectedFeeInfo(FeeCollection fee) {
        BigDecimal remaining = fee.getRemainingAmount();
        String info = String.format("Tháng: %s | Tổng: %s | Đã nộp: %s | Còn lại: %s",
            fee.getMonthYearDisplay(),
            formatCurrency(fee.getAmount()),
            formatCurrency(fee.getPaidAmount()),
            remaining.compareTo(BigDecimal.ZERO) < 0 
                ? formatCurrency(remaining.abs()) + " (dư)" 
                : formatCurrency(remaining)
        );
        selectedFeeInfoLabel.setText(info);
        amountField.setText(remaining.compareTo(BigDecimal.ZERO) > 0 
            ? remaining.toString() 
            : "0");
    }
    
    private void loadUnpaidFees() {
        try {
            User currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser == null) {
                ErrorDialog.showError("Lỗi", "Vui lòng đăng nhập lại");
                return;
            }
            
            feeList.clear();
            feeList.addAll(paymentService.getUnpaidFeesForUser(currentUser.getId()));
            feeTable.setItems(feeList);
            
            // Cập nhật tổng số tiền còn lại
            BigDecimal totalRemaining = paymentService.getTotalRemainingAmount(currentUser.getId());
            if (totalRemaining.compareTo(BigDecimal.ZERO) < 0) {
                totalRemainingLabel.setText(String.format("Tổng số dư: %s", 
                    formatCurrency(totalRemaining.abs())));
                totalRemainingLabel.setStyle("-fx-text-fill: green;");
            } else {
                totalRemainingLabel.setText(String.format("Tổng số tiền cần đóng: %s", 
                    formatCurrency(totalRemaining)));
                totalRemainingLabel.setStyle("-fx-text-fill: #e74c3c;");
            }
        } catch (DbException e) {
            ErrorDialog.showDbError(e.getMessage());
        }
    }
    
    @FXML
    private void handlePay() {
        FeeCollection selectedFee = feeComboBox.getSelectionModel().getSelectedItem();
        if (selectedFee == null) {
            ErrorDialog.showError("Lỗi", "Vui lòng chọn khoản phí cần thanh toán");
            return;
        }
        
        String amountText = amountField.getText().trim();
        if (amountText.isEmpty()) {
            ErrorDialog.showError("Lỗi", "Vui lòng nhập số tiền");
            return;
        }
        
        BigDecimal paymentAmount;
        try {
            paymentAmount = new BigDecimal(amountText);
            if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
                ErrorDialog.showError("Lỗi", "Số tiền phải lớn hơn 0");
                return;
            }
        } catch (NumberFormatException e) {
            ErrorDialog.showError("Lỗi", "Số tiền không hợp lệ");
            return;
        }
        
        String paymentMethod = paymentMethodComboBox.getSelectionModel().getSelectedItem();
        if (paymentMethod == null) {
            ErrorDialog.showError("Lỗi", "Vui lòng chọn phương thức thanh toán");
            return;
        }
        
        try {
            FeeCollection updatedFee = paymentService.processPayment(
                selectedFee.getId(), 
                paymentAmount, 
                paymentMethod
            );
            
            // Hiển thị thông báo
            BigDecimal remaining = updatedFee.getRemainingAmount();
            String message;
            if (remaining.compareTo(BigDecimal.ZERO) == 0) {
                message = "Thanh toán thành công! Đã thanh toán đủ số tiền.";
            } else if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                message = String.format("Thanh toán thành công! Còn thiếu: %s", 
                    formatCurrency(remaining));
            } else {
                message = String.format("Thanh toán thành công! Bạn đã nộp dư: %s", 
                    formatCurrency(remaining.abs()));
            }
            
            ErrorDialog.showInfo("Thông báo", message);
            
            // Reload data
            loadUnpaidFees();
            feeComboBox.getSelectionModel().select(updatedFee);
            updateSelectedFeeInfo(updatedFee);
            
        } catch (DbException e) {
            ErrorDialog.showDbError(e.getMessage());
        }
    }
    
    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "0 đ";
        return String.format("%,d đ", amount.longValue());
    }
}

