package vn.bluemoon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vn.bluemoon.ui.login.LoginController;

/**
 * Main application class
 */
public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login/LoginView.fxml"));
        Parent root = loader.load();
        
        LoginController controller = loader.getController();
        controller.setStage(primaryStage);
        
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Blue Moon - Hệ thống quản lý chung cư");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

