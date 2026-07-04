package model;

import java.math.BigDecimal;

public class InvoiceDetail {

    private int detailId;
    private int invoiceId;
    private int productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public InvoiceDetail() {
    }

    public InvoiceDetail(int detailId, int invoiceId, int productId,
            int quantity, BigDecimal unitPrice, BigDecimal subtotal) {

        this.detailId = detailId;
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

}
