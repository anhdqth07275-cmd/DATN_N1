package dao;

import util.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Dashboard;

public class DashboardDAO {
    // ==========================
// Dashboard
// ==========================
public Dashboard getDashboard() {

    Dashboard d = new Dashboard();

    try {

        Connection con =
                DBConnect.getConnection();

        PreparedStatement ps;

        ResultSet rs;

        // ==========================
        // Tổng khách hàng
        // ==========================

        ps = con.prepareStatement(
                "SELECT COUNT(*) FROM Customer");

        rs = ps.executeQuery();

        if (rs.next()) {

            d.setTotalCustomer(
                    rs.getInt(1));

        }

        // ==========================
        // Tổng hóa đơn
        // ==========================

        ps = con.prepareStatement(
                "SELECT COUNT(*) FROM Invoice");

        rs = ps.executeQuery();

        if (rs.next()) {

            d.setTotalInvoice(
                    rs.getInt(1));

        }

        // ==========================
        // Tổng doanh thu
        // ==========================

        ps = con.prepareStatement(
                "SELECT ISNULL(SUM(total_amount),0) "
                + "FROM Invoice");

        rs = ps.executeQuery();

        if (rs.next()) {

            d.setTotalRevenue(
                    rs.getDouble(1));

        }

        // ==========================
        // Tổng tiền đã thu
        // ==========================

        ps = con.prepareStatement(
                "SELECT ISNULL(SUM(amount),0) "
                + "FROM Receipt");

        rs = ps.executeQuery();

        if (rs.next()) {

            d.setTotalReceiptAmount(
        rs.getDouble(1));

        }

        // ==========================
        // Tổng chi
        // ==========================

        ps = con.prepareStatement(
                "SELECT ISNULL(SUM(amount),0) "
                + "FROM Expense_Voucher");

        rs = ps.executeQuery();

        if (rs.next()) {

            d.setTotalExpenseAmount(
        rs.getDouble(1));
        }

        // ==========================
        // Tổng công nợ
        // ==========================

        ps = con.prepareStatement(
                "SELECT ISNULL(SUM(remaining_amount),0) "
                + "FROM Debt");

        rs = ps.executeQuery();

        if (rs.next()) {

            d.setTotalDebt(
                    rs.getDouble(1));

        }
        // ==========================
// Tổng số phiếu thu
// ==========================

ps = con.prepareStatement(
        "SELECT COUNT(*) FROM Receipt");

rs = ps.executeQuery();

if (rs.next()) {

    d.setTotalReceiptCount(
            rs.getInt(1));

}

// ==========================
// Tổng số phiếu chi
// ==========================

ps = con.prepareStatement(
        "SELECT COUNT(*) FROM Expense_Voucher");

rs = ps.executeQuery();

if (rs.next()) {

    d.setTotalExpenseCount(
            rs.getInt(1));

}
        con.close();

    } catch (Exception e) {

        e.printStackTrace();

    }

    return d;

}
}