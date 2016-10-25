package ly.jamie.sbtslacknotify

import java.io.DataOutputStream
import java.net.HttpURLConnection

import sbt._

import scala.util.Try

object SlackNotifyPlugin extends AutoPlugin {
  object autoImport {
    val slackNotify = taskKey[Unit]("Sends a message to slack")
    val slackRoom = settingKey[String]("The room to send the message to")
    val slackMessage = settingKey[String]("The message to send")
    val slackHookUrl = settingKey[String]("The hook url")

    lazy val baseSlackNotifySettings: Seq[Def.Setting[_]] = Seq(
      slackNotify := {
        val config = Config(
          (slackHookUrl in slackNotify).value,
          Option((slackRoom in slackNotify).value).filter(_ != ""))
        val result = SlackNotify(
          config
        ).sendMessage((slackMessage in slackNotify).value)
        println(result)
      }
    )
  }

  import autoImport._
  override def requires = sbt.plugins.JvmPlugin

  // This plugin is automatically enabled for projects which are JvmPlugin.
  override def trigger = allRequirements

  // a group of settings that are automatically added to projects.
  override val projectSettings =
    inConfig(Compile)(baseSlackNotifySettings) ++
    inConfig(Test)(baseSlackNotifySettings)
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
