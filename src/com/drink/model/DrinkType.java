package com.drink.model;

public class DrinkType {
    private int id;
    private String typeName;
    private String remark;
    private String drinks; // 新增：包含的饮料列表

    public DrinkType() {}

    public DrinkType(int id, String typeName, String remark) {
        this.id = id;
        this.typeName = typeName;
        this.remark = remark;
    }

    public DrinkType(int id, String typeName, String remark, String drinks) {
        this.id = id;
        this.typeName = typeName;
        this.remark = remark;
        this.drinks = drinks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDrinks() {
        return drinks;
    }

    public void setDrinks(String drinks) {
        this.drinks = drinks;
    }
}
