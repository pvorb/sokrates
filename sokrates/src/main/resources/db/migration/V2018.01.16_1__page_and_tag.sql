CREATE TABLE page (
  id               IDENTITY   NOT NULL,
  path             VARCHAR    NOT NULL,
  title            VARCHAR    NOT NULL,
  created_at       TIMESTAMP  NOT NULL,
  last_modified_at TIMESTAMP  NOT NULL,
  checksum         BINARY(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (path)
);

CREATE INDEX idx_page_created_at
  ON page (created_at);
CREATE INDEX idx_page_last_modified_at
  ON page (last_modified_at);

-- tag
CREATE TABLE tag (
  id   IDENTITY NOT NULL,
  name VARCHAR  NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (name)
);

CREATE INDEX idx_tag_name
  ON tag (name);

-- page_tag
CREATE TABLE page_tag (
  page_id BIGINT NOT NULL,
  tag_id  BIGINT NOT NULL,
  PRIMARY KEY (tag_id, page_id),
  FOREIGN KEY (page_id) REFERENCES page (id),
  FOREIGN KEY (tag_id) REFERENCES tag (id)
);
