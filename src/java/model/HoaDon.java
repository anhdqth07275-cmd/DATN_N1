package model;

import java.math.BigDecimal;
import java.util.Date;

public class HoaDon {

    private int invoiceId;
    private int customerId;
    private int userId;
    private Date invoiceDate;
    private BigDecimal totalAmount;
    private String status;

    public HoaDon() {
    }

    // Constructor dùng khi lấy dữ liệu từ database
    public HoaDon(int invoiceId, int customerId, int userId,
            Date invoiceDate, BigDecimal totalAmount, String status) {

        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.userId = userId;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Constructor dùng khi thêm hóa đơn
    public HoaDon(int customerId, int userId,
            Date invoiceDate, BigDecimal totalAmount, String status) {

        this.customerId = customerId;
        this.userId = userId;
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

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "invoiceId=" + invoiceId +
                ", customerId=" + customerId +
                ", userId=" + userId +
                ", invoiceDate=" + invoiceDate +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                '}';
    }
}