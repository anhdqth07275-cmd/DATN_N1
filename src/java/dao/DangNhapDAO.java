package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.DangKy;
import util.DBConnect;

public class DangNhapDAO {

    public DangKy login(String username, String password) {

        String sql = "SELECT u.*, r.role_name "
              + "FROM [User] u "
              + "JOIN Role r ON u.role_id = r.role_id "
              + "WHERE username=? AND password=? AND status=1";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                DangKy user = new DangKy();

                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));

                // Lấy đúng vai trò đã được QTV cấu hình trong bảng User/Role,
                // KHÔNG hard-code ép quyền theo username/password. Việc gán
                // cứng "chỉ tài khoản QA/123 mới là Quản trị viên, còn lại ép
                // về Khách hàng" trước đây vừa sai nghiệp vụ (không tôn trọng
                // phân quyền QTV đã cấu hình) vừa là 1 backdoor bảo mật (tài
                // khoản admin hard-code sẵn trong source code).
                user.setRoleId(rs.getInt("role_id"));
                user.setRoleName(rs.getString("role_name"));

                con.close();

                return user;

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

}
