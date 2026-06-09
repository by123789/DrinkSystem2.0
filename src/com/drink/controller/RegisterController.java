package com.drink.controller;
import com.drink.dao.UserDAO;
import com.drink.model.User;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {
    @FXML private TextField username, age, phone, address, hobby;
    @FXML private PasswordField password, repassword;
    @FXML private ComboBox<String> gender;
    private UserDAO dao = new UserDAO();

    @FXML
    public void initialize() {
        gender.getItems().addAll("男", "女", "其他");
    }

    @FXML
    public void register() {
        String name = username.getText().trim();
        String pwd = password.getText().trim();
        String rep = repassword.getText().trim();

        if (name.isEmpty() || pwd.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "用户名和密码不能为空").show();
            return;
        }
        if (!pwd.equals(rep)) {
            new Alert(Alert.AlertType.ERROR, "两次密码不一致").show();
            return;
        }

        User user = new User();
        user.setUsername(name);
        user.setPassword(pwd);

        try {
            user.setAge(age.getText().trim().isEmpty() ? 0 : Integer.parseInt(age.getText().trim()));
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "年龄必须是数字").show();
            return;
        }

        user.setPhone(phone.getText().trim());
        user.setAddress(address.getText().trim());
        user.setGender(gender.getValue() == null ? "" : gender.getValue());
        user.setHobby(hobby.getText().trim());

        if (dao.register(user)) {
            new Alert(Alert.AlertType.INFORMATION, "注册成功").show();
            back();
        } else {
            new Alert(Alert.AlertType.ERROR, "账号已存在").show();
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
