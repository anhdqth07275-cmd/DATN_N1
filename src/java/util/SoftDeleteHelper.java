package util;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Helper dùng chung cho cơ chế xóa mềm ở mọi module nghiệp vụ:
 *   - Xóa mềm (soft delete)  : chỉ ẩn bản ghi khỏi hệ thống, có thể khôi phục
 *   - Khôi phục (restore)    : bỏ ẩn, cho hoạt động lại bình thường
 *
 * Vì mỗi bảng dùng tên cột trạng thái khác nhau (Customer dùng "status",
 * các bảng còn lại dùng "is_active"), phương thức setActive nhận tên
 * bảng/cột làm tham số. Những tham số này LUÔN do chính code trong dự án
 * truyền vào dạng hằng số (không lấy trực tiếp từ request của người dùng)
 * nên không phát sinh rủi ro SQL Injection dù dùng nối chuỗi cho tên
 * bảng/cột.
 */
public class SoftDeleteHelper {

    // ==========================
    // Đổi trạng thái hoạt động (true = khôi phục, false = xóa mềm/ẩn)
    // ==========================
    public static boolean setActive(String table, String idColumn,
            String statusColumn, int id, boolean active) {

        String sql = "UPDATE " + table
                + " SET " + statusColumn + " = ?"
                + " WHERE " + idColumn + " = ?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setBoolean(1, active);
            ps.setInt(2, id);

            int row = ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

}
