CREATE TABLE public.events
(
    id                TEXT PRIMARY KEY,
    title             VARCHAR                  NOT NULL,
    description       TEXT                     NOT NULL,
    video_url         VARCHAR,
    start_time        TIMESTAMP                NOT NULL,
    end_time          TIMESTAMP                NOT NULL,
    location          VARCHAR                  NOT NULL,
    "public"          BOOLEAN                  NOT NULL,
    participant_limit INT                      NOT NULL,
    signup_deadline   TIMESTAMP,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);