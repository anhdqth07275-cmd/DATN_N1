package model;

import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Receipt {

    private int receiptId;

    private int customerId;

    private int invoiceId;

    private int userId;

    private Date receiptDate;

    private double amount;

    private String note;

    // Hiển thị
    private String customerName;

    private String invoiceCode;

    private String userName;

    public Receipt() {
    }

    public Receipt(int receiptId,
            int customerId,
            int invoiceId,
            int userId,
            Date receiptDate,
            double amount,
            String note) {

        this.receiptId = receiptId;
        this.customerId = customerId;
        this.invoiceId = invoiceId;
        this.userId = userId;
        this.receiptDate = receiptDate;
        this.amount = amount;
        this.note = note;

    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getInvoiceCode() {
        return "HD" + String.format("%04d", invoiceId);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReceiptCode() {
        return "PT" + String.format("%04d", receiptId);
    }

    public String getMoney() {

        NumberFormat nf =
                NumberFormat.getNumberInstance(
                        new Locale("vi", "VN"));

        return nf.format(amount);

    }

    public String getDateVN() {

        if (receiptDate == null) {

            return "";

        }

        return new SimpleDateFormat("dd/MM/yyyy")
                .format(receiptDate);

    }

}