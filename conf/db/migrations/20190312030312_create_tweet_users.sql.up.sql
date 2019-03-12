CREATE TABLE tweet_users
(
  id           SERIAL    NOT NULL,
  display_name VARCHAR   NOT NULL,
  twitter_id   INTEGER   NOT NULL UNIQUE,
  created_at   TIMESTAMP NOT NULL,
  updated_at   TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
)
