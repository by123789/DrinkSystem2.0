package com.drink.controller;

import com.drink.dao.DrinkDAO;
import com.drink.model.Drink;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

import javafx.scene.Node;
import javafx.event.ActionEvent;

public class AdminController {
    @FXML private TableView<Drink> table;
    @FXML private TableColumn<Drink, String> id, name, category, supplier, brand, spec;
    @FXML private TableColumn<Drink, Double> price;
    @FXML private TableColumn<Drink, Integer> stock, status;

    @FXML private TextField searchName;
    @FXML private ComboBox<String> searchCategory;

    @FXML private TextField tfId, tfName, tfPrice, tfStock, tfSupplier, tfBrand, tfSpec;
    @FXML private ComboBox<String> tfCategory, tfStatus;

    private final DrinkDAO drinkDAO = new DrinkDAO();

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        supplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        spec.setCellValueFactory(new PropertyValueFactory<>("spec"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        String[] cats = {"全部", "碳酸饮料", "茶饮", "矿泉水", "功能饮料", "奶茶", "果汁"};
        searchCategory.getItems().addAll(cats);
        searchCategory.setValue("全部");
        tfCategory.getItems().addAll(cats);
        tfStatus.getItems().addAll("常温", "冰镇", "热饮");
        tfStatus.setValue("常温");

        refresh();

        table.getSelectionModel().selectedItemProperty().addListener((o, old, val) -> {
            if (val != null) fill(val);
        });
    }

    private void fill(Drink d) {
        tfId.setText(d.getId());
        tfName.setText(d.getName());
        tfPrice.setText(String.valueOf(d.getPrice()));
        tfStock.setText(String.valueOf(d.getStock()));
        tfCategory.setValue(d.getCategory());
        tfSupplier.setText(d.getSupplier());
        tfBrand.setText(d.getBrand());
        tfSpec.setText(d.getSpec());
        tfStatus.setValue(d.getStatus());
    }

    @FXML
    public void add() {
        try {
            String id = tfId.getText().trim();
            String name = tfName.getText().trim();
            String priceStr = tfPrice.getText().trim();
            String stockStr = tfStock.getText().trim();
            String category = tfCategory.getValue();

            if (id.isEmpty() || name.isEmpty() || category == null || priceStr.isEmpty() || stockStr.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("请填写完整信息！");
                alert.showAndWait();
                return;
            }

            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);
            String supplier = tfSupplier.getText().trim();
            String brand = tfBrand.getText().trim();
            String spec = tfSpec.getText().trim();
            String status = tfStatus.getValue();

            Drink drink = new Drink();
            drink.setId(id);
            drink.setName(name);
            drink.setPrice(price);
            drink.setStock(stock);
            drink.setCategory(category);
            drink.setSupplier(supplier);
            drink.setBrand(brand);
            drink.setSpec(spec);
            drink.setStatus(status);

            drinkDAO.add(drink);

            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("成功");
            success.setHeaderText(null);
            success.setContentText("商品添加成功！");
            success.showAndWait();

            refresh();
            reset();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("价格/库存必须是数字！");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "添加商品失败：" + e.getMessage()).show();
        }
    }

    @FXML
    public void update() {
        try {
            String id = tfId.getText().trim();
            if (id.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "请先选商品！").showAndWait();
                return;
            }
            Drink d = new Drink();
            d.setId(id);
            d.setName(tfName.getText().trim());
            d.setPrice(Double.parseDouble(tfPrice.getText().trim()));
            d.setStock(Integer.parseInt(tfStock.getText().trim()));
            d.setCategory(tfCategory.getValue());
            d.setSupplier(tfSupplier.getText().trim());
            d.setBrand(tfBrand.getText().trim());
            d.setSpec(tfSpec.getText().trim());
            d.setStatus(tfStatus.getValue());

            drinkDAO.update(d);
            new Alert(Alert.AlertType.INFORMATION, "修改成功！").showAndWait();
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "修改商品失败：" + e.getMessage()).show();
        }
    }

    @FXML
    public void delete() {
        Drink d = table.getSelectionModel().getSelectedItem();
        if (d == null) {
            new Alert(Alert.AlertType.WARNING, "请先选商品！").showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("确认");
        confirm.setHeaderText(null);
        confirm.setContentText("确定要删除商品【" + d.getName() + "】吗？");
        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                drinkDAO.delete(d.getId());
                new Alert(Alert.AlertType.INFORMATION, "删除成功！").showAndWait();
                refresh();
                reset();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "删除商品失败：" + e.getMessage()).show();
            }
        }
    }

    @FXML
    public void search() {
        String name = searchName.getText().trim();
        String cate = "全部".equals(searchCategory.getValue()) ? "" : searchCategory.getValue();
        List<Drink> list = drinkDAO.search(name, cate);
        table.getItems().setAll(list);
    }

    @FXML
    public void refresh() {
        List<Drink> list = drinkDAO.getAll();
        table.getItems().setAll(list);
    }

    @FXML
    public void reset() {
        tfId.clear();
        tfName.clear();
        tfPrice.clear();
        tfStock.clear();
        tfSupplier.clear();
        tfBrand.clear();
        tfSpec.clear();
        tfCategory.setValue(null);
        tfStatus.setValue("常温");
        table.getSelectionModel().clearSelection();
    }

    @FXML
    public void logout() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        ((Stage) table.getScene().getWindow()).setScene(new Scene(root));
    }

    @FXML
    public void close() {
        ((Stage) table.getScene().getWindow()).close();
    }

    @FXML
    public void orderList(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/order.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("订单查询系统");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "打开订单页面失败：" + e.getMessage()).show();
        }
    }

    @FXML
    public void openUserManager() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/user.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("用户信息管理");
            stage.initOwner(((Stage) table.getScene().getWindow()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "打开用户管理页面失败：" + e.getMessage()).show();
        }
    }
}
