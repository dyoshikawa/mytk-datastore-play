package models

import java.time.LocalDateTime

import scalikejdbc._
import skinny.orm.SkinnyCRUDMapperWithId

case class TwitterUserId(value: Long)

case class TweetUser(twitterUserId: TwitterUserId,
                     screenName: String,
                     createdAt: LocalDateTime,
                     updatedAt: LocalDateTime,
                     tweets: Seq[Tweet] = Nil)

object TweetUser extends SkinnyCRUDMapperWithId[TwitterUserId, TweetUser] {
  override def connectionPoolName = 'default

  override def schemaName = Some("public")

  override lazy val tableName = "tweet_users"
  override lazy val defaultAlias = createAlias("m")
  private[this] lazy val m = defaultAlias

  override def idToRawValue(id: TwitterUserId) = id.value
  override def rawValueToId(value: Any) = TwitterUserId(value.toString.toLong)
  override def primaryKeyFieldName = "twitter_user_id"

  override def extract(rs: WrappedResultSet, n: ResultName[TweetUser]): TweetUser = new TweetUser(
    twitterUserId = TwitterUserId(rs.get(n.twitterUserId)),
    screenName = rs.get(n.screenName),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )

  lazy val tweetsRef = hasMany[Tweet](
    many = Tweet -> Tweet.defaultAlias,
    on = (tu, tt) => sqls.eq(tu.twitterUserId, tt.twitterUserId),
    merge = (twitterUser, tweets) => twitterUser.copy(tweets = tweets)
  )

  def findByTwitterId(twitterUserId: Long)(implicit s: DBSession = autoSession): Option[TweetUser] = {
    findBy(sqls.eq(m.twitterUserId, twitterUserId))
  }
}