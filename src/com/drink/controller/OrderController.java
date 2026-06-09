package com.drink.controller;

import com.drink.dao.orderDAO;
import com.drink.model.Order;
import com.drink.model.OrdersPara;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class OrderController {

    @FXML private TextField oid, uname, dname, receiver, phone, start, end;
    @FXML private ComboBox<String> tname, pageNo;
    @FXML private TableView<Order> table;
    @FXML private TableColumn<Order, Integer> cid, cnum;
    @FXML private TableColumn<Order, String> cusername, ctname, cdname, creceiver, caddress, cphone, ctime;
    @FXML private TableColumn<Order, Double> ctotal;
    @FXML private Label pageInfo1, pageInfo2;

    private final orderDAO dao = new orderDAO();
    private final OrdersPara para = new OrdersPara();
    private static final int PAGE_SIZE = 10;

    @FXML
    public void initialize() {
        cid.setCellValueFactory(new PropertyValueFactory<>("id"));
        cusername.setCellValueFactory(new PropertyValueFactory<>("username"));
        ctname.setCellValueFactory(new PropertyValueFactory<>("category"));
        cdname.setCellValueFactory(new PropertyValueFactory<>("drinkName"));
        cnum.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        ctotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        creceiver.setCellValueFactory(new PropertyValueFactory<>("consignee"));
        caddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        cphone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        ctime.setCellValueFactory(new PropertyValueFactory<>("createTime"));

        tname.getItems().addAll("全部","碳酸饮料","茶饮","矿泉水","功能饮料","奶茶","果汁");
        tname.setValue("全部");

        pageNo.getItems().addAll("1","2","3","4","5");
        pageNo.setValue("1");

        // ✅ 切换类型自动回到第1页
        tname.setOnAction(e -> {
            pageNo.setValue("1");
            search();
        });

        pageNo.setOnAction(e -> search());
        search();
    }

    @FXML
    public void search() {
        try {
            para.setOid(oid.getText().trim());
            para.setUname(uname.getText().trim());
            para.setDname(dname.getText().trim());
            para.setReceiver(receiver.getText().trim());
            para.setPhone(phone.getText().trim());
            para.setStart(start.getText().trim());
            para.setEnd(end.getText().trim());

            // ✅ 饮料类型
            String type = tname.getValue().equals("全部") ? "" : tname.getValue();
            para.setTname(type);

            // ✅ 当前页码
            int page = Integer.parseInt(pageNo.getValue());

            // ✅ 查询
            List<Order> list = dao.queryOrders(para, page, PAGE_SIZE);
            table.getItems().setAll(list);

            // ✅ 分页信息
            int total = dao.countOrders(para);
            int totalPage = (total + PAGE_SIZE - 1) / PAGE_SIZE;

            if (total == 0) {
                pageInfo1.setText("第0页 共0页");
                pageInfo2.setText("共0条记录");
            } else {
                pageInfo1.setText("第" + page + "页 共" + totalPage + "页");
                pageInfo2.setText("共" + total + "条记录");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}