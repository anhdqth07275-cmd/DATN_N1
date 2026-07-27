package model;

public class Dashboard {

    // ==========================
    // Thống kê số lượng
    // ==========================

    private int totalCustomer;
    private int totalInvoice;

    private int totalReceiptCount;
    private int totalExpenseCount;

    // ==========================
    // Thống kê tiền
    // ==========================

    private double totalRevenue;
    private double totalReceiptAmount;
    private double totalExpenseAmount;
    private double totalDebt;

    // ==========================
    // Doanh thu theo thời gian
    // ==========================

    private double revenueToday;
    private double revenueMonth;
    private double revenueYear;

    public Dashboard() {
    }

    // ==========================
    // Customer
    // ==========================

    public int getTotalCustomer() {
        return totalCustomer;
    }

    public void setTotalCustomer(int totalCustomer) {
        this.totalCustomer = totalCustomer;
    }

    // ==========================
    // Invoice
    // ==========================

    public int getTotalInvoice() {
        return totalInvoice;
    }

    public void setTotalInvoice(int totalInvoice) {
        this.totalInvoice = totalInvoice;
    }

    // ==========================
    // Receipt Count
    // ==========================

    public int getTotalReceiptCount() {
        return totalReceiptCount;
    }

    public void setTotalReceiptCount(int totalReceiptCount) {
        this.totalReceiptCount = totalReceiptCount;
    }

    // ==========================
    // Expense Count
    // ==========================

    public int getTotalExpenseCount() {
        return totalExpenseCount;
    }

    public void setTotalExpenseCount(int totalExpenseCount) {
        this.totalExpenseCount = totalExpenseCount;
    }

    // ==========================
    // Revenue
    // ==========================

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    // ==========================
    // Receipt Amount
    // ==========================

    public double getTotalReceiptAmount() {
        return totalReceiptAmount;
    }

    public void setTotalReceiptAmount(double totalReceiptAmount) {
        this.totalReceiptAmount = totalReceiptAmount;
    }

    // ==========================
    // Expense Amount
    // ==========================

    public double getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public void setTotalExpenseAmount(double totalExpenseAmount) {
        this.totalExpenseAmount = totalExpenseAmount;
    }

    // ==========================
    // Debt
    // ==========================

    public double getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(double totalDebt) {
        this.totalDebt = totalDebt;
    }

    // ==========================
    // Today Revenue
    // ==========================

    public double getRevenueToday() {
        return revenueToday;
    }

    public void setRevenueToday(double revenueToday) {
        this.revenueToday = revenueToday;
    }

    // ==========================
    // Month Revenue
    // ==========================

    public double getRevenueMonth() {
        return revenueMonth;
    }

    public void setRevenueMonth(double revenueMonth) {
        this.revenueMonth = revenueMonth;
    }

    // ==========================
    // Year Revenue
    // ==========================

    public double getRevenueYear() {
        return revenueYear;
    }

    public void setRevenueYear(double revenueYear) {
        this.revenueYear = revenueYear;
    }

    // ==========================
    // Doanh thu 12 tháng trong năm hiện tại + trạng thái hóa đơn
    // (phục vụ biểu đồ ở Trang chủ - dữ liệu thật thay vì số liệu mẫu)
    // ==========================

    private double[] monthlyRevenue = new double[12];
    private int paidInvoiceCount;
    private int unpaidInvoiceCount;

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

    // Trả về chuỗi JSON dạng [1000000,2000000,...] để nhúng thẳng vào
    // script Chart.js trên trangchu.jsp, không cần thư viện JSON ngoài.
    public String getMonthlyRevenueJson() {

        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < monthlyRevenue.length; i++) {

            if (i > 0) {
                sb.append(",");
            }

            sb.append(monthlyRevenue[i]);

        }

        sb.append("]");

        return sb.toString();

    }

}