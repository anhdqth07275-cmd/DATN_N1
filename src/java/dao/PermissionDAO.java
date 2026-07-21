package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import model.Permission;
import util.DBConnect;

public class PermissionDAO {

    // ==========================
    // Toàn bộ quyền chi tiết (phần có thể truy cập) đang có trong hệ thống
    // ==========================
    public ArrayList<Permission> getAllPermissions() {

        ArrayList<Permission> list = new ArrayList<>();

        String sql = "SELECT * FROM Permission ORDER BY module_group, permission_id";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(mapRow(rs));

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

    // ==========================
    // Toàn bộ quyền chi tiết, kèm cờ granted = vai trò roleId đã được cấp
    // quyền đó hay chưa -> dùng để hiển thị màn hình Phân quyền
    // ==========================
    public ArrayList<Permission> getPermissionsByRole(int roleId) {

        ArrayList<Permission> list = getAllPermissions();

        HashSet<Integer> grantedIds = getGrantedPermissionIds(roleId);

        for (Permission p : list) {

            p.setGranted(grantedIds.contains(p.getPermissionId()));

        }

        return list;

    }

    private HashSet<Integer> getGrantedPermissionIds(int roleId) {

        HashSet<Integer> set = new HashSet<>();

        String sql = "SELECT permission_id FROM RolePermission WHERE role_id=?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, roleId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                set.add(rs.getInt("permission_id"));

            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return set;

    }

    // ==========================
    // Kiểm tra 1 vai trò có quyền truy cập 1 module (theo mã quyền) hay không
    // Dùng ở AuthFilter để chặn truy cập các module nghiệp vụ.
    // ==========================
    public boolean hasPermission(int roleId, String permissionCode) {

        String sql = "SELECT 1 "
                + "FROM RolePermission rp "
                + "JOIN Permission p ON rp.permission_id = p.permission_id "
                + "WHERE rp.role_id = ? AND p.permission_code = ?";

        try {

            Connection con = DBConnect.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, roleId);
            ps.setString(2, permissionCode);

            ResultSet rs = ps.executeQuery();

            boolean granted = rs.next();

            con.close();

            return granted;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // Lưu lại toàn bộ quyền được cấp cho 1 vai trò (QTV chọn qua checkbox)
    // Cách làm: xóa hết quyền cũ của vai trò rồi ghi lại theo danh sách mới.
    // ==========================
    public boolean saveRolePermissions(int roleId, int[] permissionIds) {

        String deleteSql = "DELETE FROM RolePermission WHERE role_id=?";
        String insertSql = "INSERT INTO RolePermission(role_id, permission_id) VALUES(?,?)";

        try {

            Connection con = DBConnect.getConnection();

            con.setAutoCommit(false);

            PreparedStatement psDelete = con.prepareStatement(deleteSql);

            psDelete.setInt(1, roleId);
            psDelete.executeUpdate();

            if (permissionIds != null) {

                PreparedStatement psInsert = con.prepareStatement(insertSql);

                for (int permissionId : permissionIds) {

                    psInsert.setInt(1, roleId);
                    psInsert.setInt(2, permissionId);
                    psInsert.addBatch();

                }

                psInsert.executeBatch();

            }

            con.commit();

            con.close();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    private Permission mapRow(ResultSet rs) throws Exception {

        Permission p = new Permission();

        p.setPermissionId(rs.getInt("permission_id"));
        p.setPermissionCode(rs.getString("permission_code"));
        p.setPermissionName(rs.getString("permission_name"));
        p.setModuleGroup(rs.getString("module_group"));
        p.setDescription(rs.getString("description"));

        return p;

    }

}
