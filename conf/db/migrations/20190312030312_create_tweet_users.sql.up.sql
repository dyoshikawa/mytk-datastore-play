CREATE TABLE tweet_users
(
  twitter_user_id INTEGER   NOT NULL,
  screen_name     VARCHAR   NOT NULL,
  created_at      TIMESTAMP NOT NULL,
  updated_at      TIMESTAMP NOT NULL,
  PRIMARY KEY (twitter_user_id)
)
