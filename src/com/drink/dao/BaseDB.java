package com.drink.dao;
import java.sql.*;

public class BaseDB {
    public static final String URL = "jdbc:mysql://localhost:3306/drink_system2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    public static final String USER = "root";
    public static final String PWD = "123456";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() {
        try {
            return DriverManager.getConnection(URL, USER, PWD);
        } catch (Exception e) {
            System.out.println("数据库连接失败!!!");
            e.printStackTrace();
            return null;
        }
    }
}