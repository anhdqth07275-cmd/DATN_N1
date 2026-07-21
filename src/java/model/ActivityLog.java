package model;

import java.sql.Timestamp;

public class ActivityLog {

    private int logId;
    private int userId;
    private String username;
    private String fullName;
    private String roleName;
    private String actionType;   // DANG_NHAP | THEM | SUA | XOA | THU | CHI
    private String module;       // Tên module tác động: Khách hàng, Hóa đơn, Phiếu thu,...
    private String description;  // Nội dung chi tiết
    private Double amount;       // Số tiền (chỉ có ý nghĩa với THU / CHI), có thể null
    private Timestamp createdDate;

    public ActivityLog() {
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    // Nhãn hiển thị tiếng Việt cho actionType, dùng trong JSP
    public String getActionLabel() {

        if (actionType == null) {
            return "";
        }

        switch (actionType) {
            case "DANG_NHAP":
                return "Đăng nhập";
            case "THEM":
                return "Thêm mới";
            case "SUA":
                return "Cập nhật";
            case "XOA":
                return "Xóa";
            case "THU":
                return "Thu tiền";
            case "CHI":
                return "Chi tiền";
            default:
                return actionType;
        }
    }

    // Màu badge tương ứng cho từng loại hành động, dùng trong JSP
    public String getActionBadgeClass() {

        if (actionType == null) {
            return "bg-secondary";
        }

        switch (actionType) {
            case "DANG_NHAP":
                return "bg-secondary";
            case "THEM":
                return "bg-success";
            case "SUA":
                return "bg-warning";
            case "XOA":
                return "bg-danger";
            case "THU":
                return "bg-primary";
            case "CHI":
                return "bg-info";
            default:
                return "bg-secondary";
        }
    }

}
