package com.drink.dao;

import com.drink.model.Drink;
import com.drink.dao.BaseDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrinkDAO {

    // 查询所有
    public List<Drink> getAll() {
        List<Drink> list = new ArrayList<>();
        String sql = "SELECT * FROM drink";
        try (Connection conn = BaseDB.getConn();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Drink d = new Drink();
                d.setId(rs.getString("id"));
                d.setName(rs.getString("name"));
                d.setPrice(rs.getDouble("price"));
                d.setStock(rs.getInt("stock"));
                d.setCategory(rs.getString("category"));
                d.setSupplier(rs.getString("supplier"));
                d.setBrand(rs.getString("brand"));
                d.setSpec(rs.getString("spec"));
                d.setStatus(rs.getString("status"));
                list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 条件查询
    public List<Drink> search(String name, String category) {
        List<Drink> list = new ArrayList<>();
        String sql = "SELECT * FROM drink WHERE name LIKE ? AND category LIKE ?";
        try (Connection conn = BaseDB.getConn();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + name + "%");
            pst.setString(2, "%" + category + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Drink d = new Drink();
                d.setId(rs.getString("id"));
                d.setName(rs.getString("name"));
                d.setPrice(rs.getDouble("price"));
                d.setStock(rs.getInt("stock"));
                d.setCategory(rs.getString("category"));
                d.setSupplier(rs.getString("supplier"));
                d.setBrand(rs.getString("brand"));
                d.setSpec(rs.getString("spec"));
                d.setStatus(rs.getString("status"));
                list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 添加商品
    public void add(Drink d) throws Exception {
        String sql = "INSERT INTO drink(id,name,price,stock,category,supplier,brand,spec,status) VALUES(?,?,?,?,?,?,?,?,?)";
        Connection conn = BaseDB.getConn();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, d.getId());
        pst.setString(2, d.getName());
        pst.setDouble(3, d.getPrice());
        pst.setInt(4, d.getStock());
        pst.setString(5, d.getCategory());
        pst.setString(6, d.getSupplier());
        pst.setString(7, d.getBrand());
        pst.setString(8, d.getSpec());
        pst.setString(9, d.getStatus());

        try {
            pst.executeUpdate();
        } catch (Exception e) {
            throw new Exception("添加商品失败: " + e.getMessage());
        } finally {
            pst.close();
            conn.close();
        }
    }

    // 修改商品
    public void update(Drink d) throws Exception {
        String sql = "UPDATE drink SET name=?,price=?,stock=?,category=?,supplier=?,brand=?,spec=?,status=? WHERE id=?";
        Connection conn = BaseDB.getConn();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, d.getName());
        pst.setDouble(2, d.getPrice());
        pst.setInt(3, d.getStock());
        pst.setString(4, d.getCategory());
        pst.setString(5, d.getSupplier());
        pst.setString(6, d.getBrand());
        pst.setString(7, d.getSpec());
        pst.setString(8, d.getStatus());
        pst.setString(9, d.getId());

        try {
            pst.executeUpdate();
        } catch (Exception e) {
            throw new Exception("修改商品失败: " + e.getMessage());
        } finally {
            pst.close();
            conn.close();
        }
    }

    // 删除商品
    public void delete(String id) throws Exception {
        String sql = "DELETE FROM drink WHERE id=?";
        Connection conn = BaseDB.getConn();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, id);

        try {
            pst.executeUpdate();
        } catch (Exception e) {
            throw new Exception("删除商品失败: " + e.getMessage());
        } finally {
            pst.close();
            conn.close();
        }
    }

    // 扣库存
    public boolean subStock(String id, int num) {
        String sql = "UPDATE drink SET stock=stock-? WHERE id=? AND stock>=?";
        try (Connection conn = BaseDB.getConn();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, num);
            pst.setString(2, id);
            pst.setInt(3, num);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
