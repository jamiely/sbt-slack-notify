// Your profile name of the sonatype account. The default is the same with the organization value
sonatypeProfileName := "ly.jamie"

// To sync with Maven central, you need to supply the following information:
publishMavenStyle := true

// License of your choice
licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))

// Where is the source code hosted
import xerial.sbt.Sonatype._
sonatypeProjectHosting := Some(GitHubHosting("jamiely", "sbt-slack-notify", "jamie.ly@gmail.com"))

// or if you want to set these fields manually
homepage := Some(url("https://github.com/jamiely/sbt-slack-notify"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/jamiely/sbt-slack-notify"),
    "scm:git@github.com:jamiely/sbt-slack-notify.git"
  )
)
developers := List(
  Developer(id="jamiely", name="Jamie Ly", email="jamie.ly@gmail.com", url=url("http://jamie.ly"))
)
