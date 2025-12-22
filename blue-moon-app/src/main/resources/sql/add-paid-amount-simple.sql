-- Simple script to add paid_amount column
-- Run this in psql or pgAdmin

ALTER TABLE fee_collections 
ADD COLUMN IF NOT EXISTS paid_amount DECIMAL(15, 2) NOT NULL DEFAULT 0;

-- Update existing paid records
UPDATE fee_collections 
SET paid_amount = amount 
WHERE status = 'paid' AND paid_amount = 0;

