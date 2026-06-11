package com.drink.model;

public class Drink {
    private String id;
    private String name;
    private double price;
    private int stock;
    private String category;
    private String supplier;
    private String brand;
    private String spec;
    private String status;
    private String temperature;
    private int buyNum;       // 购买数量
    private double totalMoney;// 小计

    public Drink() {}

    public Drink(String id, String name, double price, int stock, String category, String supplier, String brand, String spec, String status, String temperature, int buyNum, double totalMoney) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.supplier = supplier;
        this.brand = brand;
        this.spec = spec;
        this.status = status;
        this.temperature = temperature;
        this.buyNum = buyNum;
        this.totalMoney = totalMoney;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getSpec() { return spec; }
    public void setSpec(String spec) { this.spec = spec; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTemperature() { return temperature; }
    public void setTemperature(String temperature) { this.temperature = temperature; }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
