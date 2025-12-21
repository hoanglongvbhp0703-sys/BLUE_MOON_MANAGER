package vn.bluemoon.ui.fee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import vn.bluemoon.exception.DbException;
import vn.bluemoon.model.entity.FeeCollection;
import vn.bluemoon.service.FeeCollectionService;
import vn.bluemoon.util.ErrorDialog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for fee collection management view
 */
public class FeeCollectionController {
    @FXML
    private TextField searchApartmentCodeField;
    
    @FXML
    private TextField searchHouseholdCodeField;
    
    @FXML
    private TextField searchOwnerNameField;
    
    @FXML
    private ComboBox<Integer> searchMonthCombo;
    
    @FXML
    private ComboBox<Integer> searchYearCombo;
    
    @FXML
    private ComboBox<String> searchStatusCombo;
    
    @FXML
    private Button searchButton;
    
    @FXML
    private Button refreshButton;
    
    @FXML
    private Button markPaidButton;
    
    @FXML
    private TableView<FeeCollection> feeTable;
    
    @FXML
    private TableColumn<FeeCollection, Integer> sttColumn;
    
    @FXML
    private TableColumn<FeeCollection, String> apartmentCodeColumn;
    
    @FXML
    private TableColumn<FeeCollection, String> householdCodeColumn;
    
    @FXML
    private TableColumn<FeeCollection, String> ownerNameColumn;
    
    @FXML
    private TableColumn<FeeCollection, String> monthYearColumn;
    
    @FXML
    private TableColumn<FeeCollection, String> amountColumn;
    
    @FXML
    private TableColumn<FeeCollection, String> statusColumn;
    
    @FXML
    private TableColumn<FeeCollection, String> paymentDateColumn;
    
    @FXML
    private Label totalLabel;
    
    @FXML
    private Label paidLabel;
    
    @FXML
    private Label unpaidLabel;
    
    private FeeCollectionService feeService = new FeeCollectionService();
    private ObservableList<FeeCollection> feeList = FXCollections.observableArrayList();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    @FXML
    public void initialize() {
        try {
            setupComboBoxes();
            setupTableColumns();
            loadAllFeeCollections();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDialog.showError("Lỗi khởi tạo", "Lỗi khi khởi tạo màn hình quản lý thu phí: " + e.getMessage());
        }
    }
    
    private void setupComboBoxes() {
        // Tháng
        searchMonthCombo.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        
        // Năm (từ 2020 đến năm hiện tại + 1)
        int currentYear = LocalDate.now().getYear();
        for (int year = 2020; year <= currentYear + 1; year++) {
            searchYearCombo.getItems().add(year);
        }
        searchYearCombo.setValue(currentYear);
        
        // Trạng thái
        searchStatusCombo.getItems().addAll("Tất cả", "Chưa thu phí", "Đã thu phí");
        searchStatusCombo.setValue("Tất cả");
    }
    
    private void setupTableColumns() {
        sttColumn.setCellValueFactory(param -> {
            int index = feeTable.getItems().indexOf(param.getValue());
            return javafx.beans.binding.Bindings.createObjectBinding(() -> index + 1);
        });
        
        apartmentCodeColumn.setCellValueFactory(new PropertyValueFactory<>("apartmentCode"));
        householdCodeColumn.setCellValueFactory(new PropertyValueFactory<>("householdCode"));
        ownerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        
        monthYearColumn.setCellValueFactory(param -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> param.getValue().getMonthYearDisplay()
            )
        );
        
        amountColumn.setCellValueFactory(param -> {
            BigDecimal amount = param.getValue().getAmount();
            return javafx.beans.binding.Bindings.createStringBinding(
                () -> amount != null ? String.format("%,.0f", amount.doubleValue()) + " đ" : "0 đ"
            );
        });
        
