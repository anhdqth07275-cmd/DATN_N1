package model;

/**
 * Số liệu tổng hợp Nhật ký hoạt động trong 1 khoảng thời gian:
 * tổng thu, tổng chi, số lượt thêm/sửa/xóa, số lượt đăng nhập.
 */
public class ActivitySummary {

    private double totalReceipt;   // Tổng tiền thu (THU)
    private double totalExpense;   // Tổng tiền chi (CHI)
    private int receiptCount;      // Số lượt lập phiếu thu
    private int expenseCount;      // Số lượt lập phiếu chi
    private int addCount;          // Số lượt thêm (THEM)
    private int editCount;         // Số lượt sửa (SUA)
    private int deleteCount;       // Số lượt xóa (XOA)
    private int loginCount;        // Số lượt đăng nhập

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

    public int getReceiptCount() {
        return receiptCount;
    }

    public void setReceiptCount(int receiptCount) {
        this.receiptCount = receiptCount;
    }

    public int getExpenseCount() {
        return expenseCount;
    }

    public void setExpenseCount(int expenseCount) {
        this.expenseCount = expenseCount;
    }

    public int getAddCount() {
        return addCount;
    }

    public void setAddCount(int addCount) {
        this.addCount = addCount;
    }

    public int getEditCount() {
        return editCount;
    }

    public void setEditCount(int editCount) {
        this.editCount = editCount;
    }

    public int getDeleteCount() {
        return deleteCount;
    }

    public void setDeleteCount(int deleteCount) {
        this.deleteCount = deleteCount;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

}
