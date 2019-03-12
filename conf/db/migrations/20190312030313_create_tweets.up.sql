CREATE TABLE tweets
(
  id            SERIAL  NOT NULL,
  tweet_user_id INTEGER NOT NULL REFERENCES tweet_users,
  text          TEXT    NOT NULL,
  hash_tag      VARCHAR NOT NULL,
  datetime      VARCHAR NOT NULL,
  created_at    VARCHAR NOT NULL,
  updated_at    VARCHAR NOT NULL,
  PRIMARY KEY (id)
)
