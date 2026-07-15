package model;

public class InvoiceDetail {

    private int detailId;
    private int invoiceId;

    private String itemName;
    private String unit;

    private int quantity;
    private double unitPrice;
    private double subtotal;

    public InvoiceDetail() {
    }

    public InvoiceDetail(int detailId,
            int invoiceId,
            String itemName,
            String unit,
            int quantity,
            double unitPrice,
            double subtotal) {

        this.detailId = detailId;
        this.invoiceId = invoiceId;
        this.itemName = itemName;
        this.unit = unit;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        tinhThanhTien();
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        tinhThanhTien();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    private void tinhThanhTien() {
        this.subtotal = quantity * unitPrice;
    }

    public String getMoney() {

        java.text.DecimalFormat df =
                new java.text.DecimalFormat("#,###");

        return df.format(subtotal);

    }

}