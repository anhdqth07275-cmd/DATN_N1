package model;

import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Receipt {

    private int receiptId;
    private int invoiceId;
    private int userId;

    private Date paymentDate;

    private double amount;

    private String paymentMethod;

    private String note;

    // Hiển thị
    private String customerName;
    private String userName;

    public Receipt() {
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
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

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReceiptCode() {

        return "PT"
                + String.format("%04d", receiptId);

    }

    public String getInvoiceCode() {

        return "HD"
                + String.format("%04d", invoiceId);

    }

    public String getMoney() {

        NumberFormat nf
                = NumberFormat.getNumberInstance(
                        new Locale("vi", "VN"));

        return nf.format(amount);

    }

    public String getDateVN() {

        if (paymentDate == null) {

            return "";

        }

        return new SimpleDateFormat("dd/MM/yyyy")
                .format(paymentDate);

    }

}
