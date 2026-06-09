package com.drink.controller;
import com.drink.dao.UserDAO;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ForgetController {
    @FXML private TextField username;
    @FXML private PasswordField newPwd, reNewPwd;
    private UserDAO dao = new UserDAO();

    @FXML
    public void reset() {
        String name = username.getText().trim();
        String pwd = newPwd.getText().trim();
        String rep = reNewPwd.getText().trim();

        if (!pwd.equals(rep)) {
            new Alert(Alert.AlertType.ERROR, "两次密码不一致").show();
            return;
        }
        if (dao.resetPwd(name, pwd)) {
            new Alert(Alert.AlertType.INFORMATION, "重置成功").show();
            back();
        } else {
            new Alert(Alert.AlertType.ERROR, "账号不存在").show();
        }
    }

    @FXML
    public void back() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            ((Stage) username.getScene().getWindow()).setScene(new Scene(root));
        } catch (Exception e) {}
    }
}