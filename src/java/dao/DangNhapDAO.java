package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.DangKy;
import model.DangNhap;
import util.DBConnect;

public class DangNhapDAO {

    // Chỉ duy nhất tài khoản này được cấp quyền Quản trị viên.
    // Mọi tài khoản khác, dù role_id trong bảng [User] là gì, khi đăng nhập
    // đều bị ép về quyền thấp nhất (Khách hàng) để đảm bảo an toàn nghiệp vụ.
    private static final String ADMIN_USERNAME = "QA";
    private static final String ADMIN_PASSWORD = "123";

    private static final int ADMIN_ROLE_ID = 4;
    private static final String ADMIN_ROLE_NAME = "Quản trị viên";

    private static final int CUSTOMER_ROLE_ID = 1;
    private static final String CUSTOMER_ROLE_NAME = "Khách hàng";

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

                // Ép vai trò theo đúng quy tắc nghiệp vụ:
                // - Tài khoản QA/123 => Quản trị viên (toàn quyền)
                // - Tất cả tài khoản còn lại => Khách hàng (quyền thấp nhất)
                if (ADMIN_USERNAME.equalsIgnoreCase(username)
                        && ADMIN_PASSWORD.equals(password)) {

                    user.setRoleId(ADMIN_ROLE_ID);
                    user.setRoleName(ADMIN_ROLE_NAME);

                } else {

                    user.setRoleId(CUSTOMER_ROLE_ID);
                    user.setRoleName(CUSTOMER_ROLE_NAME);

                }

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