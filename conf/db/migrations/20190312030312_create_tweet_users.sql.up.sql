CREATE TABLE tweet_users
(
  id          SERIAL    NOT NULL,
  twitter_user_id  INTEGER   NOT NULL UNIQUE,
  screen_name VARCHAR   NOT NULL,
  created_at  TIMESTAMP NOT NULL,
  updated_at  TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
)
