package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.InvoiceDetail;
import util.DBConnect;

public class InvoiceDetailDAO {
    public ArrayList<InvoiceDetail> getByInvoiceId(int invoiceId){

    ArrayList<InvoiceDetail> list = new ArrayList<>();

    String sql =
            "SELECT * FROM Invoice_Detail WHERE invoice_id=?";

    try{

        Connection con = DBConnect.getConnection();

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, invoiceId);

        ResultSet rs = ps.executeQuery();

        while(rs.next()){

            InvoiceDetail d = new InvoiceDetail();

            d.setDetailId(rs.getInt("detail_id"));
            d.setInvoiceId(rs.getInt("invoice_id"));
            d.setItemName(rs.getString("item_name"));
            d.setUnit(rs.getString("unit"));
            d.setQuantity(rs.getInt("quantity"));
            d.setUnitPrice(rs.getDouble("unit_price"));
            d.setSubtotal(rs.getDouble("subtotal"));

            list.add(d);

        }

        con.close();

    }catch(Exception e){

        e.printStackTrace();

    }

    return list;

}
    public boolean insert(InvoiceDetail d){

    String sql =
    "INSERT INTO Invoice_Detail(invoice_id,item_name,unit,quantity,unit_price,subtotal) "
    + "VALUES(?,?,?,?,?,?)";

    try{

        Connection con = DBConnect.getConnection();

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1,d.getInvoiceId());

        ps.setString(2,d.getItemName());

        ps.setString(3,d.getUnit());

        ps.setInt(4,d.getQuantity());

        ps.setDouble(5,d.getUnitPrice());

        ps.setDouble(6,d.getQuantity()*d.getUnitPrice());

        int row = ps.executeUpdate();

        con.close();

        return row>0;

    }catch(Exception e){

        e.printStackTrace();

    }

    return false;

}
    private void updateInvoiceTotal(int invoiceId){

    String sql =
    "UPDATE Invoice "
    + "SET total_amount=("
    + "SELECT ISNULL(SUM(subtotal),0) "
    + "FROM Invoice_Detail "
    + "WHERE invoice_id=?"
    + ") "
    + "WHERE invoice_id=?";

    try{

        Connection con = DBConnect.getConnection();

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, invoiceId);

        ps.setInt(2, invoiceId);

        ps.executeUpdate();

        con.close();

    }catch(Exception e){

        e.printStackTrace();

    }

}
    public boolean update(InvoiceDetail d){

    String sql =
    "UPDATE Invoice_Detail SET "
    + "item_name=?,"
    + "unit=?,"
    + "quantity=?,"
    + "unit_price=?,"
    + "subtotal=? "
    + "WHERE detail_id=?";

    try{

        Connection con = DBConnect.getConnection();

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1,d.getItemName());

        ps.setString(2,d.getUnit());

        ps.setInt(3,d.getQuantity());

        ps.setDouble(4,d.getUnitPrice());

        double subtotal =
                d.getQuantity()*d.getUnitPrice();

        ps.setDouble(5,subtotal);

        ps.setInt(6,d.getDetailId());

        int row=ps.executeUpdate();

        con.close();

        if(row>0){

            updateInvoiceTotal(d.getInvoiceId());

        }

        return row>0;

    }catch(Exception e){

        e.printStackTrace();

    }

    return false;

}
    public boolean delete(int detailId,int invoiceId){

    String sql=
    "DELETE FROM Invoice_Detail WHERE detail_id=?";

    try{

        Connection con=DBConnect.getConnection();

        PreparedStatement ps=con.prepareStatement(sql);

        ps.setInt(1,detailId);

        int row=ps.executeUpdate();

        con.close();

        if(row>0){

            updateInvoiceTotal(invoiceId);

        }

        return row>0;

    }catch(Exception e){

        e.printStackTrace();

    }

    return false;

}
    public boolean deleteByInvoice(int invoiceId){

    String sql=
    "DELETE FROM Invoice_Detail WHERE invoice_id=?";

    try{

        Connection con=DBConnect.getConnection();

        PreparedStatement ps=con.prepareStatement(sql);

        ps.setInt(1,invoiceId);

        int row=ps.executeUpdate();

        con.close();

        updateInvoiceTotal(invoiceId);

        return row>0;

    }catch(Exception e){

        e.printStackTrace();

    }

    return false;

}
    
}