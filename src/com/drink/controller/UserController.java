package com.drink.controller;

import com.drink.dao.UserDAO;
import com.drink.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class UserController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, Integer> colAge;
    @FXML private TableColumn<User, String> colAddress;
    @FXML private TableColumn<User, String> colPhone;
    @FXML private TableColumn<User, String> colGender;
    @FXML private TableColumn<User, String> colNation;
    @FXML private TableColumn<User, String> colHobby;
    @FXML private TableColumn<User, String> colPassword;

    @FXML private TextField tfUsername, tfAge, tfPhone, tfAddress;
    @FXML private RadioButton rbMale, rbFemale;
    @FXML private ToggleGroup genderGroup;
    @FXML private ComboBox<String> cbNation;
    @FXML private CheckBox cbSport, cbRead, cbGame, cbTravel, cbCoding;
    @FXML private PasswordField tfPassword;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colNation.setCellValueFactory(new PropertyValueFactory<>("nation"));
        colHobby.setCellValueFactory(new PropertyValueFactory<>("hobby"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        setupNationComboBox();

        refresh();

        userTable.getSelectionModel().selectedItemProperty().addListener((o, old, val) -> {
            if (val != null) fill(val);
        });
    }

    private void setupNationComboBox() {
        cbNation.getItems().addAll(
                "汉族", "壮族", "满族", "回族", "苗族", "维吾尔族", "土家族", "彝族", "蒙古族", "藏族",
                "布依族", "侗族", "瑶族", "朝鲜族", "白族", "哈尼族", "哈萨克族", "黎族", "傣族", "畲族",
                "傈僳族", "仡佬族", "东乡族", "高山族", "拉祜族", "水族", "族", "纳西族", "羌族", "土族",
                "仫佬族", "锡伯族", "柯尔克孜族", "达斡尔族", "景颇族", "毛南族", "撒拉族", "布朗族", "塔吉克族",
                "阿昌族", "普米族", "鄂温克族", "怒族", "京族", "基诺族", "德昂族", "保安族", "俄罗斯族",
                "裕固族", "乌兹别克族", "门巴族", "鄂伦春族", "独龙族", "塔塔尔族", "赫哲族", "珞巴族"
        );
        cbNation.setValue("汉族");
    }

    private void fill(User u) {
        tfUsername.setText(u.getUsername());
        tfAge.setText(String.valueOf(u.getAge()));
        tfPhone.setText(u.getPhone());
        tfAddress.setText(u.getAddress());

        // 设置性别
        if ("男".equals(u.getGender())) {
            rbMale.setSelected(true);
        } else {
            rbFemale.setSelected(true);
        }

        // 设置民族
        if (u.getNation() != null && !u.getNation().isEmpty()) {
            cbNation.setValue(u.getNation());
        } else {
            cbNation.setValue("汉族");
        }

        // 设置爱好
        String hobby = u.getHobby();
        cbSport.setSelected(hobby != null && hobby.contains("运动"));
        cbRead.setSelected(hobby != null && hobby.contains("阅读"));
        cbGame.setSelected(hobby != null && hobby.contains("游戏"));
        cbTravel.setSelected(hobby != null && hobby.contains("旅游"));
        cbCoding.setSelected(hobby != null && hobby.contains("编码"));

        tfPassword.setText(u.getPassword());
    }

    @FXML
    public void add() {
        try {
            String username = tfUsername.getText().trim();
            String password = tfPassword.getText().trim();
            String ageStr = tfAge.getText().trim();
            String phone = tfPhone.getText().trim();
            String address = tfAddress.getText().trim();

            // 获取性别
            String gender = rbMale.isSelected() ? "男" : "女";

            // 获取民族
            String nation = cbNation.getValue();
            if (nation == null || nation.isEmpty()) {
                nation = "汉族";
            }

            // 获取爱好
            List<String> hobbies = new ArrayList<>();
            if (cbSport.isSelected()) hobbies.add("运动");
            if (cbRead.isSelected()) hobbies.add("阅读");
            if (cbGame.isSelected()) hobbies.add("游戏");
            if (cbTravel.isSelected()) hobbies.add("旅游");
            if (cbCoding.isSelected()) hobbies.add("编码");
            String hobby = String.join(",", hobbies);

            if (username.isEmpty() || password.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "用户名和密码不能为空！").show();
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setAge(ageStr.isEmpty() ? 0 : Integer.parseInt(ageStr));
            user.setPhone(phone);
            user.setAddress(address);
            user.setGender(gender);
            user.setNation(nation);
            user.setHobby(hobby);

            if (userDAO.register(user)) {
                new Alert(Alert.AlertType.INFORMATION, "用户添加成功！").show();
                refresh();
                reset();
            } else {
                new Alert(Alert.AlertType.ERROR, "用户名已存在！").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "年龄必须是数字！").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "添加用户失败：" + e.getMessage()).show();
        }
    }

    @FXML
    public void update() {
        try {
            User selected = userTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "请先选择要修改的用户！").show();
                return;
            }

            String ageStr = tfAge.getText().trim();
            String phone = tfPhone.getText().trim();
            String address = tfAddress.getText().trim();

            // 获取性别
            String gender = rbMale.isSelected() ? "男" : "女";

            // 获取民族
            String nation = cbNation.getValue();
            if (nation == null || nation.isEmpty()) {
                nation = "汉族";
            }

            // 获取爱好
            List<String> hobbies = new ArrayList<>();
            if (cbSport.isSelected()) hobbies.add("运动");
            if (cbRead.isSelected()) hobbies.add("阅读");
            if (cbGame.isSelected()) hobbies.add("游戏");
            if (cbTravel.isSelected()) hobbies.add("旅游");
            if (cbCoding.isSelected()) hobbies.add("编码");
            String hobby = String.join(",", hobbies);

            String password = tfPassword.getText().trim();

            User user = new User();
            user.setId(selected.getId());
            user.setUsername(selected.getUsername());
            user.setPassword(password.isEmpty() ? selected.getPassword() : password);
            user.setAge(ageStr.isEmpty() ? 0 : Integer.parseInt(ageStr));
            user.setPhone(phone);
            user.setAddress(address);
            user.setGender(gender);
            user.setNation(nation);
            user.setHobby(hobby);

            userDAO.updateUser(user);
            new Alert(Alert.AlertType.INFORMATION, "修改成功！").show();
            refresh();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "年龄必须是数字！").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "修改用户失败：" + e.getMessage()).show();
        }
    }

    @FXML
    public void delete() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "请先选择要删除的用户！").show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("确认");
        confirm.setHeaderText(null);
        confirm.setContentText("确定要删除用户【" + selected.getUsername() + "】吗？");
        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                userDAO.deleteUser(selected.getUsername());
                new Alert(Alert.AlertType.INFORMATION, "删除成功！").show();
                refresh();
                reset();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "删除用户失败：" + e.getMessage()).show();
            }
        }
    }

    @FXML
    public void refresh() {
        userTable.setItems(getData());
    }

    @FXML
    public void reset() {
        tfUsername.clear();
        tfAge.clear();
        tfPhone.clear();
        tfAddress.clear();
        tfPassword.clear();

        // 重置性别
        rbFemale.setSelected(true);

        // 重置民族
        cbNation.setValue("汉族");

        // 重置爱好
        cbSport.setSelected(false);
        cbRead.setSelected(false);
        cbGame.setSelected(false);
        cbTravel.setSelected(false);
        cbCoding.setSelected(false);

        userTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void close() {
        ((Stage) userTable.getScene().getWindow()).close();
    }

    private ObservableList<User> getData() {
        ObservableList<User> list = FXCollections.observableArrayList();
        try {
            java.sql.Connection conn = com.drink.dao.BaseDB.getConn();
            String sql = "SELECT id,username,password,age,address,phone,gender,nation,hobby FROM user";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setAge(rs.getInt("age"));
                u.setAddress(rs.getString("address"));
                u.setPhone(rs.getString("phone"));
                u.setGender(rs.getString("gender"));
                u.setNation(rs.getString("nation"));
                u.setHobby(rs.getString("hobby"));
                list.add(u);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
