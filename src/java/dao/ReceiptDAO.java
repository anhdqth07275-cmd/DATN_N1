package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Receipt;
import util.DBConnect;

public class ReceiptDAO {

    // ==========================
    // Lấy toàn bộ phiếu thu
    // ==========================
    public ArrayList<Receipt> getAll() {

        ArrayList<Receipt> list = new ArrayList<>();

        String sql
                = "SELECT r.*, "
                + "c.customer_name, "
                + "u.full_name "
                + "FROM Receipt r "
                + "JOIN Invoice i "
                + "ON r.invoice_id=i.invoice_id "
                + "JOIN Customer c "
                + "ON i.customer_id=c.customer_id "
                + "JOIN [User] u "
                + "ON r.user_id=u.user_id "
                + "ORDER BY r.receipt_id DESC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Receipt r = new Receipt();

                r.setReceiptId(
                        rs.getInt("receipt_id"));

                r.setInvoiceId(
                        rs.getInt("invoice_id"));

                r.setUserId(
                        rs.getInt("user_id"));

                r.setPaymentDate(
                        rs.getDate("payment_date"));

                r.setAmount(
                        rs.getDouble("amount"));

                r.setPaymentMethod(
                        rs.getString("payment_method"));

                r.setNote(
                        rs.getString("note"));

                r.setCustomerName(
                        rs.getString("customer_name"));

                r.setUserName(
                        rs.getString("full_name"));

                list.add(r);

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

    // ==========================
    // Lấy theo ID
    // ==========================
    public Receipt getById(int id) {

        String sql
                = "SELECT r.*, "
                + "c.customer_name, "
                + "u.full_name "
                + "FROM Receipt r "
                + "JOIN Invoice i "
                + "ON r.invoice_id=i.invoice_id "
                + "JOIN Customer c "
                + "ON i.customer_id=c.customer_id "
                + "JOIN [User] u "
                + "ON r.user_id=u.user_id "
                + "WHERE r.receipt_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Receipt r = new Receipt();

                r.setReceiptId(
                        rs.getInt("receipt_id"));

                r.setInvoiceId(
                        rs.getInt("invoice_id"));

                r.setUserId(
                        rs.getInt("user_id"));

                r.setPaymentDate(
                        rs.getDate("payment_date"));

                r.setAmount(
                        rs.getDouble("amount"));

                r.setPaymentMethod(
                        rs.getString("payment_method"));

                r.setNote(
                        rs.getString("note"));

                r.setCustomerName(
                        rs.getString("customer_name"));

                r.setUserName(
                        rs.getString("full_name"));

                con.close();

                return r;

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

// ==========================
// Thêm phiếu thu
// ==========================
    public boolean insert(Receipt r) {

        String sql
                = "INSERT INTO Receipt("
                + "invoice_id,"
                + "user_id,"
                + "amount,"
                + "payment_method,"
                + "note"
                + ") VALUES(?,?,?,?,?)";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ps.setInt(1, r.getInvoiceId());

            ps.setInt(2, r.getUserId());

            ps.setDouble(3, r.getAmount());

            ps.setString(4, r.getPaymentMethod());

            ps.setString(5, r.getNote());

            int row = ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

// ==========================
// Cập nhật phiếu thu
// ==========================
    public boolean update(Receipt r) {

        String sql
                = "UPDATE Receipt SET "
                + "invoice_id=?,"
                + "amount=?,"
                + "payment_method=?,"
                + "note=? "
                + "WHERE receipt_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ps.setInt(1, r.getInvoiceId());

            ps.setDouble(2, r.getAmount());

            ps.setString(3, r.getPaymentMethod());

            ps.setString(4, r.getNote());

            ps.setInt(5, r.getReceiptId());

            int row = ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

// ==========================
// Xóa phiếu thu
// ==========================
    public boolean delete(int id) {

        String sql
                = "DELETE FROM Receipt "
                + "WHERE receipt_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps
                    = con.prepareStatement(sql);

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
// Tìm kiếm phiếu thu
// ==========================

    public ArrayList<Receipt> search(String keyword) {

        ArrayList<Receipt> list = new ArrayList<>();

        String sql
                = "SELECT r.*, "
                + "c.customer_name, "
                + "u.full_name "
                + "FROM Receipt r "
                + "JOIN Invoice i "
                + "ON r.invoice_id=i.invoice_id "
                + "JOIN Customer c "
                + "ON i.customer_id=c.customer_id "
                + "JOIN [User] u "
                + "ON r.user_id=u.user_id "
                + "WHERE "
                + "c.customer_name LIKE ? "
                + "OR CAST(r.receipt_id AS NVARCHAR) LIKE ? "
                + "OR CAST(r.invoice_id AS NVARCHAR) LIKE ? "
                + "ORDER BY r.receipt_id DESC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps
                    = con.prepareStatement(sql);

            String key = "%" + keyword + "%";

            ps.setString(1, key);

            ps.setString(2, key);

            ps.setString(3, key);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Receipt r = new Receipt();

                r.setReceiptId(
                        rs.getInt("receipt_id"));

                r.setInvoiceId(
                        rs.getInt("invoice_id"));

                r.setUserId(
                        rs.getInt("user_id"));

                r.setPaymentDate(
                        rs.getDate("payment_date"));

                r.setAmount(
                        rs.getDouble("amount"));

                r.setPaymentMethod(
                        rs.getString("payment_method"));

                r.setNote(
                        rs.getString("note"));

                r.setCustomerName(
                        rs.getString("customer_name"));

                r.setUserName(
                        rs.getString("full_name"));

                list.add(r);

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }
}
