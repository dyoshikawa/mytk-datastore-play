package schedulers

import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Paging, Status, Twitter, TwitterFactory}

import scala.collection.JavaConverters._

class TweetsSchedulers {
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
    allTweet(twitter, userId, Option(newMaxId), count + 1)
  }

  def put(): Unit = {

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
    allTweet(twitter, user.getId, None, 0)
  }
}