USE QuanLyTaiChinhCongNo_lan1;
GO

/* ============================================================
   QUYỀN "XEM TOÀN BỘ CÔNG NỢ" (CONGNO_XEMTATCA)

   Mặc định, mỗi nhân viên (NV kế toán, NV kinh doanh) chỉ nhìn
   thấy công nợ của các hóa đơn do CHÍNH MÌNH lập (Invoice.user_id)
   - áp dụng cho bảng Công nợ, cảnh báo sắp đến hạn/quá hạn ở
   Trang chủ và chuông thông báo.

   Ai có quyền CONGNO_XEMTATCA thì thấy TOÀN BỘ công nợ của công ty,
   không bị giới hạn theo người lập hóa đơn.
   ============================================================ */

IF NOT EXISTS (SELECT * FROM Permission WHERE permission_code = 'CONGNO_XEMTATCA')
BEGIN
    INSERT INTO Permission (permission_code, permission_name, module_group, description) VALUES
    ('CONGNO_XEMTATCA', N'👁 Xem toàn bộ công nợ', N'Công nợ — chi tiết quyền',
     N'Xem công nợ/cảnh báo của tất cả nhân viên, không chỉ hóa đơn do mình lập');
END
GO

-- Quản trị viên (role_id = 4): mặc định có quyền xem toàn bộ
IF NOT EXISTS (SELECT * FROM RolePermission rp
               JOIN Permission p ON rp.permission_id = p.permission_id
               WHERE rp.role_id = 4 AND p.permission_code = 'CONGNO_XEMTATCA')
BEGIN
    INSERT INTO RolePermission (role_id, permission_id)
    SELECT 4, permission_id FROM Permission WHERE permission_code = 'CONGNO_XEMTATCA';
END
GO

-- Giám đốc / Quản lý (role_id = 5): mặc định có quyền xem toàn bộ
IF NOT EXISTS (SELECT * FROM RolePermission rp
               JOIN Permission p ON rp.permission_id = p.permission_id
               WHERE rp.role_id = 5 AND p.permission_code = 'CONGNO_XEMTATCA')
BEGIN
    INSERT INTO RolePermission (role_id, permission_id)
    SELECT 5, permission_id FROM Permission WHERE permission_code = 'CONGNO_XEMTATCA';
END
GO

-- NV kế toán (role_id = 2) và NV kinh doanh (role_id = 3): KHÔNG cấp
-- mặc định - chỉ xem công nợ của hóa đơn do chính mình lập. Có thể
-- bật thêm qua màn hình "Phân quyền" nếu công ty muốn.

PRINT N'Đã thêm quyền CONGNO_XEMTATCA và gán mặc định cho Quản trị viên/Giám đốc.';
