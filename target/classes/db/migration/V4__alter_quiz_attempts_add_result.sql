-- Add result status and duration to quiz_attempts
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'quiz_attempts' AND column_name = 'status'
    ) THEN
        ALTER TABLE quiz_attempts ADD COLUMN status VARCHAR(32);
    END IF;
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'quiz_attempts' AND column_name = 'duration_seconds'
    ) THEN
        ALTER TABLE quiz_attempts ADD COLUMN duration_seconds INTEGER;
    END IF;
END $$;

