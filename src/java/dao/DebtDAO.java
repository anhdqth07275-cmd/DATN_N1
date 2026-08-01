package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Debt;
import util.DBConnect;

public class DebtDAO {

    // ==========================
    // Danh sách công nợ
    // Chỉ hiển thị công nợ CÒN NỢ (remaining_amount > 0).
    // Hóa đơn đã thanh toán hết thì công nợ phải biến mất khỏi
    // danh sách đang theo dõi (vẫn còn lưu trong DB để đối chiếu).
    //
    // userId: null = xem toàn bộ (dùng cho người có quyền
    // CONGNO_XEMTATCA); khác null = chỉ lấy công nợ của các hóa
    // đơn do CHÍNH nhân viên đó lập (Invoice.user_id), để "nhắc
    // đúng tài khoản" - mỗi nhân viên chỉ thấy phần việc của mình.
    // ==========================
    public ArrayList<Debt> getAll(Integer userId) {

        ArrayList<Debt> list = new ArrayList<>();

        String sql =
                "SELECT d.*, c.customer_name, "
                + "ISNULL(r.paid,0) AS paid_amount "
                + "FROM Debt d "
                + "JOIN Customer c "
                + "ON d.customer_id = c.customer_id "
                + "JOIN Invoice i "
                + "ON d.invoice_id = i.invoice_id "
                + "LEFT JOIN ("
                + "     SELECT invoice_id, SUM(amount) AS paid "
                + "     FROM Receipt "
                + "     WHERE is_active = 1 "
                + "     GROUP BY invoice_id"
                + ") r ON r.invoice_id = d.invoice_id "
                + "WHERE d.remaining_amount > 0 "
                + (userId != null ? "AND i.user_id = ? " : "")
                + "ORDER BY d.debt_id DESC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            if (userId != null) {
                ps.setInt(1, userId);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Debt d = new Debt();

                d.setDebtId(rs.getInt("debt_id"));
                d.setCustomerId(rs.getInt("customer_id"));
                d.setInvoiceId(rs.getInt("invoice_id"));
                d.setCustomerName(rs.getString("customer_name"));
                d.setRemainingAmount(rs.getDouble("remaining_amount"));
                d.setPaidAmount(rs.getDouble("paid_amount"));
                d.setDueDate(rs.getDate("due_date"));
                d.setStatus(rs.getString("status"));

                list.add(d);

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

    // ==========================
    // Lấy công nợ theo ID
    // ==========================
    public Debt getById(int debtId) {

        String sql =
                "SELECT d.*, c.customer_name, "
                + "ISNULL(r.paid,0) AS paid_amount "
                + "FROM Debt d "
                + "JOIN Customer c "
                + "ON d.customer_id=c.customer_id "
                + "LEFT JOIN ("
                + "     SELECT invoice_id, SUM(amount) AS paid "
                + "     FROM Receipt "
                + "     WHERE is_active = 1 "
                + "     GROUP BY invoice_id"
                + ") r ON r.invoice_id = d.invoice_id "
                + "WHERE d.debt_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, debtId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Debt d = new Debt();

                d.setDebtId(rs.getInt("debt_id"));
                d.setCustomerId(rs.getInt("customer_id"));
                d.setInvoiceId(rs.getInt("invoice_id"));
                d.setCustomerName(rs.getString("customer_name"));
                d.setRemainingAmount(rs.getDouble("remaining_amount"));
                d.setPaidAmount(rs.getDouble("paid_amount"));
                d.setDueDate(rs.getDate("due_date"));
                d.setStatus(rs.getString("status"));

                rs.close();
                ps.close();
                con.close();

                return d;

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    // ==========================
    // Kiểm tra đã có công nợ chưa
    // ==========================
    public boolean exists(int invoiceId) {

        String sql =
                "SELECT debt_id FROM Debt WHERE invoice_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, invoiceId);

            ResultSet rs = ps.executeQuery();

            boolean check = rs.next();

            rs.close();
            ps.close();
            con.close();

            return check;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // Thêm công nợ
    // ==========================
    public boolean insert(Debt d) {

        String sql =
                "INSERT INTO Debt("
                + "customer_id,"
                + "invoice_id,"
                + "remaining_amount,"
                + "due_date,"
                + "status"
                + ") VALUES(?,?,?,?,?)";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, d.getCustomerId());

            ps.setInt(2, d.getInvoiceId());

            ps.setDouble(3, d.getRemainingAmount());

            ps.setDate(4, new java.sql.Date(d.getDueDate().getTime()));

            ps.setString(5, d.getStatus());

            int row = ps.executeUpdate();

            ps.close();
            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }
    
    // ==========================
// Tạo công nợ từ hóa đơn
// ==========================
// Hạn thanh toán của công nợ LẤY THEO hạn thanh toán do người
// dùng nhập khi lập hóa đơn (Invoice.due_date) - không còn cộng
// cứng 30 ngày như trước đây.
public boolean createFromInvoice(int invoiceId){

    String sql =
            "INSERT INTO Debt("
            + "customer_id,"
            + "invoice_id,"
            + "remaining_amount,"
            + "due_date,"
            + "status"
            + ") "
            + "SELECT "
            + "customer_id,"
            + "invoice_id,"
            + "total_amount,"
            + "due_date,"
            + "'Chưa thanh toán' "
            + "FROM Invoice "
            + "WHERE invoice_id=? "
            + "AND NOT EXISTS("
            + "SELECT 1 FROM Debt "
            + "WHERE invoice_id=?"
            + ")";

    try{

        Connection con = DBConnect.getConnection();

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, invoiceId);

        ps.setInt(2, invoiceId);

        int row = ps.executeUpdate();

        ps.close();

        con.close();

        return row > 0;

    }catch(Exception e){

        e.printStackTrace();

    }

    return false;

}
// ==========================
// Đồng bộ công nợ + trạng thái hóa đơn
// ==========================
// Đây là nơi DUY NHẤT quyết định số tiền còn nợ và trạng thái
// thanh toán, dựa trên công thức:
//
//      còn nợ = tổng tiền hóa đơn - tổng đã thu (Receipt)
//
// Phải gọi lại hàm này mỗi khi:
//  - tổng tiền hóa đơn thay đổi (thêm/sửa/xóa chi tiết hóa đơn)
//  - có phiếu thu được thêm/sửa/xóa
// để hóa đơn, công nợ và phiếu thu luôn khớp nhau.
public void updateFromInvoice(int invoiceId) {

    // remaining_amount không bao giờ âm, không bao giờ NULL
    String sqlDebt =
            "UPDATE Debt "
            + "SET remaining_amount = CASE "
            + "        WHEN i.total_amount - ISNULL(r.paid,0) < 0 THEN 0 "
            + "        ELSE i.total_amount - ISNULL(r.paid,0) "
            + "     END, "
            + "     status = CASE "
            + "        WHEN i.total_amount - ISNULL(r.paid,0) <= 0 "
            + "             THEN N'Đã thanh toán' "
            + "        WHEN d.due_date < CAST(GETDATE() AS DATE) "
            + "             THEN N'Quá hạn' "
            + "        ELSE N'Chưa thanh toán' "
            + "     END "
            + "FROM Debt d "
            + "JOIN Invoice i ON d.invoice_id = i.invoice_id "
            + "LEFT JOIN ("
            + "     SELECT invoice_id, SUM(amount) AS paid "
            + "     FROM Receipt "
            + "     WHERE is_active = 1 "
            + "     GROUP BY invoice_id"
            + ") r ON r.invoice_id = i.invoice_id "
            + "WHERE d.invoice_id = ?";

    // Hóa đơn tự động chuyển "Đã thanh toán"/"Chưa thanh toán" dựa
    // trên số tiền đã thu, không cho chỉnh tay để tránh mâu thuẫn
    // với công nợ/phiếu thu.
    String sqlInvoice =
            "UPDATE Invoice "
            + "SET status = CASE "
            + "        WHEN total_amount > 0 AND total_amount <= ISNULL("
            + "             (SELECT SUM(amount) FROM Receipt "
            + "              WHERE invoice_id = Invoice.invoice_id "
            + "              AND is_active = 1),0) "
            + "             THEN N'Đã thanh toán' "
            + "        ELSE N'Chưa thanh toán' "
            + "     END "
            + "WHERE invoice_id = ?";

    Connection con = null;

    try {

        con = DBConnect.getConnection();
        con.setAutoCommit(false);

        PreparedStatement psDebt = con.prepareStatement(sqlDebt);
        psDebt.setInt(1, invoiceId);
        psDebt.executeUpdate();
        psDebt.close();

        PreparedStatement psInvoice = con.prepareStatement(sqlInvoice);
        psInvoice.setInt(1, invoiceId);
        psInvoice.executeUpdate();
        psInvoice.close();

        con.commit();

    } catch (Exception e) {

        e.printStackTrace();

        if (con != null) {
            try {
                con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    } finally {

        if (con != null) {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}

// ==========================
// Tổng tiền đã thu của 1 hóa đơn (tùy chọn loại trừ 1 phiếu thu,
// dùng khi sửa phiếu thu để tính lại hạn mức cho đúng)
// ==========================
public double getTotalPaid(int invoiceId, int excludeReceiptId) {

    String sql =
            "SELECT ISNULL(SUM(amount),0) AS paid "
            + "FROM Receipt "
            + "WHERE invoice_id=? AND receipt_id<>? AND is_active=1";

    try {

        Connection con = DBConnect.getConnection();

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, invoiceId);
        ps.setInt(2, excludeReceiptId);

        ResultSet rs = ps.executeQuery();

        double paid = 0;

        if (rs.next()) {
            paid = rs.getDouble("paid");
        }

        rs.close();
        ps.close();
        con.close();

        return paid;

    } catch (Exception e) {

        e.printStackTrace();

    }

    return 0;

}

// ==========================
// Số tiền còn nợ hiện tại theo hóa đơn
// (dùng để chặn xóa hóa đơn còn nợ/đã có thu tiền, và để
// kiểm tra hạn mức khi lập phiếu thu)
// ==========================
public double getRemainingAmount(int invoiceId) {

    String sql =
            "SELECT remaining_amount FROM Debt WHERE invoice_id=?";

    try {

        Connection con = DBConnect.getConnection();

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, invoiceId);

        ResultSet rs = ps.executeQuery();

        double remaining = 0;

        if (rs.next()) {
            remaining = rs.getDouble("remaining_amount");
        }

        rs.close();
        ps.close();
        con.close();

        return remaining;

    } catch (Exception e) {

        e.printStackTrace();

    }

    return 0;

}
    // ==========================
    // Gia hạn nợ
    // ==========================
    public boolean extendDebt(int debtId,
            java.sql.Date dueDate) {

        String sql =
                "UPDATE Debt "
                + "SET due_date=? "
                + "WHERE debt_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDate(1, dueDate);

            ps.setInt(2, debtId);

            int row = ps.executeUpdate();

            ps.close();
            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // Tìm kiếm
    // ==========================
    public ArrayList<Debt> search(String keyword, Integer userId) {

        ArrayList<Debt> list = new ArrayList<>();

        String sql =
                "SELECT d.*, c.customer_name, "
                + "ISNULL(r.paid,0) AS paid_amount "
                + "FROM Debt d "
                + "JOIN Customer c "
                + "ON d.customer_id=c.customer_id "
                + "JOIN Invoice i "
                + "ON d.invoice_id = i.invoice_id "
                + "LEFT JOIN ("
                + "     SELECT invoice_id, SUM(amount) AS paid "
                + "     FROM Receipt "
                + "     WHERE is_active = 1 "
                + "     GROUP BY invoice_id"
                + ") r ON r.invoice_id = d.invoice_id "
                + "WHERE d.remaining_amount > 0 "
                + "AND (c.customer_name LIKE ? "
                + "OR CAST(d.invoice_id AS NVARCHAR) LIKE ?) "
                + (userId != null ? "AND i.user_id = ? " : "");

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");

            ps.setString(2, "%" + keyword + "%");

            if (userId != null) {
                ps.setInt(3, userId);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Debt d = new Debt();

                d.setDebtId(rs.getInt("debt_id"));
                d.setCustomerId(rs.getInt("customer_id"));
                d.setInvoiceId(rs.getInt("invoice_id"));
                d.setCustomerName(rs.getString("customer_name"));
                d.setRemainingAmount(rs.getDouble("remaining_amount"));
                d.setPaidAmount(rs.getDouble("paid_amount"));
                d.setDueDate(rs.getDate("due_date"));
                d.setStatus(rs.getString("status"));

                list.add(d);

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }
    
    // ==========================
    // CẢNH BÁO CÔNG NỢ (chuông thông báo + Trang chủ)
    //
    // userId: null = xem toàn bộ (quyền CONGNO_XEMTATCA); khác null
    // = chỉ lấy công nợ của hóa đơn do CHÍNH nhân viên đó lập, để
    // "nhắc đúng tài khoản" - không trộn lẫn việc của người khác.
    // ==========================

    // Sắp đến hạn: còn nợ, hạn thanh toán trong vòng "days" ngày tới
    // (kể cả hôm nay), nhưng CHƯA quá hạn.
    public ArrayList<Debt> getDueSoon(int days, Integer userId, int limit) {

        ArrayList<Debt> list = new ArrayList<>();

        String sql =
                "SELECT TOP (?) "
                + "d.debt_id, d.invoice_id, d.remaining_amount, "
                + "d.due_date, d.status, c.customer_name "
                + "FROM Debt d "
                + "JOIN Customer c ON d.customer_id = c.customer_id "
                + "JOIN Invoice i ON d.invoice_id = i.invoice_id "
                + "WHERE d.remaining_amount > 0 "
                + "AND d.due_date >= CAST(GETDATE() AS DATE) "
                + "AND d.due_date <= DATEADD(DAY, ?, CAST(GETDATE() AS DATE)) "
                + (userId != null ? "AND i.user_id = ? " : "")
                + "ORDER BY d.due_date ASC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            int idx = 1;
            ps.setInt(idx++, limit);
            ps.setInt(idx++, days);

            if (userId != null) {
                ps.setInt(idx++, userId);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Debt d = new Debt();

                d.setDebtId(rs.getInt("debt_id"));
                d.setInvoiceId(rs.getInt("invoice_id"));
                d.setCustomerName(rs.getString("customer_name"));
                d.setRemainingAmount(rs.getDouble("remaining_amount"));
                d.setDueDate(rs.getTimestamp("due_date"));
                d.setStatus(rs.getString("status"));

                list.add(d);

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

    // Đã quá hạn: còn nợ, hạn thanh toán đã qua.
    public ArrayList<Debt> getOverdue(Integer userId, int limit) {

        ArrayList<Debt> list = new ArrayList<>();

        String sql =
                "SELECT TOP (?) "
                + "d.debt_id, d.invoice_id, d.remaining_amount, "
                + "d.due_date, d.status, c.customer_name "
                + "FROM Debt d "
                + "JOIN Customer c ON d.customer_id = c.customer_id "
                + "JOIN Invoice i ON d.invoice_id = i.invoice_id "
                + "WHERE d.remaining_amount > 0 "
                + "AND d.due_date < CAST(GETDATE() AS DATE) "
                + (userId != null ? "AND i.user_id = ? " : "")
                + "ORDER BY d.due_date ASC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            int idx = 1;
            ps.setInt(idx++, limit);

            if (userId != null) {
                ps.setInt(idx++, userId);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Debt d = new Debt();

                d.setDebtId(rs.getInt("debt_id"));
                d.setInvoiceId(rs.getInt("invoice_id"));
                d.setCustomerName(rs.getString("customer_name"));
                d.setRemainingAmount(rs.getDouble("remaining_amount"));
                d.setDueDate(rs.getTimestamp("due_date"));
                d.setStatus(rs.getString("status"));

                list.add(d);

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

    // Đếm số công nợ sắp đến hạn - dùng cho badge chuông thông báo
    // (không giới hạn TOP, khác với getDueSoon() dùng để hiển thị).
    public int countDueSoon(int days, Integer userId) {

        return countByCondition(
                "AND d.due_date >= CAST(GETDATE() AS DATE) "
                + "AND d.due_date <= DATEADD(DAY, " + days
                + ", CAST(GETDATE() AS DATE)) ",
                userId);

    }

    // Đếm số công nợ đã quá hạn - dùng cho badge chuông thông báo.
    public int countOverdue(Integer userId) {

        return countByCondition(
                "AND d.due_date < CAST(GETDATE() AS DATE) ",
                userId);

    }

    private int countByCondition(String extraCondition, Integer userId) {

        String sql =
                "SELECT COUNT(*) AS total "
                + "FROM Debt d "
                + "JOIN Invoice i ON d.invoice_id = i.invoice_id "
                + "WHERE d.remaining_amount > 0 "
                + extraCondition
                + (userId != null ? "AND i.user_id = ? " : "");

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            if (userId != null) {
                ps.setInt(1, userId);
            }

            ResultSet rs = ps.executeQuery();

            int total = 0;

            if (rs.next()) {
                total = rs.getInt("total");
            }

            rs.close();
            ps.close();
            con.close();

            return total;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return 0;

    }

}
