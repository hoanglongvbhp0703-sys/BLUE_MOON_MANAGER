-- Add paid_amount column to fee_collections table
-- Run this script in your PostgreSQL database

-- Check if column exists, if not add it
DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'fee_collections' 
        AND column_name = 'paid_amount'
    ) THEN
        ALTER TABLE fee_collections 
        ADD COLUMN paid_amount DECIMAL(15, 2) NOT NULL DEFAULT 0;
        
        RAISE NOTICE 'Column paid_amount added successfully';
    ELSE
        RAISE NOTICE 'Column paid_amount already exists';
    END IF;
END $$;

-- Update existing records: if status is 'paid', set paid_amount = amount
UPDATE fee_collections 
SET paid_amount = amount 
WHERE status = 'paid' AND (paid_amount IS NULL OR paid_amount = 0);

