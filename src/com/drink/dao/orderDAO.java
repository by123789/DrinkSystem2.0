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
        String sql = "INSERT INTO orders(username, drink_id, drink_name, price, quantity, total, consignee, phone, address, category, temperature, create_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

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
            pst.setString(11, o.getTemperature());

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
                "SELECT * FROM orders WHERE 1=1 "
        );

        if (!para.getOid().isEmpty())
            sql.append(" AND id LIKE '%").append(para.getOid()).append("%'");
        if (!para.getUname().isEmpty())
            sql.append(" AND username LIKE '%").append(para.getUname()).append("%'");
        if (!para.getDname().isEmpty())
            sql.append(" AND drink_name LIKE '%").append(para.getDname()).append("%'");
        if (!para.getReceiver().isEmpty())
            sql.append(" AND consignee LIKE '%").append(para.getReceiver()).append("%'");
        if (!para.getPhone().isEmpty())
            sql.append(" AND phone LIKE '%").append(para.getPhone()).append("%'");
        if (!para.getStart().isEmpty())
            sql.append(" AND create_time >= '").append(para.getStart()).append("'");
        if (!para.getEnd().isEmpty())
            sql.append(" AND create_time <= '").append(para.getEnd()).append(" 23:59:59'");

        if (!para.getTname().isEmpty()) {
            sql.append(" AND category = '").append(para.getTname()).append("'");
        }

        int offset = (page - 1) * pageSize;
        sql.append(" ORDER BY id DESC LIMIT ?, ?");

        System.out.println("=== DEBUG SQL ===");
        System.out.println(sql.toString());
        System.out.println("Parameters: offset=" + offset + ", pageSize=" + pageSize);
        System.out.println("=================");

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
                o.setTemperature(rs.getString("temperature"));
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countOrders(OrdersPara para) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM orders WHERE 1=1 "
        );

        if (!para.getOid().isEmpty())
            sql.append(" AND id LIKE '%").append(para.getOid()).append("%'");
        if (!para.getUname().isEmpty())
            sql.append(" AND username LIKE '%").append(para.getUname()).append("%'");
        if (!para.getDname().isEmpty())
            sql.append(" AND drink_name LIKE '%").append(para.getDname()).append("%'");
        if (!para.getReceiver().isEmpty())
            sql.append(" AND consignee LIKE '%").append(para.getReceiver()).append("%'");
        if (!para.getPhone().isEmpty())
            sql.append(" AND phone LIKE '%").append(para.getPhone()).append("%'");
        if (!para.getStart().isEmpty())
            sql.append(" AND create_time >= '").append(para.getStart()).append("'");
        if (!para.getEnd().isEmpty())
            sql.append(" AND create_time <= '").append(para.getEnd()).append(" 23:59:59'");

        if (!para.getTname().isEmpty()) {
            sql.append(" AND category = '").append(para.getTname()).append("'");
        }

        System.out.println("=== DEBUG COUNT SQL ===");
        System.out.println(sql.toString());
        System.out.println("=======================");

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
