USE QuanLyTaiChinhCongNo_lan1;
GO

/* ============================================================
   1. THÊM CỘT is_active CHO CÁC BẢNG CHƯA CÓ CƠ CHẾ ẨN/HIỆN
      (Customer đã có sẵn cột "status" đóng đúng vai trò này rồi,
       không cần thêm cột mới cho Customer)
   ============================================================ */
IF NOT EXISTS (SELECT * FROM sys.columns
               WHERE object_id = OBJECT_ID('Invoice') AND name = 'is_active')
BEGIN
    ALTER TABLE Invoice ADD is_active BIT NOT NULL DEFAULT 1;
END
GO

IF NOT EXISTS (SELECT * FROM sys.columns
               WHERE object_id = OBJECT_ID('Receipt') AND name = 'is_active')
BEGIN
    ALTER TABLE Receipt ADD is_active BIT NOT NULL DEFAULT 1;
END
GO

IF NOT EXISTS (SELECT * FROM sys.columns
               WHERE object_id = OBJECT_ID('Expense_Voucher') AND name = 'is_active')
BEGIN
    ALTER TABLE Expense_Voucher ADD is_active BIT NOT NULL DEFAULT 1;
END
GO

/* ============================================================
   2. BẢNG YÊU CẦU "XIN QUYỀN VÔ HIỆU HÓA" CỦA NHÂN VIÊN
   ============================================================ */
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'RequestDisable')
BEGIN
    CREATE TABLE RequestDisable (
        request_id      INT IDENTITY(1,1) PRIMARY KEY,
        module_code     VARCHAR(30)   NOT NULL,   -- KHACHHANG | HOADON | PHIEUTHU | PHIEUCHI
        entity_id       INT           NOT NULL,
        entity_label    NVARCHAR(200) NULL,
        requested_by    INT           NOT NULL,
        requested_date  DATETIME      NOT NULL DEFAULT GETDATE(),
        review_status   VARCHAR(20)   NOT NULL DEFAULT 'Pending', -- Pending|Approved|Rejected
        reviewed_by     INT           NULL,
        reviewed_date   DATETIME      NULL,
        CONSTRAINT FK_ReqDisable_ReqBy FOREIGN KEY(requested_by) REFERENCES [User](user_id),
        CONSTRAINT FK_ReqDisable_RevBy FOREIGN KEY(reviewed_by) REFERENCES [User](user_id)
    );

    CREATE INDEX IX_ReqDisable_Module ON RequestDisable(module_code, review_status);
END
GO

/* ============================================================
   3. SEED QUYỀN CHI TIẾT THEO HÀNH ĐỘNG CHO TỪNG MODULE NGHIỆP VỤ
      (Thêm / Sửa / Xóa mềm / Xóa cứng) - mỗi module 1 module_group
      riêng để màn hình Phân quyền tự hiển thị thành từng khối rõ ràng.
   ============================================================ */
IF NOT EXISTS (SELECT * FROM Permission WHERE permission_code = 'KHACHHANG_THEM')
BEGIN
    INSERT INTO Permission (permission_code, permission_name, module_group, description) VALUES
    ('KHACHHANG_THEM',    N'➕ Thêm khách hàng',          N'Khách hàng — chi tiết quyền', N'Thêm mới khách hàng'),
    ('KHACHHANG_SUA',     N'✏️ Sửa khách hàng',           N'Khách hàng — chi tiết quyền', N'Chỉnh sửa thông tin khách hàng'),
    ('KHACHHANG_XOAMEM',  N'🚫 Vô hiệu hóa (xóa mềm)',    N'Khách hàng — chi tiết quyền', N'Ẩn khỏi hệ thống, có thể khôi phục lại'),
    ('KHACHHANG_XOACUNG', N'🗑️ Xóa vĩnh viễn (xóa cứng)', N'Khách hàng — chi tiết quyền', N'Xóa hẳn khỏi cơ sở dữ liệu, KHÔNG thể khôi phục');
END
GO

IF NOT EXISTS (SELECT * FROM Permission WHERE permission_code = 'HOADON_THEM')
BEGIN
    INSERT INTO Permission (permission_code, permission_name, module_group, description) VALUES
    ('HOADON_THEM',    N'➕ Thêm hóa đơn',              N'Hóa đơn — chi tiết quyền', N'Lập hóa đơn mới'),
    ('HOADON_SUA',     N'✏️ Sửa hóa đơn',               N'Hóa đơn — chi tiết quyền', N'Chỉnh sửa hóa đơn'),
    ('HOADON_XOAMEM',  N'🚫 Vô hiệu hóa (xóa mềm)',     N'Hóa đơn — chi tiết quyền', N'Ẩn khỏi hệ thống, có thể khôi phục lại'),
    ('HOADON_XOACUNG', N'🗑️ Xóa vĩnh viễn (xóa cứng)',  N'Hóa đơn — chi tiết quyền', N'Xóa hẳn khỏi cơ sở dữ liệu, KHÔNG thể khôi phục');
END
GO

