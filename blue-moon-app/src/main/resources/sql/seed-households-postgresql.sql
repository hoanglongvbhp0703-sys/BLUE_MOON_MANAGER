-- Script tạo 1000 hộ dân mẫu cho chung cư Blue Moon
-- Chạy file này sau khi đã chạy schema-household-postgresql.sql

-- Tạo 1000 căn hộ (10 tòa, mỗi tòa 10 tầng, mỗi tầng 10 phòng)
INSERT INTO apartments (building_number, floor_number, room_number, apartment_code, area, number_of_rooms, status)
SELECT 
    'A' || (tower_num::text) as building_number,
    floor_num as floor_number,
    LPAD(room_num::text, 2, '0') as room_number,
    'A' || tower_num || '-' || LPAD(floor_num::text, 2, '0') || LPAD(room_num::text, 2, '0') as apartment_code,
    (60 + (random() * 40)::int)::decimal(10,2) as area,
    (2 + (random() * 2)::int) as number_of_rooms,
    CASE 
        WHEN random() < 0.95 THEN 'occupied'
        ELSE 'available'
    END as status
FROM generate_series(1, 10) as tower_num
CROSS JOIN generate_series(1, 10) as floor_num
CROSS JOIN generate_series(1, 10) as room_num
ON CONFLICT (apartment_code) DO NOTHING;

-- Tạo 1000 hộ dân
WITH last_names AS (
    SELECT unnest(ARRAY[
        'Nguyễn', 'Trần', 'Lê', 'Phạm', 'Hoàng', 'Huỳnh', 'Phan', 'Vũ', 'Võ', 'Đặng',
        'Bùi', 'Đỗ', 'Hồ', 'Ngô', 'Dương', 'Lý', 'Đinh', 'Đào', 'Tạ', 'Lương',
        'Trương', 'Lâm', 'Vương', 'Tôn', 'Hà', 'Chu', 'Mai', 'Đỗ', 'Cao', 'Lưu'
    ]) as last_name
),
middle_names AS (
    SELECT unnest(ARRAY[
        'Văn', 'Thị', 'Đức', 'Minh', 'Thanh', 'Hữu', 'Công', 'Quang', 'Đình', 'Xuân',
        'Hoàng', 'Thành', 'Đăng', 'Tuấn', 'Hải', 'Nam', 'Long', 'Phong', 'Sơn', 'Việt'
    ]) as middle_name
),
given_names AS (
    SELECT unnest(ARRAY[
        'An', 'Bình', 'Chi', 'Dũng', 'Giang', 'Hoa', 'Hùng', 'Lan', 'Mai', 'Nam',
        'Phong', 'Quang', 'Sơn', 'Thảo', 'Tuấn', 'Uyên', 'Việt', 'Yến', 'Anh', 'Bảo',
        'Cường', 'Đức', 'Hạnh', 'Khang', 'Linh', 'My', 'Nga', 'Phương', 'Thành', 'Thu'
    ]) as first_name
),
household_data AS (
    SELECT DISTINCT ON (a.id)
        a.id as apartment_id,
        a.apartment_code || '-HH' as household_code,
        last_name || ' ' || middle_name || ' ' || first_name as owner_name,
        LPAD((100000000 + (random() * 899999999)::bigint)::text, 12, '0') as owner_id_card,
        '0' || LPAD((900000000 + (random() * 99999999)::int)::text, 9, '0') as owner_phone,
        LOWER(REPLACE(last_name || middle_name || first_name, ' ', '')) || 
        (random() * 10000)::int || '@gmail.com' as owner_email,
        (2 + (random() * 2)::int) as number_of_members,
        CURRENT_DATE - ((random() * 1825)::int || ' days')::INTERVAL as registration_date,
        'active' as status
    FROM apartments a
    CROSS JOIN last_names
    CROSS JOIN middle_names
    CROSS JOIN given_names
    WHERE a.status = 'occupied'
    ORDER BY a.id, random()
    LIMIT 1000
)
INSERT INTO households (apartment_id, household_code, owner_name, owner_id_card, owner_phone, owner_email, number_of_members, registration_date, status)
SELECT * FROM household_data
ON CONFLICT (household_code) DO NOTHING;

