package com.drink.controller;

import com.drink.dao.UserDAO;
import com.drink.dao.orderDAO;
import com.drink.model.Info;
import com.drink.model.Order;
import com.drink.model.OrdersPara;
import com.drink.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserInfoController {

    @FXML private Label lblUsername, lblUserId, lblAge, lblGender, lblAddress, lblHobby, lblNation;
    @FXML private TextField tfNewUsername, tfPhone;
    @FXML private PasswordField pfNewPassword, pfConfirmPassword;
    @FXML private ComboBox<String> cbCategory, cbNation;
    @FXML private RadioButton rbMale, rbFemale;
    @FXML private ToggleGroup genderGroup;
    @FXML private CheckBox cbSport, cbRead, cbGame, cbTravel, cbCoding;
    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Integer> colOrderId, colQuantity;
    @FXML private TableColumn<Order, String> colDrinkName, colCategory, colConsignee, colPhone, colCreateTime;
    @FXML private TableColumn<Order, Double> colPrice, colTotal;
    @FXML private Label lblOrderCount;

    private final UserDAO userDAO = new UserDAO();
    private final orderDAO orderDao = new orderDAO();
    private User currentUser;

    @FXML
    public void initialize() {
        loadCurrentUserInfo();
        setupOrderTable();
        setupCategoryComboBox();
        setupNationComboBox();
    }

    private void loadCurrentUserInfo() {
        try {
            int userId = Info.userId;
            Connection conn = com.drink.dao.BaseDB.getConn();
            String sql = "SELECT * FROM user WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                currentUser = new User();
                currentUser.setId(rs.getInt("id"));
                currentUser.setUsername(rs.getString("username"));
                currentUser.setPassword(rs.getString("password"));
                currentUser.setAge(rs.getInt("age"));
                currentUser.setAddress(rs.getString("address"));
                currentUser.setPhone(rs.getString("phone"));
                currentUser.setGender(rs.getString("gender"));
                currentUser.setHobby(rs.getString("hobby"));
                currentUser.setNation(rs.getString("nation"));

                lblUsername.setText(currentUser.getUsername());
                lblUserId.setText(String.valueOf(currentUser.getId()));
                lblAge.setText(String.valueOf(currentUser.getAge()));
                lblGender.setText(currentUser.getGender());
                lblAddress.setText(currentUser.getAddress());
                lblHobby.setText(currentUser.getHobby());
                lblNation.setText(currentUser.getNation() != null ? currentUser.getNation() : "未设置");
                tfPhone.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");

                // 设置性别单选按钮
                if ("男".equals(currentUser.getGender())) {
                    rbMale.setSelected(true);
                } else {
                    rbFemale.setSelected(true);
                }

                // 设置民族下拉框
                if (currentUser.getNation() != null && !currentUser.getNation().isEmpty()) {
                    cbNation.setValue(currentUser.getNation());
                }

                // 设置爱好复选框
                String hobby = currentUser.getHobby();
                if (hobby != null && !hobby.isEmpty()) {
                    cbSport.setSelected(hobby.contains("运动"));
                    cbRead.setSelected(hobby.contains("阅读"));
                    cbGame.setSelected(hobby.contains("游戏"));
                    cbTravel.setSelected(hobby.contains("旅游"));
                    cbCoding.setSelected(hobby.contains("编码"));
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "未找到用户信息，请重新登录").show();
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "加载用户信息失败：" + e.getMessage()).show();
        }
    }

    private void setupOrderTable() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDrinkName.setCellValueFactory(new PropertyValueFactory<>("drinkName"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colConsignee.setCellValueFactory(new PropertyValueFactory<>("consignee"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colCreateTime.setCellValueFactory(new PropertyValueFactory<>("createTime"));
    }

    private void setupCategoryComboBox() {
        cbCategory.getItems().addAll("全部", "碳酸饮料", "茶饮", "矿泉水", "功能饮料", "奶茶", "果汁");
        cbCategory.setValue("全部");
    }

    private void setupNationComboBox() {
        cbNation.getItems().addAll(
                "汉族", "壮族", "满族", "回族", "苗族", "维吾尔族", "土家族", "彝族", "蒙古族", "藏族",
                "布依族", "侗族", "瑶族", "朝鲜族", "白族", "哈尼族", "哈萨克族", "黎族", "族", "畲族",
                "傈僳族", "仡佬族", "东乡族", "高山族", "拉祜族", "水族", "族", "纳西族", "羌族", "土族",
                "佬族", "锡伯族", "柯尔克孜族", "达斡尔族", "景颇族", "毛南族", "撒拉族", "布朗族", "塔吉克族",
                "阿昌族", "普米族", "鄂温克族", "怒族", "京族", "基诺族", "德昂族", "保安族", "俄罗斯族",
                "裕固族", "乌兹别克族", "门巴族", "鄂伦春族", "独龙族", "塔塔尔族", "赫哲族", "珞巴族"
        );
        cbNation.setValue("汉族");
    }

    @FXML
    public void saveInfo() {
        try {
            if (currentUser == null) {
                new Alert(Alert.AlertType.ERROR, "用户信息未加载").show();
                return;
            }

            String newUsername = tfNewUsername.getText().trim();
            String newPassword = pfNewPassword.getText().trim();
            String confirmPassword = pfConfirmPassword.getText().trim();
            String phone = tfPhone.getText().trim();

            boolean needUpdate = false;

            if (!newUsername.isEmpty()) {
                currentUser.setUsername(newUsername);
                Info.username = newUsername;
                lblUsername.setText(newUsername);
                needUpdate = true;
            }

            if (!newPassword.isEmpty()) {
                if (newPassword.length() < 6) {
                    new Alert(Alert.AlertType.WARNING, "密码长度不能少于6位").show();
                    return;
                }
                if (!newPassword.equals(confirmPassword)) {
                    new Alert(Alert.AlertType.WARNING, "两次输入的密码不一致").show();
                    return;
                }
                currentUser.setPassword(newPassword);
                needUpdate = true;
            }

            if (!phone.isEmpty()) {
                if (!phone.matches("^1[3-9]\\d{9}$")) {
                    new Alert(Alert.AlertType.WARNING, "请输入正确的手机号码").show();
                    return;
                }
                currentUser.setPhone(phone);
                needUpdate = true;
            }

            // 更新性别
            String selectedGender = rbMale.isSelected() ? "男" : "女";
            if (!selectedGender.equals(currentUser.getGender())) {
                currentUser.setGender(selectedGender);
                lblGender.setText(selectedGender);
                needUpdate = true;
            }

            // 更新民族
            String selectedNation = cbNation.getValue();
            if (selectedNation != null && !selectedNation.equals(currentUser.getNation())) {
                currentUser.setNation(selectedNation);
                lblNation.setText(selectedNation);
                needUpdate = true;
            }

            // 更新爱好
            List<String> hobbies = new ArrayList<>();
            if (cbSport.isSelected()) hobbies.add("运动");
            if (cbRead.isSelected()) hobbies.add("阅读");
            if (cbGame.isSelected()) hobbies.add("游戏");
            if (cbTravel.isSelected()) hobbies.add("旅游");
            if (cbCoding.isSelected()) hobbies.add("编码");

            String hobbyStr = String.join(",", hobbies);
            if (!hobbyStr.equals(currentUser.getHobby())) {
                currentUser.setHobby(hobbyStr);
                lblHobby.setText(hobbyStr.isEmpty() ? "无" : hobbyStr);
                needUpdate = true;
            }

            if (needUpdate) {
                userDAO.updateUser(currentUser);
                new Alert(Alert.AlertType.INFORMATION, "信息更新成功！").show();
                resetForm();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "没有修改任何信息").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "更新信息失败：" + e.getMessage()).show();
        }
    }

    @FXML
    public void resetForm() {
        tfNewUsername.clear();
        pfNewPassword.clear();
        pfConfirmPassword.clear();
        if (currentUser != null) {
            tfPhone.setText(currentUser.getPhone());

            // 重置性别
            if ("男".equals(currentUser.getGender())) {
                rbMale.setSelected(true);
            } else {
                rbFemale.setSelected(true);
            }

            // 重置民族
            if (currentUser.getNation() != null && !currentUser.getNation().isEmpty()) {
                cbNation.setValue(currentUser.getNation());
            }

            // 重置爱好
            String hobby = currentUser.getHobby();
            cbSport.setSelected(hobby != null && hobby.contains("运动"));
            cbRead.setSelected(hobby != null && hobby.contains("阅读"));
            cbGame.setSelected(hobby != null && hobby.contains("游戏"));
            cbTravel.setSelected(hobby != null && hobby.contains("旅游"));
            cbCoding.setSelected(hobby != null && hobby.contains("编码"));
        }
    }

    @FXML
    public void queryMyOrders() {
        try {
            OrdersPara para = new OrdersPara();
            String currentUsername = lblUsername.getText();
            para.setUname(currentUsername);

            String category = cbCategory.getValue();
            if ("全部".equals(category) || category == null) {
                para.setTname("");
            } else {
                para.setTname(category);
            }

            List<Order> orders = orderDao.queryOrders(para, 1, 100);
            ObservableList<Order> orderList = FXCollections.observableArrayList(orders);
            orderTable.setItems(orderList);

            lblOrderCount.setText("共 " + orders.size() + " 条订单");

            if (orders.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "当前用户暂无订单记录").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "查询订单失败：" + e.getMessage()).show();
        }
    }

    @FXML
    public void close() {
        ((Stage) lblUsername.getScene().getWindow()).close();
    }
}
