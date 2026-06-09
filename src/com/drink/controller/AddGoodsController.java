package com.drink.controller;

import com.drink.dao.DrinkDAO;
import com.drink.model.Drink;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddGoodsController {

    @FXML
    private TextField nameField;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void save() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showAlert("提示", "商品名称不能为空！", Alert.AlertType.WARNING);
            return;
        }

        Drink drink = new Drink();
        drink.setId("D" + System.currentTimeMillis());
        drink.setName(name);
        drink.setPrice(0.0);
        drink.setStock(0);
        drink.setCategory("其他");
        drink.setSupplier("");
        drink.setBrand("");
        drink.setSpec("");
        drink.setStatus("常温");

        try {
            new DrinkDAO().add(drink);

            if (mainController != null) {
                mainController.refreshGoodsTable();
            }

            showAlert("成功", "商品添加成功！", Alert.AlertType.INFORMATION);
            closeStage();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("错误", "商品添加失败：" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void cancel() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
