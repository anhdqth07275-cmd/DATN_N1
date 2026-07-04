package dao;

import java.sql.*;
import java.util.ArrayList;
import model.HoaDon;
import util.DBConnect;

public class HoaDonDao {

    // Lấy tất cả hóa đơn
    public ArrayList<HoaDon> getAll() {

        ArrayList<HoaDon> list = new ArrayList<>();

        String sql = "SELECT * FROM Invoice ORDER BY invoice_id DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                HoaDon hd = new HoaDon();

                hd.setInvoiceId(rs.getInt("invoice_id"));
                hd.setCustomerId(rs.getInt("customer_id"));
                hd.setUserId(rs.getInt("user_id"));
                hd.setInvoiceDate(rs.getTimestamp("invoice_date"));
                hd.setTotalAmount(rs.getBigDecimal("total_amount"));
                hd.setStatus(rs.getString("status"));

                list.add(hd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Thêm hóa đơn
    public boolean themHoaDon(HoaDon hd) {

        String sql = "INSERT INTO Invoice(customer_id,user_id,invoice_date,total_amount,status) "
                + "VALUES(?,?,?,?,?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hd.getCustomerId());
            ps.setInt(2, hd.getUserId());
            ps.setTimestamp(3, new Timestamp(hd.getInvoiceDate().getTime()));
            ps.setBigDecimal(4, hd.getTotalAmount());
            ps.setString(5, hd.getStatus());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Tìm hóa đơn theo ID
    public HoaDon timHoaDonTheoId(int id) {

        String sql = "SELECT * FROM Invoice WHERE invoice_id=?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                HoaDon hd = new HoaDon();

                hd.setInvoiceId(rs.getInt("invoice_id"));
                hd.setCustomerId(rs.getInt("customer_id"));
                hd.setUserId(rs.getInt("user_id"));
                hd.setInvoiceDate(rs.getTimestamp("invoice_date"));
                hd.setTotalAmount(rs.getBigDecimal("total_amount"));
                hd.setStatus(rs.getString("status"));

                return hd;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Tìm kiếm theo mã hóa đơn
    public ArrayList<HoaDon> timKiemHoaDon(String keyword) {

        ArrayList<HoaDon> list = new ArrayList<>();

        String sql = "SELECT * FROM Invoice WHERE CAST(invoice_id AS NVARCHAR) LIKE ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                HoaDon hd = new HoaDon();

                hd.setInvoiceId(rs.getInt("invoice_id"));
                hd.setCustomerId(rs.getInt("customer_id"));
                hd.setUserId(rs.getInt("user_id"));
                hd.setInvoiceDate(rs.getTimestamp("invoice_date"));
                hd.setTotalAmount(rs.getBigDecimal("total_amount"));
                hd.setStatus(rs.getString("status"));

                list.add(hd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}