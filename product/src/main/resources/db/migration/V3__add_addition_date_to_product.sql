-- flyway:transactional=false

-- Add the column if it doesn't exist
ALTER TABLE product
    ADD COLUMN IF NOT EXISTS addition_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP;


-- Force truly random addition dates within the last 12 months
WITH random_dates AS (
    SELECT
        id,
        NOW()
            - (INTERVAL '1 day' * (FLOOR(random() * 365)))   -- up to 1 year ago
            - (INTERVAL '1 hour' * (FLOOR(random() * 24)))   -- random hour
            - (INTERVAL '1 minute' * (FLOOR(random() * 60))) -- random minute
            - (INTERVAL '1 second' * (FLOOR(random() * 60))) -- random second
            AS new_date
    FROM product
)
UPDATE product p
SET addition_date = r.new_date
    FROM random_dates r
WHERE p.id = r.id;