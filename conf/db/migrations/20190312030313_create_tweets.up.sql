CREATE TABLE tweets
(
  twitter_tweet_id BIGINT NOT NULL,
  tweet_user_twitter_user_id INTEGER NOT NULL REFERENCES tweet_users,
  text          TEXT      NOT NULL,
  datetime      TIMESTAMP NOT NULL,
  favorite_count INTEGER NOT NULL,
  retweet_count INTEGER NOT NULL,
  created_at    TIMESTAMP NOT NULL,
  updated_at    TIMESTAMP NOT NULL,
  PRIMARY KEY (twitter_tweet_id)
)
