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
    // ==========================
    public ArrayList<Debt> getAll() {

        ArrayList<Debt> list = new ArrayList<>();

        String sql =
                "SELECT d.*, c.customer_name "
                + "FROM Debt d "
                + "JOIN Customer c "
                + "ON d.customer_id = c.customer_id "
                + "ORDER BY d.debt_id DESC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Debt d = new Debt();

                d.setDebtId(rs.getInt("debt_id"));
                d.setCustomerId(rs.getInt("customer_id"));
                d.setInvoiceId(rs.getInt("invoice_id"));
                d.setCustomerName(rs.getString("customer_name"));
                d.setRemainingAmount(rs.getDouble("remaining_amount"));
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
                "SELECT d.*, c.customer_name "
                + "FROM Debt d "
                + "JOIN Customer c "
                + "ON d.customer_id=c.customer_id "
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
            + "DATEADD(DAY,30,invoice_date),"
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
// Đồng bộ số tiền công nợ
// ==========================
// ==========================
// Đồng bộ số tiền công nợ
// ==========================
public void updateFromInvoice(int invoiceId){

    String sql =
            "UPDATE Debt "
            + "SET remaining_amount=i.total_amount "
            + "FROM Debt d "
            + "JOIN Invoice i "
            + "ON d.invoice_id=i.invoice_id "
            + "WHERE d.invoice_id=?";

    try{

        Connection con=DBConnect.getConnection();

        PreparedStatement ps=con.prepareStatement(sql);

        ps.setInt(1,invoiceId);

        ps.executeUpdate();

        con.close();

    }catch(Exception e){

        e.printStackTrace();

    }

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
    public ArrayList<Debt> search(String keyword) {

        ArrayList<Debt> list = new ArrayList<>();

        String sql =
                "SELECT d.*, c.customer_name "
                + "FROM Debt d "
                + "JOIN Customer c "
                + "ON d.customer_id=c.customer_id "
                + "WHERE c.customer_name LIKE ? "
                + "OR CAST(d.invoice_id AS NVARCHAR) LIKE ?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");

            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Debt d = new Debt();

                d.setDebtId(rs.getInt("debt_id"));
                d.setCustomerId(rs.getInt("customer_id"));
                d.setInvoiceId(rs.getInt("invoice_id"));
                d.setCustomerName(rs.getString("customer_name"));
                d.setRemainingAmount(rs.getDouble("remaining_amount"));
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

}