package model;

import java.util.Date;

/**
 * Yêu cầu "Xin quyền vô hiệu hóa" — dùng khi Nhân viên (không có quyền xóa)
 * muốn đề xuất Admin/Giám đốc cân nhắc vô hiệu hóa 1 bản ghi (khách hàng,
 * hóa đơn, phiếu thu, phiếu chi...). Yêu cầu này chỉ mang tính ĐÁNH DẤU —
 * bản ghi KHÔNG bị ẩn ngay, phải chờ Admin/Giám đốc tự quyết định.
 */
public class RequestDisable {

    private int requestId;
    private String moduleCode;     // KHACHHANG | HOADON | PHIEUTHU | PHIEUCHI
    private int entityId;
    private String entityLabel;    // tên hiển thị (vd tên khách hàng, mã hóa đơn)

    private int requestedBy;
    private String requestedByName;
    private Date requestedDate;

    private String reviewStatus;   // Pending | Approved | Rejected
    private Integer reviewedBy;
    private Date reviewedDate;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getEntityLabel() {
        return entityLabel;
    }

    public void setEntityLabel(String entityLabel) {
        this.entityLabel = entityLabel;
    }

    public int getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(int requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getRequestedByName() {
        return requestedByName;
    }

    public void setRequestedByName(String requestedByName) {
        this.requestedByName = requestedByName;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Integer getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Integer reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Date getReviewedDate() {
        return reviewedDate;
    }

    public void setReviewedDate(Date reviewedDate) {
        this.reviewedDate = reviewedDate;
    }

}