        statusColumn.setCellValueFactory(param -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> param.getValue().getStatusDisplay()
            )
        );
        
        paymentDateColumn.setCellValueFactory(param -> {
            LocalDate date = param.getValue().getPaymentDate();
            return javafx.beans.binding.Bindings.createStringBinding(
                () -> date != null ? date.format(dateFormatter) : ""
            );
        });
    }
    
    @FXML
    private void handleSearch() {
        String apartmentCode = searchApartmentCodeField.getText().trim();
        String householdCode = searchHouseholdCodeField.getText().trim();
        String ownerName = searchOwnerNameField.getText().trim();
        Integer month = searchMonthCombo.getValue();
        Integer year = searchYearCombo.getValue();
        String status = searchStatusCombo.getValue();
        
        // Convert status display to database value
        String statusValue = null;
        if (status != null && !status.equals("Tất cả")) {
            statusValue = status.equals("Đã thu phí") ? "paid" : "unpaid";
        }
        
        try {
            List<FeeCollection> fees = feeService.searchFeeCollections(
                apartmentCode.isEmpty() ? null : apartmentCode,
                householdCode.isEmpty() ? null : householdCode,
                ownerName.isEmpty() ? null : ownerName,
                month,
                year,
                statusValue
            );
            feeList.clear();
            feeList.addAll(fees);
            updateStatistics();
        } catch (DbException e) {
            ErrorDialog.showDbError(e.getMessage());
        }
    }
    
    @FXML
    private void handleRefresh() {
        searchApartmentCodeField.clear();
        searchHouseholdCodeField.clear();
        searchOwnerNameField.clear();
        searchMonthCombo.setValue(null);
        searchYearCombo.setValue(LocalDate.now().getYear());
        searchStatusCombo.setValue("Tất cả");
        loadAllFeeCollections();
    }
    
    @FXML
    private void handleMarkPaid() {
        FeeCollection selected = feeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ErrorDialog.showError("Lỗi", "Vui lòng chọn một bản ghi thu phí");
            return;
        }
        
        if ("paid".equals(selected.getStatus())) {
            ErrorDialog.showInfo("Thông tin", "Bản ghi này đã được đánh dấu là đã thu phí");
            return;
        }
        
        // Hiển thị dialog để nhập thông tin thanh toán
        Dialog<PaymentInfo> dialog = createPaymentDialog();
        dialog.showAndWait().ifPresent(paymentInfo -> {
            try {
                feeService.markAsPaid(selected.getId(), paymentInfo.date, paymentInfo.method);
                
                // Lưu filter hiện tại
                String currentStatus = searchStatusCombo.getValue();
                boolean wasFilteringUnpaid = (currentStatus != null && currentStatus.equals("Chưa thu phí"));
                
                // Nếu đang filter theo "Chưa thu phí", reset về "Tất cả" để bản ghi vẫn hiển thị
                if (wasFilteringUnpaid) {
                    searchStatusCombo.setValue("Tất cả");
                }
                
                // Reload danh sách với filter mới
                handleSearch();
                
                // Tìm và select lại bản ghi vừa cập nhật
                for (FeeCollection fee : feeList) {
                    if (fee.getId().equals(selected.getId())) {
                        feeTable.getSelectionModel().select(fee);
                        feeTable.scrollTo(fee);
                        break;
                    }
                }
                
                ErrorDialog.showInfo("Thành công", "Đã đánh dấu đã thu phí thành công");
            } catch (DbException e) {
                ErrorDialog.showDbError(e.getMessage());
            }
        });
    }
    
    private Dialog<PaymentInfo> createPaymentDialog() {
        Dialog<PaymentInfo> dialog = new Dialog<>();
        dialog.setTitle("Xác nhận thu phí");
        dialog.setHeaderText("Nhập thông tin thanh toán");
        
        DatePicker datePicker = new DatePicker(LocalDate.now());
        ComboBox<String> methodCombo = new ComboBox<>();
        methodCombo.getItems().addAll("Tiền mặt", "Chuyển khoản", "Thẻ tín dụng");
        methodCombo.setValue("Tiền mặt");
        
        javafx.scene.layout.VBox vbox = new javafx.scene.layout.VBox(10);
        vbox.getChildren().addAll(
            new Label("Ngày thanh toán:"),
            datePicker,
            new Label("Phương thức thanh toán:"),
            methodCombo
        );
        dialog.getDialogPane().setContent(vbox);
        
        ButtonType confirmButton = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) {
                String method = methodCombo.getValue();
                String methodValue = method.equals("Tiền mặt") ? "cash" : 
                                   method.equals("Chuyển khoản") ? "bank_transfer" : "credit_card";
                return new PaymentInfo(datePicker.getValue(), methodValue);
            }
            return null;
        });
        
        return dialog;
    }
    
    private void loadAllFeeCollections() {
        try {
            List<FeeCollection> fees = feeService.getAllFeeCollections();
            feeList.clear();
            feeList.addAll(fees);
            feeTable.setItems(feeList);
            updateStatistics();
        } catch (DbException e) {
            // Nếu bảng chưa tồn tại, hiển thị thông báo và để danh sách trống
            if (e.getMessage().contains("does not exist") || e.getMessage().contains("relation") || e.getMessage().contains("table")) {
                ErrorDialog.showInfo("Thông báo", "Bảng fee_collections chưa được tạo. Vui lòng chạy file schema-fee-postgresql.sql trong database.");
            } else {
                ErrorDialog.showDbError(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDialog.showError("Lỗi", "Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }
    
    private void updateStatistics() {
        int total = feeList.size();
        long paid = feeList.stream().filter(f -> "paid".equals(f.getStatus())).count();
        long unpaid = total - paid;
        
        totalLabel.setText("Tổng số: " + total);
        paidLabel.setText("Đã thu: " + paid);
        unpaidLabel.setText("Chưa thu: " + unpaid);
    }
    
    private static class PaymentInfo {
        LocalDate date;
        String method;
        
        PaymentInfo(LocalDate date, String method) {
            this.date = date;
            this.method = method;
        }
    }
}

