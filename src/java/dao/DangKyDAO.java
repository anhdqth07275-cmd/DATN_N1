package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.DangKy;
import util.DBConnect;

public class DangKyDAO {

    // Kiểm tra username đã tồn tại hay chưa
    public boolean checkUsername(String username) {

        String sql = "SELECT * FROM [User] WHERE username = ?";

        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Đăng ký tài khoản
    public boolean register(DangKy dk) {

        String sql = "INSERT INTO [User]"
                + "(username,password,full_name,email,phone,role_id)"
                + " VALUES(?,?,?,?,?,?)";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, dk.getUsername());
            ps.setString(2, dk.getPassword());
            ps.setString(3, dk.getFullName());
            ps.setString(4, dk.getEmail());
            ps.setString(5, dk.getPhone());
            ps.setInt(6, dk.getRoleId());

            int row = ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;
    }

}