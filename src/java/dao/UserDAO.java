package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.DangKy;
import util.DBConnect;

public class UserDAO {

    // ==========================
    // Danh sách toàn bộ người dùng (kèm tên vai trò)
    // ==========================
    public ArrayList<DangKy> getAll() {

        ArrayList<DangKy> list = new ArrayList<>();

        String sql = "SELECT u.*, r.role_name "
                + "FROM [User] u "
                + "JOIN Role r ON u.role_id = r.role_id "
                + "ORDER BY u.user_id DESC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(mapRow(rs));

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

    // ==========================
    // Tìm kiếm theo tên đăng nhập / họ tên / email
    // ==========================
    public ArrayList<DangKy> search(String keyword) {

        ArrayList<DangKy> list = new ArrayList<>();

        String sql = "SELECT u.*, r.role_name "
                + "FROM [User] u "
                + "JOIN Role r ON u.role_id = r.role_id "
                + "WHERE u.username LIKE ? OR u.full_name LIKE ? OR u.email LIKE ? "
                + "ORDER BY u.user_id DESC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(mapRow(rs));

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

    public DangKy getById(int id) {

        String sql = "SELECT u.*, r.role_name "
                + "FROM [User] u "
                + "JOIN Role r ON u.role_id = r.role_id "
                + "WHERE u.user_id = ?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                DangKy u = mapRow(rs);

                con.close();

                return u;

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    public boolean checkUsername(String username) {

        String sql = "SELECT * FROM [User] WHERE username = ?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            boolean exists = rs.next();

            con.close();

            return exists;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // Thêm người dùng (do QTV tạo trực tiếp, đã chọn sẵn vai trò + trạng thái)
    // ==========================
    public boolean insert(DangKy u) {

        String sql = "INSERT INTO [User]"
                + "(username,password,full_name,email,phone,role_id,status)"
                + " VALUES(?,?,?,?,?,?,?)";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getFullName());
            ps.setString(4, u.getEmail());
            ps.setString(5, u.getPhone());
            ps.setInt(6, u.getRoleId());
            ps.setBoolean(7, u.isStatus());

            int row = ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // Cập nhật thông tin người dùng: họ tên, email, sđt, vai trò, trạng thái
    // (không cập nhật username/password ở đây)
    // ==========================
    public boolean update(DangKy u) {

        String sql = "UPDATE [User] SET full_name=?, email=?, phone=?, "
                + "role_id=?, status=? WHERE user_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPhone());
            ps.setInt(4, u.getRoleId());
            ps.setBoolean(5, u.isStatus());
            ps.setInt(6, u.getUserId());

            int row = ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // Khóa / mở khóa tài khoản (không xóa cứng để giữ vết dữ liệu liên quan)
    // ==========================
    public boolean setStatus(int userId, boolean status) {

        String sql = "UPDATE [User] SET status=? WHERE user_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setBoolean(1, status);
            ps.setInt(2, userId);

            int row = ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // Đặt lại mật khẩu cho người dùng (QTV thực hiện khi NV quên mật khẩu)
    // ==========================
    public boolean resetPassword(int userId, String newPassword) {

        String sql = "UPDATE [User] SET password=? WHERE user_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            int row = ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    private DangKy mapRow(ResultSet rs) throws Exception {

        DangKy u = new DangKy();

        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        u.setRoleId(rs.getInt("role_id"));
        u.setRoleName(rs.getString("role_name"));
        u.setStatus(rs.getBoolean("status"));

        return u;

    }

}
