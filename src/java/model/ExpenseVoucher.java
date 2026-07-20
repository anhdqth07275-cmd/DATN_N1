package model;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExpenseVoucher {

    private int expenseId;

    private int userId;

    private String userName;

    private String expenseName;

    private double amount;

    private Date expenseDate;

    private String description;

    public ExpenseVoucher() {
    }

    public ExpenseVoucher(int expenseId,
            int userId,
            String userName,
            String expenseName,
            double amount,
            Date expenseDate,
            String description) {

        this.expenseId = expenseId;
        this.userId = userId;
        this.userName = userName;
        this.expenseName = expenseName;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.description = description;

    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // ==========================
    // Hiển thị tiền VN
    // ==========================
    public String getMoney() {

        NumberFormat nf =
                NumberFormat.getNumberInstance(
                        new Locale("vi", "VN"));

        return nf.format(amount);

    }

    // ==========================
    // Hiển thị ngày VN
    // ==========================
    public String getDateVN() {

        if (expenseDate == null) {

            return "";

        }

        SimpleDateFormat sdf =
                new SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(expenseDate);

    }

}