IF NOT EXISTS (SELECT * FROM Permission WHERE permission_code = 'PHIEUTHU_THEM')
BEGIN
    INSERT INTO Permission (permission_code, permission_name, module_group, description) VALUES
    ('PHIEUTHU_THEM',    N'➕ Lập phiếu thu',             N'Thu tiền — chi tiết quyền', N'Lập phiếu thu mới'),
    ('PHIEUTHU_SUA',     N'✏️ Sửa phiếu thu',             N'Thu tiền — chi tiết quyền', N'Chỉnh sửa phiếu thu'),
    ('PHIEUTHU_XOAMEM',  N'🚫 Vô hiệu hóa (xóa mềm)',     N'Thu tiền — chi tiết quyền', N'Ẩn khỏi hệ thống, có thể khôi phục lại'),
    ('PHIEUTHU_XOACUNG', N'🗑️ Xóa vĩnh viễn (xóa cứng)',  N'Thu tiền — chi tiết quyền', N'Xóa hẳn khỏi cơ sở dữ liệu, KHÔNG thể khôi phục');
END
GO

IF NOT EXISTS (SELECT * FROM Permission WHERE permission_code = 'PHIEUCHI_THEM')
BEGIN
    INSERT INTO Permission (permission_code, permission_name, module_group, description) VALUES
    ('PHIEUCHI_THEM',    N'➕ Lập phiếu chi',             N'Chi tiền — chi tiết quyền', N'Lập phiếu chi mới'),
    ('PHIEUCHI_SUA',     N'✏️ Sửa phiếu chi',             N'Chi tiền — chi tiết quyền', N'Chỉnh sửa phiếu chi'),
    ('PHIEUCHI_XOAMEM',  N'🚫 Vô hiệu hóa (xóa mềm)',     N'Chi tiền — chi tiết quyền', N'Ẩn khỏi hệ thống, có thể khôi phục lại'),
    ('PHIEUCHI_XOACUNG', N'🗑️ Xóa vĩnh viễn (xóa cứng)',  N'Chi tiền — chi tiết quyền', N'Xóa hẳn khỏi cơ sở dữ liệu, KHÔNG thể khôi phục');
END
GO

/* ============================================================
   4. SEED PHÂN QUYỀN MẶC ĐỊNH THEO ĐÚNG QUY TẮC NGHIỆP VỤ:
      - Quản trị viên (role_id=4): TẤT CẢ (thêm/sửa/xóa mềm/xóa cứng)
      - Giám đốc (role_id=5)     : thêm/sửa/xóa mềm — KHÔNG xóa cứng
      - NV kế toán (role_id=2)   : chỉ thêm/sửa — không có quyền xóa nào
                                    (dùng nút "Xin quyền vô hiệu hóa")
      - NV kinh doanh (role_id=3): chỉ thêm/sửa Khách hàng + Hóa đơn
   ============================================================ */

-- Quản trị viên: toàn bộ quyền hành động chi tiết vừa tạo
IF NOT EXISTS (SELECT * FROM RolePermission rp
               JOIN Permission p ON rp.permission_id = p.permission_id
               WHERE rp.role_id = 4 AND p.permission_code = 'KHACHHANG_XOACUNG')
BEGIN
    INSERT INTO RolePermission (role_id, permission_id)
    SELECT 4, permission_id FROM Permission
    WHERE permission_code LIKE '%\_THEM' ESCAPE '\'
       OR permission_code LIKE '%\_SUA' ESCAPE '\'
       OR permission_code LIKE '%\_XOAMEM' ESCAPE '\'
       OR permission_code LIKE '%\_XOACUNG' ESCAPE '\';
END
GO

-- Giám đốc: thêm/sửa/xóa mềm - không xóa cứng
IF NOT EXISTS (SELECT * FROM RolePermission rp
               JOIN Permission p ON rp.permission_id = p.permission_id
               WHERE rp.role_id = 5 AND p.permission_code = 'KHACHHANG_XOAMEM')
BEGIN
    INSERT INTO RolePermission (role_id, permission_id)
    SELECT 5, permission_id FROM Permission
    WHERE permission_code LIKE '%\_THEM' ESCAPE '\'
       OR permission_code LIKE '%\_SUA' ESCAPE '\'
       OR permission_code LIKE '%\_XOAMEM' ESCAPE '\';
END
GO

-- Nhân viên kế toán: chỉ thêm/sửa ở cả 4 module
IF NOT EXISTS (SELECT * FROM RolePermission rp
               JOIN Permission p ON rp.permission_id = p.permission_id
               WHERE rp.role_id = 2 AND p.permission_code = 'PHIEUTHU_THEM')
BEGIN
    INSERT INTO RolePermission (role_id, permission_id)
    SELECT 2, permission_id FROM Permission
    WHERE permission_code LIKE '%\_THEM' ESCAPE '\'
       OR permission_code LIKE '%\_SUA' ESCAPE '\';
END
GO

-- Nhân viên kinh doanh: chỉ thêm/sửa Khách hàng + Hóa đơn
IF NOT EXISTS (SELECT * FROM RolePermission rp
               JOIN Permission p ON rp.permission_id = p.permission_id
               WHERE rp.role_id = 3 AND p.permission_code = 'HOADON_THEM')
BEGIN
    INSERT INTO RolePermission (role_id, permission_id)
    SELECT 3, permission_id FROM Permission
    WHERE permission_code IN
        ('KHACHHANG_THEM','KHACHHANG_SUA','HOADON_THEM','HOADON_SUA');
END
GO

PRINT N'Đã nâng cấp: cơ chế xóa mềm/xóa cứng + phân quyền chi tiết theo hành động.';
