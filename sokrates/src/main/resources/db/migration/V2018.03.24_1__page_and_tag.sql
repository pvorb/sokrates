CREATE TABLE page (
  id                 IDENTITY    NOT NULL PRIMARY KEY,
  source_file_path   VARCHAR     NOT NULL UNIQUE,
  source_file_format VARCHAR(64) NOT NULL,
  url                VARCHAR     NOT NULL UNIQUE,
  title              VARCHAR     NOT NULL,
  created_at         DATE        NOT NULL,
  last_modified_at   DATE        NOT NULL,
  locale             VARCHAR(16) NOT NULL,
  checksum           BINARY(20)  NOT NULL
);

CREATE INDEX idx__page__created_at
  ON page (created_at);
CREATE INDEX idx__page__last_modified_at
  ON page (last_modified_at);

-- tag
CREATE TABLE tag (
  id   IDENTITY NOT NULL,
  name VARCHAR  NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (name)
);

-- page_tag
CREATE TABLE page_tag (
  page_id BIGINT NOT NULL,
  tag_id  BIGINT NOT NULL,
  PRIMARY KEY (tag_id, page_id),
  FOREIGN KEY (page_id) REFERENCES page (id),
  FOREIGN KEY (tag_id) REFERENCES tag (id)
);
