CREATE TABLE page (
  id                 IDENTITY    NOT NULL,
  source_file_path   VARCHAR     NOT NULL,
  source_file_format VARCHAR(64) NOT NULL,
  output_file_path   VARCHAR     NOT NULL,
  title              VARCHAR     NOT NULL,
  created_at         DATE        NOT NULL,
  last_modified_at   DATE        NOT NULL,
  locale             VARCHAR(16) NOT NULL,
  checksum           BINARY(20)  NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (source_file_path),
  UNIQUE (output_file_path)
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
