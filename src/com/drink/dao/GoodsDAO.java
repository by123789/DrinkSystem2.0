package com.drink.dao;

import com.drink.model.Goods;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoodsDAO {
    // 数据库连接（改成你自己的）
    private static final String URL = "jdbc:mysql://localhost:3306/drink?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PWD = "123456";

    // 新增
    public void add(Goods goods) {
        String sql = "insert into goods(name) values(?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PWD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, goods.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询所有
    public List<Goods> findAll() {
        List<Goods> list = new ArrayList<>();
        String sql = "select id,name from goods";
        try (Connection conn = DriverManager.getConnection(URL, USER, PWD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Goods g = new Goods();
                g.setId(rs.getInt("id"));
                g.setName(rs.getString("name"));
                list.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}