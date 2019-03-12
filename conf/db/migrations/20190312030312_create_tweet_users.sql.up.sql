CREATE TABLE tweet_users
(
  id           SERIAL  NOT NULL,
  display_name VARCHAR NOT NULL,
  twitter_id   INTEGER NOT NULL UNIQUE,
  hash_tag     VARCHAR NOT NULL,
  datetime     VARCHAR NOT NULL,
  created_at   VARCHAR NOT NULL,
  updated_at   VARCHAR NOT NULL,
  PRIMARY KEY (id)
)
