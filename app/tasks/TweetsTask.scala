package tasks

import java.time._

import akka.actor.ActorSystem
import javax.inject.Inject
import models.{Tweet, TweetUser}
import scalikejdbc._
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Paging, Status, Twitter, TwitterFactory}

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class TweetsTask @Inject()(actorSystem: ActorSystem)(implicit executionContext: ExecutionContext) {

  case class TweetData(twitterTweetId: Long, twitterUserId: Long, text: String,
                       datetime: LocalDateTime, favoriteCount: Int, retweetCount: Int)

  def allTweet(twitter: Twitter, userId: Long, maxId: Option[Long], count: Int): Unit = {
    if (count > 30) {
      return
    }
    val paging: Paging = new Paging(1, 200)
    maxId match {
      case None =>
        println("none")
      case Some(m) =>
        paging.setMaxId(m)
    }
    val tweetList: Seq[Status] = twitter.getUserTimeline(userId, paging).asScala
    val newMaxId: Long = tweetList.last.getId
    if (newMaxId == maxId.getOrElse(0)) {
      return
    }
    println(tweetList.last.getId)
    println(tweetList.last.getHashtagEntities)
    println(tweetList.last.getFavoriteCount)
    tweetList.foreach { tweet =>
      putTweet(TweetData(
        twitterTweetId = tweet.getId,
        twitterUserId = tweet.getUser.getId,
        text = tweet.getText,
        datetime = LocalDateTime.ofInstant(tweet.getCreatedAt.toInstant, ZoneId.systemDefault),
        favoriteCount = tweet.getFavoriteCount,
        retweetCount = tweet.getRetweetCount
      ))
    }
    allTweet(twitter, userId, Option(newMaxId), count + 1)
  }

  def putTweetUser(screenName: String, twitterUserId: Long): Unit = {
    implicit val session = AutoSession

    TweetUser.findByTwitterId(twitterUserId) match {
      case Some(t) =>
        println(t.twitterUserId)
      case None =>
        println("NONE!!!!")
        TweetUser.createWithAttributes(
          'twitterUserId -> twitterUserId,
          'screenName -> screenName,
          'createdAt -> LocalDateTime.now,
          'updatedAt -> LocalDateTime.now
        )
    }
  }

  def putTweet(tweet: TweetData): Unit = {
    implicit val session = AutoSession

    Tweet.findByTwitterTweetId(tweet.twitterTweetId) match {
      case Some(t) =>
        println(t.twitterTweetId)
      case None =>
        println("NONE tweet")
        Tweet.createWithAttributes(
          'twitterTweetId -> tweet.twitterTweetId,
          'tweetUserTwitterUserId -> tweet.twitterUserId,
          'text -> tweet.text,
          'favoriteCount -> tweet.favoriteCount,
          'retweetCount -> tweet.retweetCount,
          'datetime -> tweet.datetime,
          'createdAt -> LocalDateTime.now,
          'updatedAt -> LocalDateTime.now
        )
    }
  }

  def main(): Unit = {
    val consumerKey = sys.env.getOrElse("TWITTER_CONSUMER_KEY", "TWITTER_CONSUMER_KEY")
    val consumerSecret = sys.env.getOrElse("TWITTER_CONSUMER_SECRET", "TWITTER_CONSUMER_SECRET")
    val accessToken = sys.env.getOrElse("TWITTER_ACCESS_TOKEN", "TWITTER_ACCESS_TOKEN")
    val accessTokenSecret = sys.env.getOrElse("TWITTER_ACCESS_TOKEN_SECRET", "TWITTER_ACCESS_TOKEN_SECRET")

    val cb: ConfigurationBuilder = new ConfigurationBuilder
    cb.setDebugEnabled(true)
      .setOAuthConsumerKey(consumerKey)
      .setOAuthConsumerSecret(consumerSecret)
      .setOAuthAccessToken(accessToken)
      .setOAuthAccessTokenSecret(accessTokenSecret)
    val tf: TwitterFactory = new TwitterFactory(cb.build)
    val twitter: Twitter = tf.getInstance
    val user = twitter.showUser("dayukoume")
    putTweetUser(user.getScreenName, user.getId)
    allTweet(twitter, user.getId, None, 0)
  }

  actorSystem.scheduler.schedule(initialDelay = 10.seconds, interval = 1.minute) {
    // the block of code that will be executed
    println("Executing fetch tweets...")
    main()
  }
}