-- Tạo nhân khẩu cho mỗi hộ (chủ hộ)
INSERT INTO residents (household_id, full_name, id_card, date_of_birth, gender, relationship, phone, occupation, status)
SELECT 
    h.id as household_id,
    h.owner_name as full_name,
    h.owner_id_card as id_card,
    CURRENT_DATE - INTERVAL '25 years' - ((random() * 365 * 40)::int || ' days')::INTERVAL as date_of_birth,
    CASE WHEN random() < 0.5 THEN 'male' ELSE 'female' END as gender,
    'Chủ hộ' as relationship,
    h.owner_phone as phone,
    CASE (random() * 10)::int
        WHEN 0 THEN 'Kinh doanh'
        WHEN 1 THEN 'Công nhân'
        WHEN 2 THEN 'Nhân viên văn phòng'
        WHEN 3 THEN 'Giáo viên'
        WHEN 4 THEN 'Bác sĩ'
        WHEN 5 THEN 'Kỹ sư'
        WHEN 6 THEN 'Nội trợ'
        WHEN 7 THEN 'Hưu trí'
        WHEN 8 THEN 'Sinh viên'
        ELSE 'Tự do'
    END as occupation,
    'active' as status
FROM households h;

-- Tạo các thành viên khác trong hộ
WITH household_expansion AS (
    SELECT 
        h.id as household_id,
        h.owner_name,
        h.number_of_members,
        generate_series(2, h.number_of_members) as member_index
    FROM households h
    WHERE h.number_of_members > 1
),
member_names AS (
    SELECT 
        he.household_id,
        he.member_index,
        CASE 
            WHEN he.owner_name LIKE '%Thị%' THEN 
                SPLIT_PART(he.owner_name, ' ', 1) || ' ' || 
                CASE WHEN random() < 0.5 THEN 'Văn' ELSE 'Đức' END || ' ' ||
                (ARRAY['An', 'Bình', 'Dũng', 'Hùng', 'Minh', 'Quang', 'Sơn', 'Tuấn', 'Long', 'Nam', 'Phong', 'Việt', 'Đức', 'Thành', 'Bảo', 'Cường'])[(random() * 16)::int + 1]
            ELSE 
                SPLIT_PART(he.owner_name, ' ', 1) || ' ' || 
                'Thị' || ' ' ||
                (ARRAY['An', 'Chi', 'Hoa', 'Lan', 'Mai', 'Thảo', 'Uyên', 'Yến', 'Giang', 'Linh', 'Nga', 'Phương', 'Hương', 'Thu', 'Hạnh', 'My'])[(random() * 16)::int + 1]
        END as full_name,
        CASE 
            WHEN he.member_index = 2 THEN 
                CASE WHEN random() < 0.5 THEN 'Vợ' ELSE 'Chồng' END
            WHEN random() < 0.5 THEN 'Con trai'
            ELSE 'Con gái'
        END as relationship,
        CASE WHEN random() < 0.5 THEN 'male' ELSE 'female' END as gender,
        CURRENT_DATE - INTERVAL '18 years' - ((random() * 365 * 50)::int || ' days')::INTERVAL as date_of_birth
    FROM household_expansion he
)
INSERT INTO residents (household_id, full_name, date_of_birth, gender, relationship, occupation, status)
SELECT 
    mn.household_id,
    mn.full_name,
    mn.date_of_birth,
    mn.gender,
    mn.relationship,
    CASE (random() * 10)::int
        WHEN 0 THEN 'Kinh doanh'
        WHEN 1 THEN 'Công nhân'
        WHEN 2 THEN 'Nhân viên văn phòng'
        WHEN 3 THEN 'Giáo viên'
        WHEN 4 THEN 'Nội trợ'
        WHEN 5 THEN 'Học sinh'
        WHEN 6 THEN 'Sinh viên'
        WHEN 7 THEN 'Kỹ sư'
        WHEN 8 THEN 'Bác sĩ'
        ELSE 'Tự do'
    END as occupation,
    'active' as status
FROM member_names mn
WHERE mn.full_name IS NOT NULL AND mn.full_name != '';

-- Cập nhật số thành viên thực tế
UPDATE households h
SET number_of_members = (
    SELECT COUNT(*) 
    FROM residents r 
    WHERE r.household_id = h.id AND r.status = 'active'
);

-- Hiển thị kết quả
DO $$
DECLARE
    total_residents INT;
    total_households INT;
    avg_members NUMERIC;
BEGIN
    SELECT COUNT(*) INTO total_residents FROM residents WHERE status = 'active';
    SELECT COUNT(*) INTO total_households FROM households;
    SELECT AVG(number_of_members) INTO avg_members FROM households;
    
    RAISE NOTICE '========================================';
    RAISE NOTICE 'Kết quả:';
    RAISE NOTICE 'Tổng số hộ dân: %', total_households;
    RAISE NOTICE 'Tổng số nhân khẩu: %', total_residents;
    RAISE NOTICE 'Trung bình: %.2f nhân khẩu/hộ', avg_members;
    RAISE NOTICE '========================================';
END $$;

