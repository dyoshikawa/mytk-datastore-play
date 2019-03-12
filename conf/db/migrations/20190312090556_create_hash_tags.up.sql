CREATE TABLE hash_tags
(
  id SERIAL NOT NULL,
  tweet_id INTEGER NOT NULL REFERENCES tweets,
  created_at    TIMESTAMP NOT NULL,
  updated_at    TIMESTAMP NOT NULL,
  PRIMARY KEY(id)
)