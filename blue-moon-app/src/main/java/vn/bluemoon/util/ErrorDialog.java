package vn.bluemoon.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Utility class for showing error dialogs
 */
public class ErrorDialog {
    
    /**
     * Show error dialog
     * @param title Dialog title
     * @param message Error message
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show database error dialog
     * @param message Error message
     */
    public static void showDbError(String message) {
        showError("Lỗi cơ sở dữ liệu", 
            "Đã xảy ra lỗi khi kết nối hoặc thao tác với cơ sở dữ liệu:\n" + message);
    }

    /**
     * Show information dialog
     * @param title Dialog title
     * @param message Information message
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show confirmation dialog
     * @param title Dialog title
     * @param message Confirmation message
     * @return true if user confirmed
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}










