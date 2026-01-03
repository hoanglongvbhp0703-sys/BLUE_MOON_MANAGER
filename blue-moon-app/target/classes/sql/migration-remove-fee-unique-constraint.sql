-- Migration: Remove unique constraint on fee_collections to allow multiple fees for same month/year
-- This allows admin to add multiple fee collections for the same household/month/year
-- Date: 2026-01-02

-- Drop the unique index that prevents multiple fees for same household/month/year
DROP INDEX IF EXISTS idx_fee_collections_household_month_year;

-- Note: After this migration, multiple fee collections can exist for the same household/month/year
-- This allows:
-- 1. Auto-created fee when user registers personal info (e.g., 500,000 VNĐ)
-- 2. Admin-added fee for the same month/year (e.g., 600,000 VNĐ)
-- Both will appear in the unpaid fees list

