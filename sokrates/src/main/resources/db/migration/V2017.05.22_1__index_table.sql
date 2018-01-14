-- page
CREATE TABLE page (
  id               IDENTITY PRIMARY KEY NOT NULL,
  path             VARCHAR              NOT NULL,
  title            VARCHAR              NOT NULL,
  created_at       TIMESTAMP            NOT NULL,
  last_modified_at TIMESTAMP            NOT NULL,
  checksum         BINARY(20)           NOT NULL
);

CREATE UNIQUE INDEX unique_idx_page_path
  ON page (path);
CREATE INDEX idx_page_created_at
  ON page (created_at);
CREATE INDEX idx_page_last_modified_at
  ON page (last_modified_at);

-- tag
CREATE TABLE tag (
  id   IDENTITY PRIMARY KEY NOT NULL,
  name VARCHAR UNIQUE       NOT NULL,
);

CREATE INDEX idx_tag_name
  ON tag (name);

-- page_tag
CREATE TABLE page_tag (
  page_id BIGINT NOT NULL REFERENCES page (id),
  tag_id  BIGINT NOT NULL REFERENCES tag (id)
);

CREATE INDEX idx_page_tag
  ON page_tag (page_id, tag_id);
