package models

import java.time.LocalDateTime

import scalikejdbc._
import skinny.orm.SkinnyMapper
import skinny.orm.feature._

case class TweetUser(screenName: String, twitterId: Long, createdAt: LocalDateTime, updatedAt: LocalDateTime)

object TweetUser extends SkinnyMapper[TweetUser] {
  override lazy val defaultAlias = createAlias("m")

  override def extract(rs: WrappedResultSet, n: ResultName[TweetUser]): TweetUser = new TweetUser(
    screenName = rs.get(n.name),
    twitterId = rs.get(n.twitterId),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )
}