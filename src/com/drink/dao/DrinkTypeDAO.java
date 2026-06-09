package com.drink.dao;

import com.drink.model.DrinkType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrinkTypeDAO {

    // 查询所有分类（带饮料列表）
    public List<DrinkType> getAllTypes() throws SQLException {
        List<DrinkType> list = new ArrayList<>();
        Connection conn = BaseDB.getConn();
        if (conn == null) {
            System.err.println("数据库连接失败！");
            return list;
        }

        String sql = "SELECT id, typeName, remark FROM drinktype ORDER BY id ASC";
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                DrinkType type = new DrinkType();
                type.setId(rs.getInt("id"));
                type.setTypeName(rs.getString("typeName"));

                // 查询该分类下的饮料列表
                String drinks = getDrinksByCategory(conn, type.getTypeName());
                type.setDrinks(drinks);

                // 保留原始的 remark 字段
                type.setRemark(rs.getString("remark"));

                list.add(type);
            }
        } catch (SQLException e) {
            System.err.println("查询所有分类失败：" + e.getMessage());
            throw e;
        } finally {
            try {
                if (!conn.isClosed()) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    // 根据名称查询（带饮料列表）
    public List<DrinkType> searchByName(String name) throws SQLException {
        List<DrinkType> list = new ArrayList<>();
        Connection conn = BaseDB.getConn();
        if (conn == null) {
            System.err.println("数据库连接失败！");
            return list;
        }

        String sql = "SELECT id, typeName, remark FROM drinktype WHERE typeName LIKE ? ORDER BY id ASC";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + name + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    DrinkType type = new DrinkType();
                    type.setId(rs.getInt("id"));
                    type.setTypeName(rs.getString("typeName"));

                    // 查询该分类下的饮料列表
                    String drinks = getDrinksByCategory(conn, type.getTypeName());
                    type.setDrinks(drinks);

                    // 保留原始的 remark 字段
                    type.setRemark(rs.getString("remark"));

                    list.add(type);
                }
            }
        } catch (SQLException e) {
            System.err.println("按名称查询分类失败：" + e.getMessage());
            throw e;
        } finally {
            try {
                if (!conn.isClosed()) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    // 查询某分类下的所有饮料名称
    private String getDrinksByCategory(Connection conn, String categoryName) throws SQLException {
        String sql = "SELECT name FROM drink WHERE category = ?";
        StringBuilder drinks = new StringBuilder();

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, categoryName);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    if (drinks.length() > 0) {
                        drinks.append("、");
                    }
                    drinks.append(rs.getString("name"));
                }
            }
        }

        return drinks.length() > 0 ? drinks.toString() : "暂无饮料";
    }

    // 添加分类
    public boolean add(DrinkType type) throws SQLException {
        Connection conn = BaseDB.getConn();
        if (conn == null) {
            System.err.println("数据库连接失败！");
            return false;
        }

        String sql = "INSERT INTO drinktype(typeName, remark) VALUES(?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, type.getTypeName());
            pst.setString(2, type.getRemark());

            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("添加分类失败：" + e.getMessage());
            throw e;
        } finally {
            try {
                if (!conn.isClosed()) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 修改分类
    public boolean update(DrinkType type) throws SQLException {
        Connection conn = BaseDB.getConn();
        if (conn == null) {
            System.err.println("数据库连接失败！");
            return false;
        }

        String sql = "UPDATE drinktype SET typeName = ?, remark = ? WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, type.getTypeName());
            pst.setString(2, type.getRemark());
            pst.setInt(3, type.getId());

            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("修改分类失败：" + e.getMessage());
            throw e;
        } finally {
            try {
                if (!conn.isClosed()) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 删除分类
    public boolean delete(int id) throws SQLException {
        Connection conn = BaseDB.getConn();
        if (conn == null) {
            System.err.println("数据库连接失败！");
            return false;
        }

        String sql = "DELETE FROM drinktype WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);

            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("删除分类失败：" + e.getMessage());
            throw e;
        } finally {
            try {
                if (!conn.isClosed()) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
