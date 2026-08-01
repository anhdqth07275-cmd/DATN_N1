package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.ExpenseVoucher;
import util.DBConnect;

public class ExpenseVoucherDAO {

    // ==========================
    // Lấy tất cả phiếu chi
    // ==========================
    public ArrayList<ExpenseVoucher> getAll() {
        return getAll(false);
    }

    public ArrayList<ExpenseVoucher> getAll(boolean includeInactive) {

        ArrayList<ExpenseVoucher> list = new ArrayList<>();

        String sql =
                "SELECT e.*, u.full_name "
                + "FROM Expense_Voucher e "
                + "JOIN [User] u "
                + "ON e.user_id=u.user_id "
                + (includeInactive ? "" : "WHERE e.is_active = 1 ")
                + "ORDER BY expense_id DESC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                ExpenseVoucher e =
                        new ExpenseVoucher();

                e.setExpenseId(
                        rs.getInt("expense_id"));

                e.setUserId(
                        rs.getInt("user_id"));

                e.setUserName(
                        rs.getString("full_name"));

                e.setExpenseName(
                        rs.getString("expense_name"));

                e.setAmount(
                        rs.getDouble("amount"));

                e.setExpenseDate(
                        rs.getTimestamp("expense_date"));

                e.setDescription(
                        rs.getString("description"));

                e.setEvidenceImage(
                        rs.getString("evidence_image"));

                e.setActive(
                        rs.getBoolean("is_active"));

                list.add(e);

            }

            con.close();

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return list;

    }

    // ==========================
    // Lấy theo ID
    // ==========================
    public ExpenseVoucher getById(int id) {

        String sql =
                "SELECT e.*, u.full_name "
                + "FROM Expense_Voucher e "
                + "JOIN [User] u "
                + "ON e.user_id=u.user_id "
                + "WHERE expense_id=?";

        try {

            Connection con =
                    DBConnect.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                ExpenseVoucher e =
                        new ExpenseVoucher();

                e.setExpenseId(
                        rs.getInt("expense_id"));

                e.setUserId(
                        rs.getInt("user_id"));

                e.setUserName(
                        rs.getString("full_name"));

                e.setExpenseName(
                        rs.getString("expense_name"));

                e.setAmount(
                        rs.getDouble("amount"));

                e.setExpenseDate(
                        rs.getTimestamp("expense_date"));

                e.setDescription(
                        rs.getString("description"));

                e.setEvidenceImage(
                        rs.getString("evidence_image"));

                con.close();

                return e;

            }

            con.close();

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return null;

    }
        // ==========================
    // Thêm phiếu chi
    // ==========================
    public boolean insert(ExpenseVoucher e) {

        String sql =
                "INSERT INTO Expense_Voucher"
                + "(user_id,expense_name,amount,description,evidence_image) "
                + "VALUES(?,?,?,?,?)";

        try {

            Connection con =
                    DBConnect.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, e.getUserId());

            ps.setString(2, e.getExpenseName());

            ps.setDouble(3, e.getAmount());

            ps.setString(4, e.getDescription());

            ps.setString(5, e.getEvidenceImage());

            int row =
                    ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return false;

    }

    // ==========================
    // Cập nhật phiếu chi
    // ==========================
    public boolean update(ExpenseVoucher e) {

        String sql =
                "UPDATE Expense_Voucher "
                + "SET "
                + "expense_name=?, "
                + "amount=?, "
                + "description=?, "
                + "evidence_image=? "
                + "WHERE expense_id=?";

        try {

            Connection con =
                    DBConnect.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, e.getExpenseName());

            ps.setDouble(2, e.getAmount());

            ps.setString(3, e.getDescription());

            ps.setString(4, e.getEvidenceImage());

            ps.setInt(5, e.getExpenseId());

            int row =
                    ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return false;

    }
        // ==========================
    // Giữ tương thích ngược - delete() thực chất luôn là xóa mềm
    // ==========================
    public boolean delete(int id) {
        return softDelete(id);
    }

    // ==========================
    // Xóa mềm: ẩn phiếu chi khỏi hệ thống, có thể khôi phục
    // ==========================
    public boolean softDelete(int id) {
        return util.SoftDeleteHelper.setActive(
                "Expense_Voucher", "expense_id", "is_active", id, false);
    }

    // ==========================
    // Khôi phục phiếu chi đã bị vô hiệu hóa
    // ==========================
    public boolean restore(int id) {
        return util.SoftDeleteHelper.setActive(
                "Expense_Voucher", "expense_id", "is_active", id, true);
    }

    // ==========================
    // Tìm kiếm phiếu chi
    // ==========================
    public ArrayList<ExpenseVoucher> search(String keyword) {

        ArrayList<ExpenseVoucher> list =
                new ArrayList<>();

        String sql =
                "SELECT e.*, u.full_name "
                + "FROM Expense_Voucher e "
                + "JOIN [User] u "
                + "ON e.user_id=u.user_id "
                + "WHERE "
                + "expense_name LIKE ? "
                + "OR full_name LIKE ? "
                + "ORDER BY expense_id DESC";

        try {

            Connection con =
                    DBConnect.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");

            ps.setString(2, "%" + keyword + "%");

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                ExpenseVoucher e =
                        new ExpenseVoucher();

                e.setExpenseId(
                        rs.getInt("expense_id"));

                e.setUserId(
                        rs.getInt("user_id"));

                e.setUserName(
                        rs.getString("full_name"));

                e.setExpenseName(
                        rs.getString("expense_name"));

                e.setAmount(
                        rs.getDouble("amount"));

                e.setExpenseDate(
                        rs.getTimestamp("expense_date"));

                e.setDescription(
                        rs.getString("description"));

                e.setEvidenceImage(
                        rs.getString("evidence_image"));

                list.add(e);

            }

            con.close();

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return list;

    }

}