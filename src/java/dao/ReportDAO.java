package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import model.Debt;
import model.Report;
import util.DBConnect;

public class ReportDAO {

    private static final String[] MONTH_LABELS = {
        "T1", "T2", "T3", "T4", "T5", "T6",
        "T7", "T8", "T9", "T10", "T11", "T12"
    };

    // ==========================
    // Lấy toàn bộ dữ liệu báo cáo theo khoảng thời gian
    // range: thisMonth | thisQuarter | thisYear
    // ==========================
    public Report getReport(String range) {

        if (range == null) {
            range = "thisMonth";
        }

        Report report = new Report();

        report.setRange(range);

        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();

        String rangeLabel;

        switch (range) {

            case "thisQuarter":

                int currentMonth = calStart.get(Calendar.MONTH); // 0-11
                int quarterStartMonth = (currentMonth / 3) * 3;

                calStart.set(Calendar.MONTH, quarterStartMonth);
                calStart.set(Calendar.DAY_OF_MONTH, 1);

                rangeLabel = "Quý này";

                break;

            case "thisYear":

                calStart.set(Calendar.DAY_OF_YEAR, 1);

                rangeLabel = "Năm nay";

                break;

            default:

                range = "thisMonth";
                calStart.set(Calendar.DAY_OF_MONTH, 1);

                rangeLabel = "Tháng này";

                break;

        }

        clearTime(calStart, true);
        clearTime(calEnd, false);

        Timestamp startTs = new Timestamp(calStart.getTimeInMillis());
        Timestamp endTs = new Timestamp(calEnd.getTimeInMillis());

        report.setRange(range);
        report.setRangeLabel(rangeLabel);

        report.setTotalRevenue(getTotalRevenue(startTs, endTs));
        report.setTotalReceipt(getTotalReceipt(startTs, endTs));
        report.setTotalExpense(getTotalExpense(startTs, endTs));
        report.setTotalDebt(getTotalDebt());

        report.setMonthLabels(MONTH_LABELS);
        report.setMonthlyRevenue(
                getMonthlyRevenue(
                        Calendar.getInstance().get(Calendar.YEAR)));

        int[] invoiceStatus = getInvoiceStatusCount(startTs, endTs);

        report.setPaidInvoiceCount(invoiceStatus[0]);
        report.setUnpaidInvoiceCount(invoiceStatus[1]);

        report.setTopDebts(getTopDebts(10));

        return report;

    }

    // Đưa giờ về đầu ngày (00:00:00) hoặc cuối thời điểm hiện tại
    private void clearTime(Calendar cal, boolean startOfDay) {

        if (startOfDay) {

            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

        } else {

            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);

        }

    }

    // ==========================
    // Tổng doanh thu (theo hóa đơn) trong khoảng thời gian
    // ==========================
    private double getTotalRevenue(Timestamp start, Timestamp end) {

        String sql =
                "SELECT ISNULL(SUM(total_amount),0) "
                + "FROM Invoice "
                + "WHERE invoice_date BETWEEN ? AND ?";

        return queryTotal(sql, start, end);

    }

    // ==========================
    // Tổng tiền đã thu trong khoảng thời gian
    // ==========================
    private double getTotalReceipt(Timestamp start, Timestamp end) {

        String sql =
                "SELECT ISNULL(SUM(amount),0) "
                + "FROM Receipt "
                + "WHERE payment_date BETWEEN ? AND ?";

        return queryTotal(sql, start, end);

    }

    // ==========================
    // Tổng tiền đã chi trong khoảng thời gian
    // ==========================
    private double getTotalExpense(Timestamp start, Timestamp end) {

        String sql =
                "SELECT ISNULL(SUM(amount),0) "
                + "FROM Expense_Voucher "
                + "WHERE expense_date BETWEEN ? AND ?";

        return queryTotal(sql, start, end);

    }

    // Hàm dùng chung để chạy các câu SUM(...) BETWEEN ? AND ?
    private double queryTotal(String sql, Timestamp start, Timestamp end) {

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setTimestamp(1, start);
            ps.setTimestamp(2, end);

            ResultSet rs = ps.executeQuery();

            double total = 0;

            if (rs.next()) {

                total = rs.getDouble(1);

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

    // ==========================
    // Tổng công nợ hiện đang còn phải thu
    // ==========================
    private double getTotalDebt() {

        String sql =
                "SELECT ISNULL(SUM(remaining_amount),0) "
                + "FROM Debt "
                + "WHERE remaining_amount > 0";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            double total = 0;

            if (rs.next()) {

                total = rs.getDouble(1);

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

    // ==========================
    // Doanh thu theo từng tháng trong 1 năm (phục vụ biểu đồ)
    // ==========================
    private double[] getMonthlyRevenue(int year) {

        double[] result = new double[12];

        String sql =
                "SELECT MONTH(invoice_date) AS mo, "
                + "SUM(total_amount) AS amt "
                + "FROM Invoice "
                + "WHERE YEAR(invoice_date) = ? "
                + "GROUP BY MONTH(invoice_date)";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, year);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int month = rs.getInt("mo");
                double amt = rs.getDouble("amt");

                if (month >= 1 && month <= 12) {

                    result[month - 1] = amt;

                }

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;

    }

    // ==========================
    // Đếm số hóa đơn đã thanh toán / còn nợ trong khoảng thời gian
    // Trả về mảng [paidCount, unpaidCount]
    // ==========================
    private int[] getInvoiceStatusCount(Timestamp start, Timestamp end) {

        int[] result = new int[2]; // 0: paid, 1: unpaid

        String sql =
                "SELECT status, COUNT(*) AS cnt "
                + "FROM Invoice "
                + "WHERE invoice_date BETWEEN ? AND ? "
                + "GROUP BY status";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setTimestamp(1, start);
            ps.setTimestamp(2, end);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String status = rs.getString("status");
                int cnt = rs.getInt("cnt");

                if ("Đã thanh toán".equalsIgnoreCase(status)) {

                    result[0] += cnt;

                } else {

                    result[1] += cnt;

                }

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;

    }

    // ==========================
    // Top khách hàng có công nợ còn lại nhiều nhất
    // ==========================
    private ArrayList<Debt> getTopDebts(int limit) {

        ArrayList<Debt> list = new ArrayList<>();

        String sql =
                "SELECT TOP " + limit + " "
                + "d.debt_id, d.invoice_id, d.customer_id, "
                + "d.remaining_amount, d.due_date, d.status, "
                + "c.customer_name "
                + "FROM Debt d "
                + "JOIN Customer c "
                + "ON d.customer_id = c.customer_id "
                + "WHERE d.remaining_amount > 0 "
                + "ORDER BY d.remaining_amount DESC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Debt d = new Debt();

                d.setDebtId(rs.getInt("debt_id"));
                d.setInvoiceId(rs.getInt("invoice_id"));
                d.setCustomerId(rs.getInt("customer_id"));
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

}
