package com.drink.model;

public class Order {
    private int id;
    private String username;
    private String drinkId;
    private String drinkName;
    private double price;
    private int quantity;
    private double total;
    private int num;
    private double money;
    private String phone;
    private String consignee;
    private String address;
    private String createTime;
    private String category; // 饮料类型，数据库列名：category
    private String temperature; // 温度状态：冰镇、常温、热饮

    public Order() {}

    public Order(int id, String username, String drinkId, String drinkName, double price, int quantity, double total, int num, double money, String phone, String consignee, String address, String createTime, String category, String temperature) {
        this.id = id;
        this.username = username;
        this.drinkId = drinkId;
        this.drinkName = drinkName;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
        this.num = num;
        this.money = money;
        this.phone = phone;
        this.consignee = consignee;
        this.address = address;
        this.createTime = createTime;
        this.category = category;
        this.temperature = temperature;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getDrinkId() { return drinkId; }
    public void setDrinkId(String drinkId) { this.drinkId = drinkId; }

    public String getDrinkName() { return drinkName; }
    public void setDrinkName(String drinkName) { this.drinkName = drinkName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public int getNum() { return num; }
    public void setNum(int num) { this.num = num; }

    public double getMoney() { return money; }
    public void setMoney(double money) { this.money = money; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getConsignee() { return consignee; }
    public void setConsignee(String consignee) { this.consignee = consignee; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTemperature() { return temperature; }
    public void setTemperature(String temperature) { this.temperature = temperature; }
}
