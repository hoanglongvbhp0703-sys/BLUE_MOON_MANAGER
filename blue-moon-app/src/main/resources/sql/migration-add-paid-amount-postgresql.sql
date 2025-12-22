-- Migration: Add paid_amount column to fee_collections table
-- Run this script if your database already exists

-- Add paid_amount column if it doesn't exist
DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'fee_collections' AND column_name = 'paid_amount'
    ) THEN
        ALTER TABLE fee_collections 
        ADD COLUMN paid_amount DECIMAL(15, 2) NOT NULL DEFAULT 0;
        
        -- Update existing records: if status is 'paid', set paid_amount = amount
        UPDATE fee_collections 
        SET paid_amount = amount 
        WHERE status = 'paid' AND paid_amount = 0;
    END IF;
END $$;

