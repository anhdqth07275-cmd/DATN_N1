package model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Debt {

    private int debtId;
    private int customerId;
    private int invoiceId;

    private String customerName;

    private double remainingAmount;

    private Date dueDate;

    private String status;

    public Debt() {
    }

    public Debt(int debtId,
            int customerId,
            int invoiceId,
            String customerName,
            double remainingAmount,
            Date dueDate,
            String status) {

        this.debtId = debtId;
        this.customerId = customerId;
        this.invoiceId = invoiceId;
        this.customerName = customerName;
        this.remainingAmount = remainingAmount;
        this.dueDate = dueDate;
        this.status = status;

    }

    // ==========================
    // Getter & Setter
    // ==========================

    public int getDebtId() {
        return debtId;
    }

    public void setDebtId(int debtId) {
        this.debtId = debtId;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ==========================
    // Hiển thị
    // ==========================

    public String getInvoiceCode() {

        return String.format("HD%04d", invoiceId);

    }

    public String getDebtCode() {

        return String.format("CN%04d", debtId);

    }

    public String getMoney() {

        DecimalFormat df = new DecimalFormat("#,###");

        return df.format(remainingAmount);

    }

    public String getDateVN() {

        if (dueDate == null) {

            return "";

        }

        SimpleDateFormat sdf =
                new SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(dueDate);

    }

}