/* ============================================================
   GỠ BỎ QUYỀN "XÓA CỨNG" (XOACUNG) KHỎI HỆ THỐNG PHÂN QUYỀN
   ------------------------------------------------------------
   Dùng cho các cơ sở dữ liệu ĐÃ chạy script
   upgrade_xoa_phanquyen_chitiet.sql trước đây (nên đã có sẵn các
   permission_code dạng *_XOACUNG). Hệ thống không còn hỗ trợ chức
   năng xóa cứng (xóa vĩnh viễn) - chỉ còn Thêm / Sửa / Xóa mềm.

   Script này AN TOÀN để chạy nhiều lần (idempotent): nếu không còn
   permission XOACUNG nào thì không có gì bị ảnh hưởng.
   ============================================================ */

USE QuanLyTaiChinhCongNo_lan1;
GO

-- 1. Xóa các bản ghi RolePermission đang trỏ tới quyền *_XOACUNG
DELETE rp
FROM RolePermission rp
JOIN Permission p ON rp.permission_id = p.permission_id
WHERE p.permission_code LIKE '%\_XOACUNG' ESCAPE '\';
GO

-- 2. Xóa chính các permission *_XOACUNG khỏi bảng Permission
DELETE FROM Permission
WHERE permission_code LIKE '%\_XOACUNG' ESCAPE '\';
GO

PRINT N'Đã gỡ bỏ toàn bộ quyền "Xóa cứng" (XOACUNG) khỏi hệ thống phân quyền.';
