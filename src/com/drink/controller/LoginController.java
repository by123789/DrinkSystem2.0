package com.drink.controller;

import com.drink.dao.UserDAO;
import com.drink.model.Info;
import com.drink.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    public static String loginUser;
    public static int loginUserId;

    @FXML private TextField username;
    @FXML private PasswordField password;

    private UserDAO dao = new UserDAO();

    @FXML
    public void login() {
        try {
            String name = username.getText().trim();
            String pwd = password.getText().trim();

            User u = dao.login(name, pwd);
            if (u == null) {
                new Alert(Alert.AlertType.ERROR, "账号或密码错误").show();
                return;
            }
            Info.userId=u.getId();
            Info.username=name;
            loginUser = name;
            loginUserId = u.getId();

            // 加载主界面
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();

            // 获取 MainController 并注入 Stage
            MainController mainController = loader.getController();
            Stage currentStage = (Stage) username.getScene().getWindow();
            mainController.setMainStage(currentStage);

            // 替换当前窗口的 Scene
            currentStage.setScene(new Scene(root));
            currentStage.centerOnScreen();
            currentStage.setTitle("饮料售卖系统");

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "登录失败：" + e.getMessage()).show();
        }
    }

    @FXML
    public void goRegister() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/register.fxml"));
        Stage stage = (Stage) username.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    public void goForget() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/forget.fxml"));
        Stage stage = (Stage) username.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
