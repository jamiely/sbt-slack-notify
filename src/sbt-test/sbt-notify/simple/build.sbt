lazy val root = (project in file(".")).
  settings(
    version := "0.1",
    scalaVersion := "2.10.2",
    slackMessage := {
        s"Just pushed version ${version.value}"
    },
    slackRoom := System.getenv("SBTSLACKNOTIFY_SLACKROOM"),
    slackHookUrl := System.getenv("SBTSLACKNOTIFY_SLACKHOOKURL")
  )

