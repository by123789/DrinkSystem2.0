package com.drink.controller;

import com.drink.dao.DrinkTypeDAO;
import com.drink.model.DrinkType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class DrinkTypeController {

    @FXML private TableView<DrinkType> table;
    @FXML private TableColumn<DrinkType, Integer> colId;
    @FXML private TableColumn<DrinkType, String> colTypeName;
    @FXML private TableColumn<DrinkType, String> colDrinks;
    @FXML private TableColumn<DrinkType, String> colRemark;

    @FXML private TextField searchName, tfId, tfTypeName, tfRemark;

    private final DrinkTypeDAO dao = new DrinkTypeDAO();

    @FXML
    public void initialize() {
        // 禁用所有列的排序功能
        colId.setSortable(false);
        colTypeName.setSortable(false);
        colDrinks.setSortable(false);
        colRemark.setSortable(false);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTypeName.setCellValueFactory(new PropertyValueFactory<>("typeName"));

        // 设置"包含饮料"列的单元格工厂，添加 Tooltip
        colDrinks.setCellFactory(column -> new TableCell<DrinkType, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(item);
                    Tooltip tooltip = new Tooltip(item);
                    tooltip.setWrapText(true);
                    tooltip.setMaxWidth(500);
                    setTooltip(tooltip);
                }
            }
        });
        colDrinks.setCellValueFactory(new PropertyValueFactory<>("drinks"));

        colRemark.setCellValueFactory(new PropertyValueFactory<>("remark"));

        refresh();

        table.getSelectionModel().selectedItemProperty().addListener((o, old, val) -> {
            if (val != null) fill(val);
        });
    }

    private void fill(DrinkType type) {
        tfId.setText(String.valueOf(type.getId()));
        tfTypeName.setText(type.getTypeName());
        tfRemark.setText(type.getRemark() != null ? type.getRemark() : "");
    }

    @FXML
    public void search() {
        try {
            String name = searchName.getText().trim();
            List<DrinkType> list;

            if (name.isEmpty()) {
                list = dao.getAllTypes();
            } else {
                list = dao.searchByName(name);
            }

            ObservableList<DrinkType> observableList = FXCollections.observableArrayList(list);
            table.setItems(observableList);

            if (list.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "未找到相关分类").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "查询失败：" + e.getMessage()).show();
        }
    }


    @FXML
    public void refresh() {
        try {
            List<DrinkType> list = dao.getAllTypes();
            ObservableList<DrinkType> observableList = FXCollections.observableArrayList(list);
            table.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "加载数据失败：" + e.getMessage()).show();
        }
    }

    @FXML
    public void add() {
        try {
            String typeName = tfTypeName.getText().trim();
            String remark = tfRemark.getText().trim();

            if (typeName.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "分类名称不能为空！").show();
                return;
            }

            DrinkType type = new DrinkType();
            type.setTypeName(typeName);
            type.setRemark(remark);

            if (dao.add(type)) {
                new Alert(Alert.AlertType.INFORMATION, "添加成功！").show();
                refresh();
                reset();
            } else {
                new Alert(Alert.AlertType.ERROR, "添加失败！").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "添加失败：" + e.getMessage()).show();
        }
    }

    @FXML
    public void update() {
        try {
            DrinkType selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "请先选择要修改的分类！").show();
                return;
            }

            String typeName = tfTypeName.getText().trim();
            String remark = tfRemark.getText().trim();

            if (typeName.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "分类名称不能为空！").show();
                return;
            }

            DrinkType type = new DrinkType();
            type.setId(selected.getId());
            type.setTypeName(typeName);
            type.setRemark(remark);

            if (dao.update(type)) {
                new Alert(Alert.AlertType.INFORMATION, "修改成功！").show();
                refresh();
            } else {
                new Alert(Alert.AlertType.ERROR, "修改失败！").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "修改失败：" + e.getMessage()).show();
        }
    }

    @FXML
    public void delete() {
        DrinkType selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "请先选择要删除的分类！").show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("确认");
        confirm.setHeaderText(null);
        confirm.setContentText("确定要删除分类【" + selected.getTypeName() + "】吗？");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                if (dao.delete(selected.getId())) {
                    new Alert(Alert.AlertType.INFORMATION, "删除成功！").show();
                    refresh();
                    reset();
                } else {
                    new Alert(Alert.AlertType.ERROR, "删除失败！").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "删除失败：" + e.getMessage()).show();
            }
        }
    }

    @FXML
    public void reset() {
        tfId.clear();
        tfTypeName.clear();
        tfRemark.clear();
        searchName.clear();
        table.getSelectionModel().clearSelection();
    }

    @FXML
    public void close() {
        ((Stage) table.getScene().getWindow()).close();
    }
}
