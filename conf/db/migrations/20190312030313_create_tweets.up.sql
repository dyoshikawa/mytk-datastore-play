CREATE TABLE tweets
(
  twitter_tweet_id INTEGER NOT NULL,
  tweet_user_id INTEGER   NOT NULL REFERENCES tweet_users,
  text          TEXT      NOT NULL,
  datetime      TIMESTAMP NOT NULL,
  created_at    TIMESTAMP NOT NULL,
  updated_at    TIMESTAMP NOT NULL,
  PRIMARY KEY (twitter_tweet_id)
)
