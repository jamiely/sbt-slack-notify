version := "0.1"

slackMessage := {
    s"Just pushed version ${version.value}"
}

slackRoom := System.getenv("SBTSLACKNOTIFY_SLACKROOM")

slackHookUrl := System.getenv("SBTSLACKNOTIFY_SLACKHOOKURL")

lazy val root = (project in file("."))

