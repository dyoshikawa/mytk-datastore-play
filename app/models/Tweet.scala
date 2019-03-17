package models

import java.time.LocalDateTime

import scalikejdbc._
import skinny.orm.SkinnyCRUDMapperWithId

case class TwitterTweetId(value: Long)

case class Tweet(twitterTweetId: TwitterTweetId,
                 tweetUserTwitterUserId: TwitterUserId,
                 text: String,
                 datetime: LocalDateTime,
                 favoriteCount: Int,
                 retweetCount: Int,
                 createdAt: LocalDateTime,
                 updatedAt: LocalDateTime,
                 tweetUser: Option[TweetUser] = None)

object Tweet extends SkinnyCRUDMapperWithId[TwitterTweetId, Tweet] {
  override def connectionPoolName = 'default

  override def schemaName = Some("public")

  override lazy val tableName = "tweets"
  override lazy val defaultAlias = createAlias("tt")
  private[this] lazy val tt = defaultAlias

  override def idToRawValue(id: TwitterTweetId) = id.value

  override def rawValueToId(value: Any) = TwitterTweetId(value.toString.toLong)

  override def primaryKeyFieldName = "twitter_tweet_id"

  override def extract(rs: WrappedResultSet, n: ResultName[Tweet]): Tweet = new Tweet(
    twitterTweetId = TwitterTweetId(rs.get(n.twitterTweetId)),
    tweetUserTwitterUserId = TwitterUserId(rs.get(n.tweetUserTwitterUserId)),
    text = rs.get(n.text),
    datetime = rs.get(n.datetime),
    favoriteCount = rs.get(n.favoriteCount),
    retweetCount = rs.get(n.retweetCount),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )

  belongsTo[TweetUser](TweetUser, (tweet, tweetUser) => tweet.copy(tweetUser = tweetUser)).byDefault

  def findByTwitterTweetId(twitterTweetId: Long)(implicit s: DBSession = autoSession): Option[Tweet] = {
    findBy(sqls.eq(tt.twitterTweetId, twitterTweetId))
  }
}