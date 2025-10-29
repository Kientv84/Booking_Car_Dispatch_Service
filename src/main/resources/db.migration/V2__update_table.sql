-- V2__update_table.sql

-- ==========================
-- COMMENT for dispatch_entity
-- ==========================
COMMENT ON COLUMN dispatch_entity.id IS 'Id of dispatch entity';
COMMENT ON COLUMN dispatch_entity.booking_id IS 'Booking Id liên kết với đơn đặt xe';
COMMENT ON COLUMN dispatch_entity.latitude IS 'Vĩ độ của vị trí dispatch';
COMMENT ON COLUMN dispatch_entity.longitude IS 'Kinh độ của vị trí dispatch';
COMMENT ON COLUMN dispatch_entity.vehicle_id IS 'Id của phương tiện được điều phối';
COMMENT ON COLUMN dispatch_entity.driver_id IS 'Id của tài xế được điều phối';
COMMENT ON COLUMN dispatch_entity.status IS 'Trạng thái của dispatch';
COMMENT ON COLUMN dispatch_entity.created_at IS 'Thời điểm tạo bản ghi';
COMMENT ON COLUMN dispatch_entity.updated_at IS 'Thời điểm cập nhật bản ghi';

-- ==========================
-- COMMENT for dispatch_log_entity
-- ==========================
COMMENT ON COLUMN dispatch_log_entity.id IS 'Id của log dispatch';
COMMENT ON COLUMN dispatch_log_entity.dispatch_id IS 'Id tham chiếu đến dispatch_entity';
COMMENT ON COLUMN dispatch_log_entity.booking_id IS 'Booking Id liên kết với log';
COMMENT ON COLUMN dispatch_log_entity.vehicle_id IS 'Id của phương tiện trong log';
COMMENT ON COLUMN dispatch_log_entity.driver_id IS 'Id của tài xế trong log';
COMMENT ON COLUMN dispatch_log_entity.status IS 'Trạng thái của log';
COMMENT ON COLUMN dispatch_log_entity.cycle IS 'Chu kỳ điều phối (cycle)';


ALTER TABLE dispatch_log_entity
    ALTER COLUMN dispatch_id DROP NOT NULL;



