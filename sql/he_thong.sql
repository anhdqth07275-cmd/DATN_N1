/* ============================================================
   SCRIPT BỔ SUNG CHO PHẦN HỆ THỐNG
   (Người dùng / Phân quyền / Nhật ký hoạt động)

   Chạy script này trên database QuanLyTaiChinhCongNo_lan1
   (database hiện tại của project - xem util.DBConnect) SAU KHI
   đã có sẵn các bảng [User] và [Role].
   ============================================================ */

USE QuanLyTaiChinhCongNo_lan1;
GO

/* ------------------------------------------------------------
   1. BẢNG PERMISSION
   Danh sách "quyền chi tiết" - tức các phần/module mà người
   dùng có thể được cấp quyền truy cập.
   ------------------------------------------------------------ */
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Permission')
BEGIN
    CREATE TABLE Permission (
        permission_id     INT IDENTITY(1,1) PRIMARY KEY,
        permission_code   VARCHAR(50)   NOT NULL UNIQUE,
        permission_name   NVARCHAR(150) NOT NULL,
        module_group      NVARCHAR(50)  NOT NULL,
        description       NVARCHAR(300) NULL
    );
END
GO

/* ------------------------------------------------------------
   2. BẢNG ROLEPERMISSION
   Vai trò nào được cấp quyền (phần) nào.
   ------------------------------------------------------------ */
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'RolePermission')
BEGIN
    CREATE TABLE RolePermission (
        role_id        INT NOT NULL,
        permission_id  INT NOT NULL,
        CONSTRAINT PK_RolePermission PRIMARY KEY (role_id, permission_id),
        CONSTRAINT FK_RolePermission_Role
            FOREIGN KEY (role_id) REFERENCES Role(role_id),
        CONSTRAINT FK_RolePermission_Permission
            FOREIGN KEY (permission_id) REFERENCES Permission(permission_id)
                ON DELETE CASCADE
    );
END
GO

/* ------------------------------------------------------------
   3. BẢNG ACTIVITYLOG
   Nhật ký hoạt động: đăng nhập, thêm, sửa, xóa, thu, chi,...
   ------------------------------------------------------------ */
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'ActivityLog')
BEGIN
    CREATE TABLE ActivityLog (
        log_id         INT IDENTITY(1,1) PRIMARY KEY,
        user_id        INT           NOT NULL,
        username       VARCHAR(50)   NOT NULL,
        full_name      NVARCHAR(100) NULL,
        role_name      NVARCHAR(50)  NULL,
        action_type    VARCHAR(20)   NOT NULL,   -- DANG_NHAP | THEM | SUA | XOA | THU | CHI
        module         NVARCHAR(50)  NOT NULL,   -- Khách hàng, Hóa đơn, Phiếu thu,...
        description    NVARCHAR(500) NULL,
        amount         DECIMAL(18,0) NULL,       -- chỉ có ý nghĩa với THU / CHI
        created_date   DATETIME      NOT NULL DEFAULT GETDATE()
    );

    CREATE INDEX IX_ActivityLog_CreatedDate ON ActivityLog(created_date);
END
GO

/* ------------------------------------------------------------
   4. SEED DỮ LIỆU QUYỀN CHI TIẾT (PHẦN CÓ THỂ TRUY CẬP)
   ------------------------------------------------------------ */
IF NOT EXISTS (SELECT * FROM Permission WHERE permission_code = 'KHACHHANG')
BEGIN
    INSERT INTO Permission (permission_code, permission_name, module_group, description) VALUES
    ('KHACHHANG', N'Quản lý khách hàng', N'Nghiệp vụ', N'Xem, thêm, sửa, xóa thông tin khách hàng'),
    ('HOADON',    N'Quản lý hóa đơn',    N'Nghiệp vụ', N'Lập, sửa, xóa và xem chi tiết hóa đơn'),
    ('CONGNO',    N'Quản lý công nợ',    N'Nghiệp vụ', N'Theo dõi công nợ, gia hạn công nợ của khách hàng'),
    ('PHIEUTHU',  N'Thu tiền',           N'Nghiệp vụ', N'Lập phiếu thu, ghi nhận thanh toán của khách hàng'),
    ('PHIEUCHI',  N'Chi tiền',           N'Nghiệp vụ', N'Lập phiếu chi các khoản chi phí'),
    ('BAOCAO',    N'Báo cáo',            N'Nghiệp vụ', N'Xem báo cáo doanh thu, thu chi, công nợ'),
    ('HETHONG',   N'Hệ thống',           N'Hệ thống', N'Truy cập khu vực quản trị hệ thống (Người dùng, Phân quyền, Nhật ký hoạt động)');
END
GO

/* ------------------------------------------------------------
   5. SEED PHÂN QUYỀN MẶC ĐỊNH THEO VAI TRÒ
   (role_id tham chiếu theo dữ liệu Role có sẵn của project:
    1 Khách hàng | 2 NV kế toán | 3 NV kinh doanh
    4 Quản trị viên | 5 Giám đốc/Quản lý)

   Có thể chỉnh lại tùy ý qua màn hình "Phân quyền" sau khi cài đặt.
   ------------------------------------------------------------ */

-- Quản trị viên (role_id = 4): toàn quyền, bao gồm cả Hệ thống
IF NOT EXISTS (SELECT * FROM RolePermission WHERE role_id = 4)
BEGIN
    INSERT INTO RolePermission (role_id, permission_id)
    SELECT 4, permission_id FROM Permission;
END
GO

-- Nhân viên kế toán (role_id = 2): khách hàng, hóa đơn, công nợ,
-- phiếu thu, phiếu chi, báo cáo (không có Hệ thống)
IF NOT EXISTS (SELECT * FROM RolePermission WHERE role_id = 2)
BEGIN
    INSERT INTO RolePermission (role_id, permission_id)
    SELECT 2, permission_id FROM Permission
    WHERE permission_code IN
        ('KHACHHANG','HOADON','CONGNO','PHIEUTHU','PHIEUCHI','BAOCAO');
END
GO

-- Nhân viên kinh doanh (role_id = 3): khách hàng, hóa đơn, công nợ, báo cáo
-- (không thu/chi tiền, không vào Hệ thống)
IF NOT EXISTS (SELECT * FROM RolePermission WHERE role_id = 3)
BEGIN
    INSERT INTO RolePermission (role_id, permission_id)
    SELECT 3, permission_id FROM Permission
    WHERE permission_code IN ('KHACHHANG','HOADON','CONGNO','BAOCAO');
END
GO

-- Giám đốc / Quản lý (role_id = 5): xem toàn bộ nghiệp vụ, không quản trị Hệ thống
IF NOT EXISTS (SELECT * FROM RolePermission WHERE role_id = 5)
BEGIN
    INSERT INTO RolePermission (role_id, permission_id)
    SELECT 5, permission_id FROM Permission
    WHERE permission_code IN
        ('KHACHHANG','HOADON','CONGNO','PHIEUTHU','PHIEUCHI','BAOCAO');
END
GO

-- Khách hàng (role_id = 1): không có quyền truy cập module quản trị/nghiệp vụ nội bộ
-- (tài khoản khách hàng không thuộc phạm vi phần Hệ thống này)

PRINT 'Đã tạo xong bảng Permission, RolePermission, ActivityLog và seed dữ liệu mặc định.';
