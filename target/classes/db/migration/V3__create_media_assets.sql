-- Create media_assets table to store metadata of uploaded files
CREATE TABLE IF NOT EXISTS media_assets (
    id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    course_id BIGINT,
    file_name VARCHAR(512) NOT NULL,
    file_path VARCHAR(1024) NOT NULL,
    file_size BIGINT NOT NULL,
    content_type VARCHAR(128) NOT NULL,
    checksum VARCHAR(128) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE SET NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_media_assets_checksum ON media_assets(checksum);
CREATE INDEX IF NOT EXISTS idx_media_assets_owner ON media_assets(owner_id);
CREATE INDEX IF NOT EXISTS idx_media_assets_course ON media_assets(course_id);

