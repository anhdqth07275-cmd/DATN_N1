package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.DangKy;
import util.DBConnect;

public class QuenMatKhauDAO {

    // Kiểm tra email đã đăng ký với hệ thống hay chưa (tài khoản còn hoạt động).
    // Trả về thông tin user tương ứng nếu tồn tại, ngược lại trả về null.
    public DangKy getUserByEmail(String email) {

        String sql = "SELECT * FROM [User] WHERE email = ? AND status = 1";

        try (Connection con = DBConnect.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    DangKy user = new DangKy();

                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setRoleId(rs.getInt("role_id"));

                    return user;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Cập nhật mật khẩu mới cho tài khoản theo email
    public boolean updatePasswordByEmail(String email, String newPassword) {

        String sql = "UPDATE [User] SET password = ? WHERE email = ?";

        try (Connection con = DBConnect.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);

            int row = ps.executeUpdate();

            return row > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
