package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import model.ActivityLog;
import model.ActivitySummary;
import util.DBConnect;

public class ActivityLogDAO {

    // ==========================
    // Ghi 1 dòng nhật ký hoạt động
    // ==========================
    public boolean insert(ActivityLog log) {

        String sql = "INSERT INTO ActivityLog"
                + "(user_id, username, full_name, role_name, action_type, "
                + "module, description, amount, created_date) "
                + "VALUES(?,?,?,?,?,?,?,?,GETDATE())";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, log.getUserId());
            ps.setString(2, log.getUsername());
            ps.setString(3, log.getFullName());
            ps.setString(4, log.getRoleName());
            ps.setString(5, log.getActionType());
            ps.setString(6, log.getModule());
            ps.setString(7, log.getDescription());

            if (log.getAmount() != null) {
                ps.setDouble(8, log.getAmount());
            } else {
                ps.setNull(8, Types.DECIMAL);
            }

            int row = ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // Danh sách nhật ký trong khoảng [start, end], mới nhất trước
    // ==========================
    public ArrayList<ActivityLog> getLogs(Timestamp start, Timestamp end) {

        ArrayList<ActivityLog> list = new ArrayList<>();

        String sql = "SELECT * FROM ActivityLog "
                + "WHERE created_date BETWEEN ? AND ? "
                + "ORDER BY created_date DESC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setTimestamp(1, start);
            ps.setTimestamp(2, end);

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
    // Số liệu tổng hợp trong khoảng [start, end]
    // ==========================
    public ActivitySummary getSummary(Timestamp start, Timestamp end) {

        ActivitySummary s = new ActivitySummary();

        String sql = "SELECT action_type, COUNT(*) AS cnt, "
                + "ISNULL(SUM(amount),0) AS total "
                + "FROM ActivityLog "
                + "WHERE created_date BETWEEN ? AND ? "
                + "GROUP BY action_type";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setTimestamp(1, start);
            ps.setTimestamp(2, end);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String action = rs.getString("action_type");
                int cnt = rs.getInt("cnt");
                double total = rs.getDouble("total");

                switch (action) {

                    case "THU":
                        s.setReceiptCount(cnt);
                        s.setTotalReceipt(total);
                        break;

                    case "CHI":
                        s.setExpenseCount(cnt);
                        s.setTotalExpense(total);
                        break;

                    case "THEM":
                        s.setAddCount(cnt);
                        break;

                    case "SUA":
                        s.setEditCount(cnt);
                        break;

                    case "XOA":
                        s.setDeleteCount(cnt);
                        break;

                    case "DANG_NHAP":
                        s.setLoginCount(cnt);
                        break;

                    default:
                        break;

                }

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return s;

    }

    private ActivityLog mapRow(ResultSet rs) throws Exception {

        ActivityLog log = new ActivityLog();

        log.setLogId(rs.getInt("log_id"));
        log.setUserId(rs.getInt("user_id"));
        log.setUsername(rs.getString("username"));
        log.setFullName(rs.getString("full_name"));
        log.setRoleName(rs.getString("role_name"));
        log.setActionType(rs.getString("action_type"));
        log.setModule(rs.getString("module"));
        log.setDescription(rs.getString("description"));

        double amt = rs.getDouble("amount");

        log.setAmount(rs.wasNull() ? null : amt);

        log.setCreatedDate(rs.getTimestamp("created_date"));

        return log;

    }

}
