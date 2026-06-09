package com.drink.controller;

import com.drink.dao.DrinkDAO;
import com.drink.dao.orderDAO;
import com.drink.model.Drink;
import com.drink.model.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class BuyController {
    @FXML private TableView<Drink> table;
    @FXML private TableColumn<Drink, String> id, name, category, supplier, brand, spec;
    @FXML private TableColumn<Drink, Double> price;
    @FXML private TableColumn<Drink, Integer> stock;

    @FXML private TableView<Drink> cartTable;
    @FXML private TableColumn<Drink, String> cartId, cartName;
    @FXML private TableColumn<Drink, Double> cartPrice;
    @FXML private TableColumn<Drink, Integer> cartNum;
    @FXML private TableColumn<Drink, Double> cartMoney;

    @FXML private TextField tname, tid, tname2, tprice, tstock, tcategory2, tsupplier, tbrand, tspec;
    @FXML private TextField treceiver, taddress, tphone, tmoney;
    @FXML private ComboBox<String> tcategory;
    @FXML private Spinner<Integer> tnum;
    @FXML private Label lblUserId;
    @FXML private Label lblUsername;

    private final List<Drink> cartList = new ArrayList<>();
    private final DrinkDAO drinkDAO = new DrinkDAO();
    private final orderDAO orderDAO = new orderDAO();

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

        cartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        cartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        cartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        cartNum.setCellValueFactory(new PropertyValueFactory<>("buyNum"));
        cartMoney.setCellValueFactory(new PropertyValueFactory<>("totalMoney"));

        table.getItems().addAll(drinkDAO.getAll());
        tnum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        tcategory.getItems().addAll("全部", "饮料", "茶饮", "矿泉水", "功能饮料", "奶茶", "果汁");
        tcategory.setValue("全部");

        table.getSelectionModel().selectedItemProperty().addListener((o, old, val) -> {
            if (val != null) fillData(val);
        });

        if (LoginController.loginUser != null) {
            lblUserId.setText(String.valueOf(LoginController.loginUserId));
            lblUsername.setText(LoginController.loginUser);
        }
    }

    private void fillData(Drink d) {
        tid.setText(d.getId());
        tname2.setText(d.getName());
        tprice.setText(d.getPrice() + "");
        tstock.setText(d.getStock() + "");
        tcategory2.setText(d.getCategory());
        tsupplier.setText(d.getSupplier());
        tbrand.setText(d.getBrand());
        tspec.setText(d.getSpec());
    }

    @FXML
    public void addToCart() {
        Drink d = table.getSelectionModel().getSelectedItem();
        if (d == null) {
            new Alert(Alert.AlertType.ERROR, "请选择商品").show();
            return;
        }
        int num = tnum.getValue();
        if (num > d.getStock()) {
            new Alert(Alert.AlertType.ERROR, "库存不足").show();
            return;
        }

        Drink cartItem = new Drink();
        cartItem.setId(d.getId());
        cartItem.setName(d.getName());
        cartItem.setPrice(d.getPrice());
        cartItem.setCategory(d.getCategory());
        cartItem.setBuyNum(num);
        cartItem.setTotalMoney(num * d.getPrice());

        cartList.add(cartItem);
        cartTable.getItems().setAll(cartList);
        calculateTotal();
    }

    @FXML
    public void deleteCart() {
        Drink selected = cartTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.ERROR, "请选择要删除的商品").show();
            return;
        }
        cartList.remove(selected);
        cartTable.getItems().setAll(cartList);
        calculateTotal();
    }

    @FXML
    public void clearCart() {
        cartList.clear();
        cartTable.getItems().clear();
        tmoney.setText("");
    }

    private void calculateTotal() {
        double total = cartList.stream().mapToDouble(Drink::getTotalMoney).sum();
        tmoney.setText(String.format("%.2f", total));
    }

    @FXML
    public void save() {
        if (cartTable.getItems().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "购物车为空，无法下单").show();
            return;
        }
        if (treceiver.getText().isBlank() || tphone.getText().isBlank() || taddress.getText().isBlank()) {
            new Alert(Alert.AlertType.ERROR, "请填写完整的收货人信息").show();
            return;
        }

        boolean allSuccess = true;
        for (Drink d : cartTable.getItems()) {
            if (!drinkDAO.subStock(d.getId(), d.getBuyNum())) {
                allSuccess = false;
                break;
            }

            Order o = new Order();
            o.setUsername(LoginController.loginUser);
            o.setDrinkId(d.getId());
            o.setDrinkName(d.getName());
            o.setPrice(d.getPrice());
            o.setQuantity(d.getBuyNum());
            o.setTotal(d.getTotalMoney());
            o.setCategory(d.getCategory());

            o.setConsignee(treceiver.getText().trim());
            o.setPhone(tphone.getText().trim());
            o.setAddress(taddress.getText().trim());

            orderDAO.add(o);
        }

        if (allSuccess) {
            new Alert(Alert.AlertType.INFORMATION, "下单成功！").show();
            cartTable.getItems().clear();
            treceiver.clear();
            tphone.clear();
            taddress.clear();
        } else {
            new Alert(Alert.AlertType.ERROR, "下单失败，库存不足").show();
        }
    }

    @FXML
    public void search() {
        String name = tname.getText().trim();
        String cate = tcategory.getValue() == null ? "" : tcategory.getValue();
        if ("全部".equals(cate)) {
            cate = "";
        }
        table.getItems().setAll(drinkDAO.search(name, cate));
    }

    @FXML
    public void reset() {
        table.getSelectionModel().clearSelection();
        tid.clear();
        tname2.clear();
        tprice.clear();
        tstock.clear();
        treceiver.clear();
        taddress.clear();
        tphone.clear();
        tnum.getValueFactory().setValue(1);
    }

    @FXML
    public void add() {}

    @FXML
    public void update() {}

    @FXML
    public void delete() {}

    @FXML
    public void logout() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        ((Stage) table.getScene().getWindow()).setScene(new Scene(root));
    }

    @FXML
    public void close() {
        ((Stage) table.getScene().getWindow()).close();
    }
}
