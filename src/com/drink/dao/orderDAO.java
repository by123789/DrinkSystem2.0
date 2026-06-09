package com.drink.dao;

import com.drink.model.Order;
import com.drink.model.OrdersPara;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class orderDAO {

    public boolean add(Order o) {
        String sql = "INSERT INTO orders(username, drink_id, drink_name, price, quantity, total, consignee, phone, address, category, create_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

        try (Connection conn = BaseDB.getConn();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, o.getUsername());
            pst.setString(2, o.getDrinkId());
            pst.setString(3, o.getDrinkName());
            pst.setDouble(4, o.getPrice());
            pst.setInt(5, o.getQuantity());
            pst.setDouble(6, o.getTotal());
            pst.setString(7, o.getConsignee());
            pst.setString(8, o.getPhone());
            pst.setString(9, o.getAddress());
            pst.setString(10, o.getCategory());

            int rows = pst.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> queryOrders(OrdersPara para, int page, int pageSize) {
        List<Order> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT o.*, d.category FROM orders o " +
                        "LEFT JOIN drink d ON o.drink_id = d.id " +
                        "WHERE 1=1 "
        );

        if (!para.getOid().isEmpty())
            sql.append(" AND o.id LIKE '%").append(para.getOid()).append("%'");
        if (!para.getUname().isEmpty())
            sql.append(" AND o.username LIKE '%").append(para.getUname()).append("%'");
        if (!para.getDname().isEmpty())
            sql.append(" AND o.drink_name LIKE '%").append(para.getDname()).append("%'");
        if (!para.getReceiver().isEmpty())
            sql.append(" AND o.consignee LIKE '%").append(para.getReceiver()).append("%'");
        if (!para.getPhone().isEmpty())
            sql.append(" AND o.phone LIKE '%").append(para.getPhone()).append("%'");
        if (!para.getStart().isEmpty())
            sql.append(" AND o.create_time >= '").append(para.getStart()).append("'");
        if (!para.getEnd().isEmpty())
            sql.append(" AND o.create_time <= '").append(para.getEnd()).append(" 23:59:59'");

        if (!para.getTname().isEmpty()) {
            sql.append(" AND d.category = '").append(para.getTname()).append("'");
        }

        int offset = (page - 1) * pageSize;
        sql.append(" ORDER BY o.id DESC LIMIT ?, ?");

        try (Connection conn = BaseDB.getConn();
             PreparedStatement pst = conn.prepareStatement(sql.toString())) {

            pst.setInt(1, offset);
            pst.setInt(2, pageSize);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setUsername(rs.getString("username"));
                o.setDrinkName(rs.getString("drink_name"));
                o.setPrice(rs.getDouble("price"));
                o.setQuantity(rs.getInt("quantity"));
                o.setTotal(rs.getDouble("total"));
                o.setConsignee(rs.getString("consignee"));
                o.setPhone(rs.getString("phone"));
                o.setAddress(rs.getString("address"));
                o.setCreateTime(rs.getString("create_time"));
                o.setCategory(rs.getString("category"));
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countOrders(OrdersPara para) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM orders o " +
                        "LEFT JOIN drink d ON o.drink_id = d.id " +
                        "WHERE 1=1 "
        );

        if (!para.getOid().isEmpty())
            sql.append(" AND o.id LIKE '%").append(para.getOid()).append("%'");
        if (!para.getUname().isEmpty())
            sql.append(" AND o.username LIKE '%").append(para.getUname()).append("%'");
        if (!para.getDname().isEmpty())
            sql.append(" AND o.drink_name LIKE '%").append(para.getDname()).append("%'");
        if (!para.getReceiver().isEmpty())
            sql.append(" AND o.consignee LIKE '%").append(para.getReceiver()).append("%'");
        if (!para.getPhone().isEmpty())
            sql.append(" AND o.phone LIKE '%").append(para.getPhone()).append("%'");
        if (!para.getStart().isEmpty())
            sql.append(" AND o.create_time >= '").append(para.getStart()).append("'");
        if (!para.getEnd().isEmpty())
            sql.append(" AND o.create_time <= '").append(para.getEnd()).append(" 23:59:59'");

        if (!para.getTname().isEmpty()) {
            sql.append(" AND d.category = '").append(para.getTname()).append("'");
        }

        try (Connection conn = BaseDB.getConn();
             PreparedStatement pst = conn.prepareStatement(sql.toString())) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}