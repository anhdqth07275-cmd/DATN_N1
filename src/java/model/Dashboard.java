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

}