package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.InvoiceDetail;
import util.DBConnect;

public class InvoiceDetailDao {

    // Lấy tất cả sản phẩm của 1 hóa đơn
    public ArrayList<InvoiceDetail> getByInvoiceId(int invoiceId) {

        ArrayList<InvoiceDetail> list = new ArrayList<>();

        String sql = "SELECT * FROM Invoice_Detail WHERE invoice_id = ?";

        try (
                Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {

            ps.setInt(1, invoiceId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                InvoiceDetail detail = new InvoiceDetail();

                detail.setDetailId(rs.getInt("detail_id"));
                detail.setInvoiceId(rs.getInt("invoice_id"));
                detail.setProductId(rs.getInt("product_id"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setUnitPrice(rs.getBigDecimal("unit_price"));
                detail.setSubtotal(rs.getBigDecimal("subtotal"));

                list.add(detail);

            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

}