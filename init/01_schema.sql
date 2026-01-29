CREATE TABLE users (
                      user_id    VARCHAR(36)  PRIMARY KEY,
                      login_id   VARCHAR(50)  UNIQUE NOT NULL,
                      password   VARCHAR(255) NOT NULL,
                      nickname   VARCHAR(50)  NOT NULL,
                      created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE post (
                      post_id    VARCHAR(36)  PRIMARY KEY,
                      title      VARCHAR(200) NOT NULL,
                      content    TEXT         NOT NULL,
                      user_id    VARCHAR(36)  NOT NULL,
                      created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                      INDEX idx_created_at (created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;