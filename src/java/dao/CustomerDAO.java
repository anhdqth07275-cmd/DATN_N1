package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.Customer;
import util.DBConnect;

public class CustomerDAO {

    public ArrayList<Customer> getAll() {
        return getAll(false);
    }

    // includeInactive = true -> lấy cả khách hàng đã bị vô hiệu hóa (status=0)
    // Dùng khi Admin/Giám đốc bật "Hiển thị cả đã vô hiệu hóa" để có thể
    // khôi phục lại những bản ghi đó.
    public ArrayList<Customer> getAll(boolean includeInactive) {

        ArrayList<Customer> list = new ArrayList<>();

        String sql = "SELECT * FROM Customer\n"
                + (includeInactive ? "" : "WHERE status = 1\n")
                + "ORDER BY customer_id DESC;";

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

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

    public boolean insert(Customer c) {

        String sql = "INSERT INTO Customer(customer_name, phone, address, email, status) VALUES(?,?,?,?,?)";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, c.getCustomerName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getEmail());
            ps.setBoolean(5, true);

            int row = ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Customer getById(int id) {

        String sql = "SELECT * FROM Customer WHERE customer_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Customer c = new Customer();

                c.setCustomerId(rs.getInt("customer_id"));
                c.setCustomerName(rs.getString("customer_name"));
                c.setPhone(rs.getString("phone"));
                c.setAddress(rs.getString("address"));
                c.setEmail(rs.getString("email"));
                c.setCreatedDate(rs.getTimestamp("created_date"));
                c.setStatus(rs.getBoolean("status"));

                con.close();

                return c;

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    public boolean update(Customer c) {

        String sql = "UPDATE Customer SET customer_name=?,phone=?,address=?,email=?,status=? WHERE customer_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, c.getCustomerName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getEmail());
            ps.setBoolean(5, c.isStatus());
            ps.setInt(6, c.getCustomerId());

            int row = ps.executeUpdate();

            con.close();

            return row > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // Giữ tương thích ngược - delete() thực chất luôn là xóa mềm
    public boolean delete(int id) {
        return softDelete(id);
    }

    // ==========================
    // Xóa mềm: chỉ ẩn khỏi hệ thống, có thể khôi phục lại
    // ==========================
    public boolean softDelete(int id) {
        return util.SoftDeleteHelper.setActive(
                "Customer", "customer_id", "status", id, false);
    }

    // ==========================
    // Khôi phục khách hàng đã bị vô hiệu hóa
    // ==========================
    public boolean restore(int id) {
        return util.SoftDeleteHelper.setActive(
                "Customer", "customer_id", "status", id, true);
    }

    public ArrayList<Customer> search(String keyword) {

        ArrayList<Customer> list = new ArrayList<>();

        String sql = "SELECT * FROM Customer "
                + "WHERE customer_name LIKE ? "
                + "OR phone LIKE ? "
                + "ORDER BY customer_id DESC";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Customer c = new Customer();

                c.setCustomerId(rs.getInt("customer_id"));
                c.setCustomerName(rs.getString("customer_name"));
                c.setPhone(rs.getString("phone"));
                c.setEmail(rs.getString("email"));
                c.setAddress(rs.getString("address"));
                c.setCreatedDate(rs.getTimestamp("created_date"));
                c.setStatus(rs.getBoolean("status"));

                list.add(c);

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }
}
