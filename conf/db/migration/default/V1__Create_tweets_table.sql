CREATE TABLE TWEETS
(
  id         SERIAL  NOT NULL,
  text       TEXT    NOT NULL,
  hash_tag   varchar NOT NULL,
  datetime   varchar NOT NULL,
  creted_at  varchar NOT NULL,
  updated_at varchar NOT NULL,
  PRIMARY KEY (id)
)