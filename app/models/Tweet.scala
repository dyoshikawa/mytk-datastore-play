package models

import java.time.LocalDateTime

import scalikejdbc._
import skinny.orm.SkinnyCRUDMapperWithId

case class TweetId(value: Long)

case class Tweet(id: TweetId,
                 twitterUserId: Long,
                 twitterTweetId: Long,
                 text: String,
                 datetime: LocalDateTime,
                 createdAt: LocalDateTime,
                 updatedAt: LocalDateTime)

object Tweet extends SkinnyCRUDMapperWithId[TweetId, Tweet] {
  override def connectionPoolName = 'default

  override def schemaName = Some("public")

  override lazy val tableName = "tweet_users"
  override lazy val defaultAlias = createAlias("m")
  private[this] lazy val m = defaultAlias

  override def idToRawValue(id: TweetId) = id.value

  override def rawValueToId(value: Any) = TweetId(value.toString.toLong)

  //  override def columnNames = Seq("id", "screen_name", "twitter_id", "created_at", "updated_at")

  override def extract(rs: WrappedResultSet, n: ResultName[Tweet]): Tweet = new Tweet(
    id = TweetId(rs.get(n.id)),
    twitterUserId = rs.get(n.twitterUserId),
    twitterTweetId = rs.get(n.twitterTweetId),
    text = rs.get(n.text),
    datetime = rs.get(n.datetime),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )

  def findByTwitterTweetId(twitterTweetId: Long)(implicit s: DBSession = autoSession): Option[Tweet] = {
    findBy(sqls.eq(m.twitterTweetId, twitterTweetId))
  }
}