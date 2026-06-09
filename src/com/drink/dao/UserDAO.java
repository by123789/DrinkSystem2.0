package com.drink.dao;

import com.drink.model.User;
import java.sql.*;

public class UserDAO {

    public User login(String name, String pwd) {
        String sql = "SELECT * FROM user WHERE username=? AND password=?";
        try (Connection conn = BaseDB.getConn();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setString(2, pwd);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setAge(rs.getInt("age"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setGender(rs.getString("gender"));
                user.setHobby(rs.getString("hobby"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(User user) {
        if (exists(user.getUsername())) return false;
        String sql = "INSERT INTO user(username, password, age, phone, address, gender, hobby) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = BaseDB.getConn();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPassword());
            pst.setInt(3, user.getAge());
            pst.setString(4, user.getPhone());
            pst.setString(5, user.getAddress());
            pst.setString(6, user.getGender());
            pst.setString(7, user.getHobby());
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pst.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    System.out.println("新用户注册成功，ID: " + userId);
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean exists(String name) {
        String sql = "SELECT username FROM user WHERE username=?";
        try (Connection conn = BaseDB.getConn();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, name);
            return pst.executeQuery().next();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean resetPwd(String name, String pwd) {
        String sql = "UPDATE user SET password=? WHERE username=?";
        try (Connection conn = BaseDB.getConn();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, pwd);
            pst.setString(2, name);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE user SET password=?, age=?, phone=?, address=?, gender=?, hobby=? WHERE username=?";
        try (Connection conn = BaseDB.getConn();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, user.getPassword());
            pst.setInt(2, user.getAge());
            pst.setString(3, user.getPhone());
            pst.setString(4, user.getAddress());
            pst.setString(5, user.getGender());
            pst.setString(6, user.getHobby());
            pst.setString(7, user.getUsername());
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String username) {
        if ("admin".equals(username)) {
            return false;
        }
        String sql = "DELETE FROM user WHERE username=?";
        try (Connection conn = BaseDB.getConn();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
