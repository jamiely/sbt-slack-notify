package ly.jamie.sbtslacknotify

import java.io.DataOutputStream
import java.net.HttpURLConnection

import sbt._
import sbt.Keys.sLog

import scala.util.Try

object SlackNotifyPlugin extends AutoPlugin {
  object autoImport {
    val slackNotify = taskKey[Option[(Int, String)]]("Sends a message to slack")
    val slackRoom = settingKey[String]("The room to send the message to")
    val slackMessage = settingKey[String]("The message to send")
    val slackHookUrl = settingKey[String]("The hook url")

    lazy val baseSlackNotifySettings: Seq[Setting[_]] = Def.settings(
      slackRoom := "",
      slackMessage := "",
      slackHookUrl := "",
      slackNotify := {
        val log = sLog.value
        if(slackRoom.value.isEmpty) {
          log.error(s"You must specify the slackRoom key")
          None
        } else if(slackMessage.value.isEmpty) {
          log.error(s"You must specify the slackMessage key")
          None
        } else if(slackHookUrl.value.isEmpty) {
          log.error(s"You must specify the slackHookUrl key")
          None
        } else {
          val config = Config(
            (slackHookUrl in slackNotify).value,
            Option((slackRoom in slackNotify).value).filter(_ != ""))
          val result = SlackNotify(
            config
          ).sendMessage((slackMessage in slackNotify).value)
          log.info(s"Slack response: $result")
          Option(result)
        }
      }
    )
  }

  import autoImport._
  override def requires = sbt.plugins.JvmPlugin

  // this plugin must be specifically enabled
  override def trigger = allRequirements

  // a group of settings that are automatically added to projects.
  override val projectSettings = baseSlackNotifySettings
}

case class Config(
                      url: String,
                      room: Option[String]
                      )

case class SlackNotify(
                      config: Config
                      ) {
  protected def getPayload(message: String, room: Option[String] = None) = s"""{
    "text": "$message"
    ${room.map { r =>
      s""" , "channel" : "$r" """
    }.getOrElse("")}
  }"""

  def sendMessage(message: String, optRoom: Option[String] = None) = {
    val room = optRoom.orElse(config.room)
    val postData = getPayload(message, room).getBytes
    val conn = new URL(config.url).openConnection().asInstanceOf[HttpURLConnection]
    conn.setDoOutput( true )
    conn.setInstanceFollowRedirects( false )
    conn.setRequestMethod( "POST" )
    conn.setRequestProperty( "Content-Type", "application/json")
    conn.setRequestProperty( "charset", "utf-8")
    conn.setRequestProperty( "Content-Length", postData.length.toString)
    conn.setUseCaches( false )

    val wr = new DataOutputStream( conn.getOutputStream )
    Try { wr.write(postData) }
    wr.close()

    (conn.getResponseCode, conn.getResponseMessage)
  }
}
