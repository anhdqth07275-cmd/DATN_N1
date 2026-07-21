package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Role;
import util.DBConnect;

public class RoleDAO {

    public ArrayList<Role> getAllRoles() {

        ArrayList<Role> list = new ArrayList<>();

        String sql = "SELECT role_id, role_name FROM Role ORDER BY role_id";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Role r = new Role();

                r.setRoleId(rs.getInt("role_id"));
                r.setRoleName(rs.getString("role_name"));

                list.add(r);

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

}
