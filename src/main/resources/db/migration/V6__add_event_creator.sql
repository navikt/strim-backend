ALTER TABLE events
    ADD COLUMN created_by_id    TEXT NOT NULL DEFAULT 'unknown',
    ADD COLUMN created_by_name  TEXT NOT NULL DEFAULT 'Unknown',
    ADD COLUMN created_by_email TEXT NOT NULL DEFAULT 'unknown@unknown';
