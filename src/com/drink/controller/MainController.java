package com.drink.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import com.drink.model.Info;

public class MainController {
    @FXML
    private Label txuid;

    @FXML
    private Label txuname;
    @FXML
    private AnchorPane contentPane;
    // 保存主窗口
    private Stage mainStage;

    // 启动类注入主Stage
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @FXML
    public void initialize() {
        txuid.setText(String.valueOf(Info.userId));
        txuname.setText(Info.username);
    }

    // ========== 退出登录 ==========
    @FXML
    private void logout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("确认");
        confirm.setHeaderText(null);
        confirm.setContentText("确定要退出登录吗？");

        if (confirm.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            try {
                // 打开登录窗口
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
                Parent root = loader.load();
                Stage loginStage = new Stage();
                loginStage.setTitle("饮料售卖系统 - 登录");
                loginStage.setScene(new javafx.scene.Scene(root));
                loginStage.centerOnScreen();
                loginStage.setResizable(false);

                // 设置登录窗口关闭时，程序退出
                loginStage.setOnCloseRequest(e -> System.exit(0));

                // 先显示登录窗口
                loginStage.show();

                // 再关闭主窗口（确保登录窗口已经显示）
                if (mainStage != null) {
                    mainStage.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "打开登录窗口失败：" + e.getMessage()).show();
            }
        }
    }

    // ========== 5个模态弹窗（和你分类弹窗格式统一） ==========
    @FXML
    private void openBuyModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/buy.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("用户下单");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(mainStage);
            stage.centerOnScreen();
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openUserInfoModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user-info.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("个人信息维护");
            stage.setScene(new javafx.scene.Scene(root));
            stage.setMinWidth(1100);
            stage.setMinHeight(850);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(mainStage);
            stage.centerOnScreen();
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAdminModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("商品管理");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(mainStage);
            stage.centerOnScreen();
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openOrderModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/order.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("订单查询");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(mainStage);
            stage.centerOnScreen();
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openUserModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("用户管理");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(mainStage);
            stage.centerOnScreen();
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openDrinkTypeModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/drink-type.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("饮料分类管理");
            stage.setScene(new javafx.scene.Scene(root));
            stage.setMinWidth(1000);
            stage.setMinHeight(700);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(mainStage);
            stage.centerOnScreen();
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 【原有：新增商品弹窗不动，已经是模态】
    public void openAddModal() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add-goods.fxml"));
        Parent root = loader.load();

        AddGoodsController addController = loader.getController();
        addController.setMainController(this);

        Stage modalStage = new Stage();
        modalStage.setTitle("新增商品");
        modalStage.setScene(new javafx.scene.Scene(root));
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(contentPane.getScene().getWindow());
        modalStage.showAndWait();
    }

    // 保留此方法供 AddGoodsController 调用（虽然现在是模态窗口，但保持兼容）
    public void refreshGoodsTable() {
        // 由于 admin 页面现在是独立模态窗口，这个方法实际上不需要了
        // 但为了兼容 AddGoodsController 的调用，保留空实现
        System.out.println("refreshGoodsTable called - admin is now a modal window");
    }
}
