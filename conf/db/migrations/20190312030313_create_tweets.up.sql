CREATE TABLE tweets
(
  id            SERIAL    NOT NULL,
  tweet_user_id INTEGER   NOT NULL REFERENCES tweet_users,
  twitter_tweet_id INTEGER NOT NULL UNIQUE,
  text          TEXT      NOT NULL,
  datetime      TIMESTAMP NOT NULL,
  created_at    TIMESTAMP NOT NULL,
  updated_at    TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
)
