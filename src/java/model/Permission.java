package model;

/**
 * Đại diện cho 1 "quyền chi tiết" - tức 1 phần (module/chức năng) mà người
 * dùng có thể được cấp quyền truy cập, ví dụ: Quản lý khách hàng, Hóa đơn,...
 *
 * Thuộc tính "granted" chỉ dùng khi hiển thị màn hình Phân quyền: đánh dấu
 * quyền này đã được cấp cho vai trò đang xem hay chưa (không lưu vào cột
 * riêng trong bảng Permission mà được tính toán từ bảng RolePermission).
 */
public class Permission {

    private int permissionId;
    private String permissionCode;   // Mã quyền, dùng để so khớp trong code (VD: KHACHHANG)
    private String permissionName;   // Tên hiển thị (VD: Quản lý khách hàng)
    private String moduleGroup;      // Nhóm chức năng (VD: Nghiệp vụ, Hệ thống)
    private String description;      // Mô tả phần có thể truy cập
    private boolean granted;         // Vai trò đang xem có được cấp quyền này không

    public Permission() {
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getModuleGroup() {
        return moduleGroup;
    }

    public void setModuleGroup(String moduleGroup) {
        this.moduleGroup = moduleGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }

}
