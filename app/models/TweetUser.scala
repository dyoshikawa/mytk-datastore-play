package models

import java.time.LocalDateTime

import scalikejdbc._
import skinny.orm.SkinnyCRUDMapper

case class TweetUser(twitterUserId: Long,
                     screenName: String,
                     createdAt: LocalDateTime,
                     updatedAt: LocalDateTime,
                     tweets: Seq[Tweet] = Nil)

object TweetUser extends SkinnyCRUDMapper[TweetUser] {
  override def connectionPoolName = 'default

  override def schemaName = Some("public")

  override lazy val tableName = "tweet_users"
  override lazy val defaultAlias = createAlias("m")
  private[this] lazy val m = defaultAlias

  override def extract(rs: WrappedResultSet, n: ResultName[TweetUser]): TweetUser = new TweetUser(
    twitterUserId = rs.get(n.twitterUserId),
    screenName = rs.get(n.screenName),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )

  lazy val tweetsRef = hasMany[Tweet](
    many = Tweet -> Tweet.defaultAlias,
    on = (u, t) => sqls.eq(u.id, t.twitterUserId),
    merge = (twitterUser, tweets) => twitterUser.copy(tweets = tweets)
  )

  def findByTwitterId(twitterUserId: Long)(implicit s: DBSession = autoSession): Option[TweetUser] = {
    findBy(sqls.eq(m.twitterUserId, twitterUserId))
  }
}