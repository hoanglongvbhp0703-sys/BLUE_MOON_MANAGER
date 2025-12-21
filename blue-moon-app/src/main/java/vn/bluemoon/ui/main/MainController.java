package vn.bluemoon.ui.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import vn.bluemoon.util.ErrorDialog;

import java.io.IOException;

/**
 * Controller for main view
 */
public class MainController {
    
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
    private void handleLogout() {
        try {
            Stage currentStage = (Stage) ((MenuItem) null).getParentPopup().getOwnerWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login/LoginView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            currentStage.setTitle("Blue Moon - Hệ thống quản lý chung cư");
            currentStage.setScene(scene);
        } catch (Exception e) {
            // If we can't get the stage from menu, create a new one
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login/LoginView.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 800, 600);
                Stage stage = new Stage();
                stage.setTitle("Blue Moon - Hệ thống quản lý chung cư");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ErrorDialog.showError("Lỗi", "Không thể đăng xuất: " + ex.getMessage());
            }
        }
    }
}

