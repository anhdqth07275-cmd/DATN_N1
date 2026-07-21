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
    public boolean update(HoaDon hd) {

        String sql
                = "UPDATE Invoice "
                + "SET customer_id=?, total_amount=?, status=? "
                + "WHERE invoice_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, hd.getCustomerId());
            ps.setDouble(2, hd.getTotalAmount());
            ps.setString(3, hd.getStatus());
            ps.setInt(4, hd.getInvoiceId());

            int row = ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // Xóa hóa đơn
    // ==========================
    public boolean delete(int id) {

        String sql
                = "DELETE FROM Invoice WHERE invoice_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            int row = ps.executeUpdate();

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