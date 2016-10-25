# sbt-slack-notify

Provides an SBT task to send a message when you run the `slackNotify`
task.

# Setup

TODO: push to a public repo

# Usage

```scala
lazy val root = (project in file(".")).
  settings(
    version := "0.1",
    slackMessage := {
        s"Just pushed version ${version.value}"
    },
    slackRoom := "#someroom",
    slackHookUrl := "http://slackhookurl" // or something like System.getenv("SBTSLACKNOTIFY_SLACKHOOKURL")
  )
```

# `slackNotify`

Use the `slackNotify` task to send the message to the specified room.
The room has to be public or the Slack Hook must've been created
specifically for that room.

