package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.HoaDon;
import util.DBConnect;

public class HoaDonDAO {

    // ==========================
    // Lấy toàn bộ hóa đơn
    // ==========================
    public ArrayList<HoaDon> getAll() {

        ArrayList<HoaDon> list = new ArrayList<>();

        String sql
                = "SELECT i.*, c.customer_name, u.full_name "
                + "FROM Invoice i "
                + "JOIN Customer c ON i.customer_id = c.customer_id "
                + "JOIN [User] u ON i.user_id = u.user_id "
                + "ORDER BY i.invoice_id DESC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                HoaDon hd = new HoaDon();

                hd.setInvoiceId(rs.getInt("invoice_id"));
                hd.setCustomerId(rs.getInt("customer_id"));
                hd.setUserId(rs.getInt("user_id"));
                hd.setInvoiceDate(rs.getDate("invoice_date"));
                hd.setTotalAmount(rs.getDouble("total_amount"));
                hd.setStatus(rs.getString("status"));

                hd.setCustomerName(rs.getString("customer_name"));
                hd.setUserName(rs.getString("full_name"));

                list.add(hd);

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

    // ==========================
    // Lấy hóa đơn theo ID
    // ==========================
    public HoaDon getById(int id) {

        String sql
                = "SELECT i.*, c.customer_name, u.full_name "
                + "FROM Invoice i "
                + "JOIN Customer c ON i.customer_id = c.customer_id "
                + "JOIN [User] u ON i.user_id = u.user_id "
                + "WHERE invoice_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                HoaDon hd = new HoaDon();

                hd.setInvoiceId(rs.getInt("invoice_id"));
                hd.setCustomerId(rs.getInt("customer_id"));
                hd.setUserId(rs.getInt("user_id"));
                hd.setInvoiceDate(rs.getDate("invoice_date"));
                hd.setTotalAmount(rs.getDouble("total_amount"));
                hd.setStatus(rs.getString("status"));

                hd.setCustomerName(rs.getString("customer_name"));
                hd.setUserName(rs.getString("full_name"));

                con.close();

                return hd;

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    // ==========================
    // Thêm hóa đơn
    // ==========================
    public int insert(HoaDon hd) {

        String sql
                = "INSERT INTO Invoice(customer_id,user_id,total_amount,status) "
                + "VALUES(?,?,?,?)";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    sql,
                    java.sql.Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, hd.getCustomerId());

            ps.setInt(2, hd.getUserId());

            // Khi mới tạo hóa đơn thì tổng tiền = 0
            ps.setDouble(3, 0);

            ps.setString(4, hd.getStatus());

            int row = ps.executeUpdate();

            if (row > 0) {

                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {

                    int invoiceId = rs.getInt(1);

                    rs.close();
                    ps.close();
                    con.close();

                    return invoiceId;

                }

                rs.close();
            }

            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return 0;

    }

    // ==========================
    // Cập nhật hóa đơn
    // ==========================
    // CHÚ Ý: total_amount KHÔNG được cập nhật ở đây. Nó luôn được
    // tính lại tự động từ tổng Invoice_Detail (xem
    // InvoiceDetailDAO.updateInvoiceTotal). Nếu ghi đè total_amount
    // ở form sửa hóa đơn thì tổng tiền sẽ bị lệch/mất mỗi khi người
    // dùng chỉ đổi khách hàng, đây chính là lỗi "tổng tiền về 0"
    // trước đây.
    // status cũng KHÔNG được sửa tay ở đây. Trạng thái thanh toán
    // luôn được suy ra tự động từ số tiền đã thu (xem
    // DebtDAO.updateFromInvoice), để tránh mâu thuẫn giữa hóa đơn -
    // công nợ - phiếu thu.
    public boolean update(HoaDon hd) {

        String sql
                = "UPDATE Invoice "
                + "SET customer_id=? "
                + "WHERE invoice_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, hd.getCustomerId());
            ps.setInt(2, hd.getInvoiceId());

            int row = ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // Kiểm tra hóa đơn có thể xóa hay không
    // ==========================
    // Trước đây delete() gọi thẳng DELETE FROM Invoice trong khi
    // Invoice_Detail/Debt/Receipt đều tham chiếu invoice_id -> nếu
    // DB có ràng buộc khóa ngoại thì lệnh xóa sẽ ném lỗi, bị
    // "nuốt" bởi catch(Exception) rồi vẫn redirect như xóa thành
    // công, khiến hóa đơn "không thể xóa" mà không rõ lý do.
    // Về nghiệp vụ: hóa đơn đã có phiếu thu (đã phát sinh giao
    // dịch tiền) thì không nên xóa để giữ tính toàn vẹn của sổ
    // sách kế toán - chỉ nên cho xóa hóa đơn chưa có phiếu thu nào.
    public boolean canDelete(int invoiceId) {

        String sql
                = "SELECT COUNT(*) cnt FROM Receipt WHERE invoice_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, invoiceId);

            ResultSet rs = ps.executeQuery();

            boolean ok = true;

            if (rs.next()) {
                ok = rs.getInt("cnt") == 0;
            }

            rs.close();
            ps.close();
            con.close();

            return ok;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // Xóa hóa đơn
    // ==========================
    // Xóa theo đúng thứ tự phụ thuộc khóa ngoại, trong 1 transaction:
    // Debt -> Invoice_Detail -> Invoice. Chỉ nên gọi khi canDelete()
    // trả về true (không còn phiếu thu nào gắn với hóa đơn).
    public boolean delete(int id) {

        Connection con = null;

        try {

            con = DBConnect.getConnection();
            con.setAutoCommit(false);

            PreparedStatement psDebt = con.prepareStatement(
                    "DELETE FROM Debt WHERE invoice_id=?");
            psDebt.setInt(1, id);
            psDebt.executeUpdate();
            psDebt.close();

            PreparedStatement psDetail = con.prepareStatement(
                    "DELETE FROM Invoice_Detail WHERE invoice_id=?");
            psDetail.setInt(1, id);
            psDetail.executeUpdate();
            psDetail.close();

            PreparedStatement psInvoice = con.prepareStatement(
                    "DELETE FROM Invoice WHERE invoice_id=?");
            psInvoice.setInt(1, id);
            int row = psInvoice.executeUpdate();
            psInvoice.close();

            con.commit();

            return row > 0;

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

        return false;

    }

    // ==========================
    // Tìm kiếm
    // ==========================
    public ArrayList<HoaDon> search(String keyword) {

        ArrayList<HoaDon> list = new ArrayList<>();

        String sql
                = "SELECT i.*, c.customer_name, u.full_name "
                + "FROM Invoice i "
                + "JOIN Customer c ON i.customer_id=c.customer_id "
                + "JOIN [User] u ON i.user_id=u.user_id "
                + "WHERE c.customer_name LIKE ? "
                + "OR CAST(i.invoice_id AS NVARCHAR) LIKE ?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                HoaDon hd = new HoaDon();

                hd.setInvoiceId(rs.getInt("invoice_id"));
                hd.setCustomerId(rs.getInt("customer_id"));
                hd.setUserId(rs.getInt("user_id"));

                hd.setCustomerName(rs.getString("customer_name"));
                hd.setUserName(rs.getString("full_name"));

                hd.setInvoiceDate(rs.getDate("invoice_date"));
                hd.setTotalAmount(rs.getDouble("total_amount"));
                hd.setStatus(rs.getString("status"));

                list.add(hd);

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

    // ==========================
// Lấy hóa đơn chưa thanh toán
// ==========================
    public ArrayList<HoaDon> getUnpaidInvoice() {

        ArrayList<HoaDon> list = new ArrayList<>();

        String sql
                = "SELECT "
                + "i.*, "
                + "c.customer_name, "
                + "u.full_name, "
                + "ISNULL(d.remaining_amount,0) remaining_amount "
                + "FROM Invoice i "
                + "JOIN Customer c "
                + "ON i.customer_id=c.customer_id "
                + "JOIN [User] u "
                + "ON i.user_id=u.user_id "
                + "LEFT JOIN Debt d "
                + "ON i.invoice_id=d.invoice_id "
                + "WHERE i.status<>N'Đã thanh toán' "
                + "ORDER BY i.invoice_id DESC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                HoaDon hd = new HoaDon();

                hd.setInvoiceId(
                        rs.getInt("invoice_id"));

                hd.setCustomerId(
                        rs.getInt("customer_id"));

                hd.setUserId(
                        rs.getInt("user_id"));

                hd.setInvoiceDate(
                        rs.getDate("invoice_date"));

                hd.setTotalAmount(
                        rs.getDouble("total_amount"));

                hd.setStatus(
                        rs.getString("status"));

                hd.setCustomerName(
                        rs.getString("customer_name"));

                hd.setUserName(
                        rs.getString("full_name"));

                hd.setRemainingAmount(
                        rs.getDouble("remaining_amount"));

                list.add(hd);

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }
    
    // ==========================
// 5 hóa đơn mới nhất
// ==========================
public ArrayList<HoaDon> getTop5Newest() {

    ArrayList<HoaDon> list = new ArrayList<>();

    String sql =
        "SELECT TOP 5 "
        + "i.invoice_id, "
        + "c.customer_name, "
        + "i.invoice_date, "
        + "i.total_amount, "
        + "i.status "
        + "FROM Invoice i "
        + "INNER JOIN Customer c "
        + "ON i.customer_id = c.customer_id "
        + "ORDER BY i.invoice_date DESC";

    try {

        Connection con = DBConnect.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ResultSet rs =
                ps.executeQuery();

        while (rs.next()) {

            HoaDon hd = new HoaDon();

            hd.setInvoiceId(
                    rs.getInt("invoice_id"));

            hd.setCustomerName(
                    rs.getString("customer_name"));

            hd.setInvoiceDate(
                    rs.getTimestamp("invoice_date"));

            hd.setTotalAmount(
                    rs.getDouble("total_amount"));

            hd.setStatus(
                    rs.getString("status"));

            list.add(hd);

        }

        con.close();

    } catch (Exception e) {

        e.printStackTrace();

    }

    return list;

}

}