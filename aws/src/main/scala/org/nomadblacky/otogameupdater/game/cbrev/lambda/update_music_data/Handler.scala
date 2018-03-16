package org.nomadblacky.otogameupdater.game.cbrev.lambda.update_music_data

import awscala.DateTime
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import org.nomadblacky.otogameupdater.game.cbrev.client.MyPageClient
import org.nomadblacky.otogameupdater.game.cbrev.lambda.DynamoDbRecord
import org.nomadblacky.otogameupdater.game.cbrev.model.MusicPlayData

import scala.beans.BeanProperty

case class Request(
  @BeanProperty var accessCode: String = "",
  @BeanProperty var password: String = ""
) {
  def this() = this("", "")
}

case class Response(
  @BeanProperty message: String
)

case class MusicPlayDataRecord(playData: MusicPlayData, updated: DateTime) extends DynamoDbRecord {
  override val key: String = playData.musicDetail.title
  override val attributes = Seq(
  )
}

class Handler extends RequestHandler[Request, Response] {
  override def handleRequest(input: Request, context: Context): Response = {
    val client = MyPageClient(input.accessCode, input.password)
    val musics = client.fetchMusics
    val playData = client.fetchMusicPlayData(musics)



    Response("OK")
  }
}
