package model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HoaDon {

    private int invoiceId;
    private int customerId;
    private int userId;

    private String customerName;
    private String userName;

    private Date invoiceDate;

    private double totalAmount;

    private String status;

    public HoaDon() {
    }

    public HoaDon(int invoiceId, int customerId, int userId,
                  String customerName, String userName,
                  Date invoiceDate,
                  double totalAmount,
                  String status) {

        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.userId = userId;
        this.customerName = customerName;
        this.userName = userName;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoiceCode() {
        return String.format("HD%04d", invoiceId);
    }

    public String getMoney() {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(totalAmount);
    }

    public String getDateVN() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(invoiceDate);
    }
}