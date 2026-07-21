package model;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Report {

    // ==========================
    // Bộ lọc đang áp dụng
    // ==========================

    private String range;      // thisMonth | thisQuarter | thisYear
    private String rangeLabel; // Nhãn hiển thị cho người dùng

    // ==========================
    // 4 thẻ tổng quan
    // ==========================

    private double totalRevenue;   // Báo cáo doanh thu
    private double totalDebt;      // Báo cáo công nợ
    private double totalReceipt;   // Tổng đã thu (báo cáo thanh toán)
    private double totalExpense;   // Tổng đã chi (báo cáo chi thu)

    // ==========================
    // Biểu đồ doanh thu 12 tháng trong năm
    // ==========================

    private String[] monthLabels;
    private double[] monthlyRevenue;

    // ==========================
    // Biểu đồ trạng thái hóa đơn
    // ==========================

    private int paidInvoiceCount;
    private int unpaidInvoiceCount;

    // ==========================
    // Top công nợ còn lại nhiều nhất
    // ==========================

    private ArrayList<Debt> topDebts;

    public Report() {
    }

    // ==========================
    // Getter & Setter
    // ==========================

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getRangeLabel() {
        return rangeLabel;
    }

    public void setRangeLabel(String rangeLabel) {
        this.rangeLabel = rangeLabel;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(double totalDebt) {
        this.totalDebt = totalDebt;
    }

    public double getTotalReceipt() {
        return totalReceipt;
    }

    public void setTotalReceipt(double totalReceipt) {
        this.totalReceipt = totalReceipt;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public String[] getMonthLabels() {
        return monthLabels;
    }

    public void setMonthLabels(String[] monthLabels) {
        this.monthLabels = monthLabels;
    }

    public double[] getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(double[] monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public int getPaidInvoiceCount() {
        return paidInvoiceCount;
    }

    public void setPaidInvoiceCount(int paidInvoiceCount) {
        this.paidInvoiceCount = paidInvoiceCount;
    }

    public int getUnpaidInvoiceCount() {
        return unpaidInvoiceCount;
    }

    public void setUnpaidInvoiceCount(int unpaidInvoiceCount) {
        this.unpaidInvoiceCount = unpaidInvoiceCount;
    }

    public ArrayList<Debt> getTopDebts() {
        return topDebts;
    }

    public void setTopDebts(ArrayList<Debt> topDebts) {
        this.topDebts = topDebts;
    }

    // ==========================
    // Số dư = Thu - Chi trong kỳ
    // ==========================

    public double getBalance() {

        return totalReceipt - totalExpense;

    }

    // ==========================
    // Hiển thị tiền VNĐ
    // ==========================

    private String money(double value) {

        NumberFormat nf =
                NumberFormat.getNumberInstance(
                        new Locale("vi", "VN"));

        return nf.format(value);

    }

    public String getRevenueMoney() {
        return money(totalRevenue);
    }

    public String getDebtMoney() {
        return money(totalDebt);
    }

    public String getReceiptMoney() {
        return money(totalReceipt);
    }

    public String getExpenseMoney() {
        return money(totalExpense);
    }

    public String getBalanceMoney() {
        return money(getBalance());
    }

    // ==========================
    // Chuỗi JSON cho Chart.js
    // (không dùng thư viện ngoài,
    // project hiện chưa có JSON lib)
    // ==========================

    public String getMonthLabelsJson() {

        StringBuilder sb = new StringBuilder("[");

        if (monthLabels != null) {

            for (int i = 0; i < monthLabels.length; i++) {

                if (i > 0) {
                    sb.append(",");
                }

                sb.append("\"").append(monthLabels[i]).append("\"");

            }

        }

        sb.append("]");

        return sb.toString();

    }

    public String getMonthlyRevenueJson() {

        StringBuilder sb = new StringBuilder("[");

        if (monthlyRevenue != null) {

            for (int i = 0; i < monthlyRevenue.length; i++) {

                if (i > 0) {
                    sb.append(",");
                }

                sb.append(monthlyRevenue[i]);

            }

        }

        sb.append("]");

        return sb.toString();

    }

}
