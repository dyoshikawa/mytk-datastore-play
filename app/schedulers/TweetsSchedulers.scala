package schedulers

import java.io._
import java.net.URL
import twitter4j._
import scala.sys.process._
import scala.language.postfixOps


class TweetsSchedulers extends StatusListener{
  override def onDeletionNotice(statusDeletionNotice :StatusDeletionNotice) ={
    // ツイートが削除された時に発動します
    // 今回は無視
  }

  override def onScrubGeo(userId :Long, upToStatusId :Long) ={
    // 今回は無視
  }

  override def onStatus(status :Status) ={
    // ツイートされた時に発動します
    val user = status.getUser()
    val file = new File(new File(".").getCanonicalPath, s"files/${user.getId}.txt")
    s"echo ${status.getText()}" #>> file !

    val medias = status.getMediaEntities().map(x => x.getMediaURL()).toList
    medias.zipWithIndex.foreach{case(x:String, i:Int) =>
      val stream = new URL(x).openStream
      val buf = Stream.continually(stream.read).takeWhile( -1 != ).map(_.byteValue).toArray
      val nameOnly = x.drop(x.lastIndexOf('/'))
      val fileName = nameOnly.split('.').mkString(i.toString ++ ".")
      val dir = (new File(".").getCanonicalPath).toString ++ s"/files/${user.getId}/"
      s"mkdir -p ${dir}"!
      val imageFile = new File(dir, s"${fileName}")
      val bw = new BufferedOutputStream(new FileOutputStream(imageFile))
      stream.close()
      bw.write(buf)
      bw.close
    }
  }

  override def onTrackLimitationNotice(numberOfLimitedStatuses :Int) ={
    // 今回は無視します
  }

  override def onException(e :Exception) ={
    // 例外が起こった場合に通知されます
    // 今回はスタックトレースでも出しておきます
    e.printStackTrace();
  }

  override def onStallWarning(e: StallWarning) = {
    // 変わったらしい
  }
}