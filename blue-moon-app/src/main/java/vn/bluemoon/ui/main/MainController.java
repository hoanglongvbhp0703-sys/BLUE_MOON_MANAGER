package vn.bluemoon.ui.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vn.bluemoon.security.SessionManager;
import vn.bluemoon.util.ErrorDialog;

import java.io.IOException;
import java.util.Set;

/**
 * Controller for main view
 */
public class MainController {
    private Stage mainStage;
    
    @FXML
    private void handleUserManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/user/UserManagementView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 700);
            Stage stage = new Stage();
            stage.setTitle("Quản lý người dùng");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            ErrorDialog.showError("Lỗi", "Không thể mở màn hình quản lý người dùng: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleFunctionManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/function/FunctionManagementView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 900, 600);
            Stage stage = new Stage();
            stage.setTitle("Quản lý chức năng");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            ErrorDialog.showError("Lỗi", "Không thể mở màn hình quản lý chức năng: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleMenuManagement() {
        ErrorDialog.showInfo("Thông tin", "Chức năng tạo menu đang được phát triển");
    }
    
    @FXML
    private void handleResidentManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/resident/ResidentManagementView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1400, 800);
            Stage stage = new Stage();
            stage.setTitle("Quản lý nhân khẩu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            ErrorDialog.showError("Lỗi", "Không thể mở màn hình quản lý nhân khẩu: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleFeeCollection() {
        try {
            java.net.URL resource = getClass().getResource("/ui/fee/FeeCollectionView.fxml");
            if (resource == null) {
                ErrorDialog.showError("Lỗi", "Không tìm thấy file FXML: /ui/fee/FeeCollectionView.fxml");
                return;
            }
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1400, 800);
            Stage stage = new Stage();
            stage.setTitle("Quản lý thu phí");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDialog.showError("Lỗi", "Không thể mở màn hình quản lý thu phí: " + e.getMessage() + "\n" + e.getClass().getName());
        }
    }
    
    @FXML
    private void handleLogout() {
        try {
            // Xóa session
            SessionManager.getInstance().clearSession();
            
            // Đóng tất cả các cửa sổ con (các Stage được mở từ MainView)
            Stage primaryStage = getMainStage();
            javafx.collections.ObservableList<javafx.stage.Window> windows = javafx.stage.Window.getWindows();
            for (javafx.stage.Window window : windows) {
                if (window instanceof Stage) {
                    Stage stage = (Stage) window;
                    // Đóng tất cả các Stage trừ primary stage
                    if (stage != primaryStage && stage.isShowing()) {
                        stage.close();
                    }
                }
            }
            
            // Chuyển về màn hình đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login/LoginView.fxml"));
            Parent root = loader.load();
            
            // Set stage cho LoginController
            vn.bluemoon.ui.login.LoginController loginController = loader.getController();
            if (loginController != null) {
                loginController.setStage(primaryStage);
            }
            
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Blue Moon - Hệ thống quản lý chung cư");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDialog.showError("Lỗi", "Không thể đăng xuất: " + e.getMessage());
        }
    }
    
    /**
     * Lấy Stage chính (primary stage)
     */
    private Stage getMainStage() {
        if (mainStage != null) {
            return mainStage;
        }
        
        // Tìm primary stage từ các window đang mở
        for (javafx.stage.Window window : javafx.stage.Window.getWindows()) {
            if (window instanceof Stage) {
                Stage stage = (Stage) window;
                // Stage chính thường là stage đầu tiên hoặc stage có scene là MainView
                if (stage.getScene() != null && 
                    stage.getScene().getRoot() != null &&
                    stage.getScene().getRoot().getUserData() != null) {
                    mainStage = stage;
                    return stage;
                }
            }
        }
        
        // Nếu không tìm thấy, lấy stage đầu tiên
        Set<Stage> stages = javafx.stage.Window.getWindows().stream()
            .filter(window -> window instanceof Stage)
            .map(window -> (Stage) window)
            .filter(stage -> stage.isShowing())
            .collect(java.util.stream.Collectors.toSet());
        
        if (!stages.isEmpty()) {
            mainStage = stages.iterator().next();
            return mainStage;
        }
        
        return null;
    }
    
    /**
     * Set main stage (được gọi từ LoginController khi chuyển sang MainView)
     */
    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }
}

