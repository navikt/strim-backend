CREATE TABLE IF NOT EXISTS event_participants (
    id UUID PRIMARY KEY,
    event_id UUID NOT NULL,
    email TEXT NOT NULL,
    name TEXT NOT NULL,

    CONSTRAINT fk_event_participants_event
        FOREIGN KEY (event_id)
        REFERENCES events(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_event_participants_event_email
        UNIQUE (event_id, email)
    );

CREATE INDEX IF NOT EXISTS idx_event_participants_event_id
    ON event_participants(event_id);
