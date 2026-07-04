package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.Customer;
import util.DBConnect;

public class CustomerDAO {

    public ArrayList<Customer> getAllCustomers() {

        ArrayList<Customer> list = new ArrayList<>();

        String sql = "SELECT * FROM Customer ORDER BY customer_id DESC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Customer c = new Customer();

                c.setCustomerId(rs.getInt("customer_id"));
                c.setCustomerName(rs.getString("customer_name"));
                c.setPhone(rs.getString("phone"));
                c.setAddress(rs.getString("address"));
                c.setEmail(rs.getString("email"));
                c.setCreatedDate(rs.getTimestamp("created_date"));
                c.setStatus(rs.getBoolean("status"));

                list.add(c);

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
}