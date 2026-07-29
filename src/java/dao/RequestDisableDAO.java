package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import model.RequestDisable;
import util.DBConnect;

public class RequestDisableDAO {

    // ==========================
    // Tạo yêu cầu mới (Nhân viên bấm "Xin quyền vô hiệu hóa").
    // Không tạo trùng nếu đã có 1 yêu cầu Pending cho đúng bản ghi đó.
    // ==========================
    public boolean create(String moduleCode, int entityId,
            String entityLabel, int requestedBy) {

        if (hasPending(moduleCode, entityId)) {
            return false;
        }

        String sql = "INSERT INTO RequestDisable "
                + "(module_code, entity_id, entity_label, requested_by) "
                + "VALUES (?,?,?,?)";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, moduleCode);
            ps.setInt(2, entityId);
            ps.setString(3, entityLabel);
            ps.setInt(4, requestedBy);

            int row = ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // Có yêu cầu đang chờ (Pending) cho đúng bản ghi này không
    // ==========================
    public boolean hasPending(String moduleCode, int entityId) {

        String sql = "SELECT 1 FROM RequestDisable "
                + "WHERE module_code=? AND entity_id=? AND review_status='Pending'";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, moduleCode);
            ps.setInt(2, entityId);

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
    // Toàn bộ entity_id đang có yêu cầu Pending trong 1 module - dùng để
    // đánh dấu (badge) hàng loạt trên trang danh sách chỉ với 1 lần query,
    // tránh phải query riêng cho từng dòng (N+1 query).
    // ==========================
    public HashSet<Integer> getPendingEntityIds(String moduleCode) {

        HashSet<Integer> set = new HashSet<>();

        String sql = "SELECT entity_id FROM RequestDisable "
                + "WHERE module_code=? AND review_status='Pending'";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, moduleCode);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                set.add(rs.getInt("entity_id"));
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return set;

    }

    // ==========================
    // Đánh dấu toàn bộ yêu cầu Pending của 1 bản ghi là Approved/Rejected.
    // - Approved: gọi ngay sau khi Admin/Giám đốc thực sự bấm "Vô hiệu hóa"
    // - Rejected: Admin/Giám đốc bấm "Bỏ qua yêu cầu" (không vô hiệu hóa)
    // ==========================
    public boolean resolve(String moduleCode, int entityId,
            int reviewerId, String newStatus) {

        String sql = "UPDATE RequestDisable "
                + "SET review_status=?, reviewed_by=?, reviewed_date=GETDATE() "
                + "WHERE module_code=? AND entity_id=? AND review_status='Pending'";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, newStatus);
            ps.setInt(2, reviewerId);
            ps.setString(3, moduleCode);
            ps.setInt(4, entityId);

            ps.executeUpdate();

            con.close();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // Toàn bộ yêu cầu đang chờ (mọi module) - hiển thị ở Hệ thống cho
    // Admin/Giám đốc xem tổng quan 1 chỗ.
    // ==========================
    public ArrayList<RequestDisable> getAllPending() {

        ArrayList<RequestDisable> list = new ArrayList<>();

        String sql = "SELECT rq.*, u.full_name AS requested_by_name "
                + "FROM RequestDisable rq "
                + "JOIN [User] u ON rq.requested_by = u.user_id "
                + "WHERE rq.review_status='Pending' "
                + "ORDER BY rq.requested_date DESC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                RequestDisable rq = new RequestDisable();

                rq.setRequestId(rs.getInt("request_id"));
                rq.setModuleCode(rs.getString("module_code"));
                rq.setEntityId(rs.getInt("entity_id"));
                rq.setEntityLabel(rs.getString("entity_label"));
                rq.setRequestedBy(rs.getInt("requested_by"));
                rq.setRequestedByName(rs.getString("requested_by_name"));
                rq.setRequestedDate(rs.getTimestamp("requested_date"));
                rq.setReviewStatus(rs.getString("review_status"));

                list.add(rq);

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

}
