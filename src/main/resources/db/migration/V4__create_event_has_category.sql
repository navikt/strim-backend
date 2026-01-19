CREATE TABLE event_has_category
(
    event_id    UUID NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    category_id INT  NOT NULL REFERENCES category (id) ON DELETE CASCADE,
    PRIMARY KEY (event_id, category_id)
);
