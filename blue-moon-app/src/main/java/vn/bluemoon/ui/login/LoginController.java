package vn.bluemoon.ui.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import vn.bluemoon.exception.AuthException;
import vn.bluemoon.exception.DbException;
import vn.bluemoon.model.dto.LoginRequest;
import vn.bluemoon.security.SessionManager;
import vn.bluemoon.service.AuthService;
import vn.bluemoon.util.ErrorDialog;
import vn.bluemoon.validation.ValidationException;

import java.io.IOException;

/**
 * Controller for login view
 */
public class LoginController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button registerButton;
    
    @FXML
    private Hyperlink forgotPasswordLink;
    
    @FXML
    private Label errorLabel;
    
    private Stage stage;
    private AuthService authService = new AuthService();
    private SessionManager sessionManager = SessionManager.getInstance();
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    private void handleLogin() {
        errorLabel.setVisible(false);
        
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu");
            return;
        }
        
        try {
            LoginRequest request = new LoginRequest(username, password);
            String sessionToken = authService.login(request);
            
            // Store session token
            sessionManager.createSession(authService.getCurrentUser(sessionToken));
            
            // Navigate to main application
            navigateToMain(sessionToken);
            
        } catch (ValidationException e) {
            showError(e.getMessage());
        } catch (AuthException e) {
            showError(e.getMessage());
        } catch (DbException e) {
            ErrorDialog.showDbError(e.getMessage());
        } catch (Exception e) {
            ErrorDialog.showError("Lỗi", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/register/RegisterView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 500);
            Stage registerStage = new Stage();
            registerStage.setTitle("Đăng ký");
            registerStage.setScene(scene);
            registerStage.setResizable(false);
            registerStage.show();
        } catch (IOException e) {
            ErrorDialog.showError("Lỗi", "Không thể mở màn hình đăng ký: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleForgotPassword() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Quên mật khẩu");
        dialog.setHeaderText("Nhập email của bạn");
        dialog.setContentText("Email:");
        
        dialog.showAndWait().ifPresent(email -> {
            try {
                vn.bluemoon.service.PasswordResetService service = new vn.bluemoon.service.PasswordResetService();
                service.requestPasswordReset(email);
                ErrorDialog.showInfo("Thành công", "Email đặt lại mật khẩu đã được gửi đến " + email);
            } catch (Exception e) {
                ErrorDialog.showError("Lỗi", "Không thể gửi email đặt lại mật khẩu: " + e.getMessage());
            }
        });
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void navigateToMain(String sessionToken) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main/MainView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 800);
            if (stage != null) {
                stage.setTitle("Blue Moon - Hệ thống quản lý chung cư");
                stage.setScene(scene);
                stage.setResizable(true);
            }
        } catch (IOException e) {
            ErrorDialog.showError("Lỗi", "Không thể mở màn hình chính: " + e.getMessage());
        }
    }
}

