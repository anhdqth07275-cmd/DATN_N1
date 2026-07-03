package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.DangNhap;
import util.DBConnect;

public class DangNhapDAO {

    // Kiểm tra đăng nhập
    public boolean login(DangNhap dn) {

        String sql = "SELECT * FROM [User] WHERE username = ? AND password = ?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, dn.getUsername());
            ps.setString(2, dn.getPassword());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                con.close();
                return true;
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}