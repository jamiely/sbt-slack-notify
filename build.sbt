sbtPlugin := true

name := "sbt-slack-notify"

version := "0.3.1"

organization := "ly.jamie"

useGpg := true

// Add the default sonatype repository setting
publishTo := sonatypePublishTo.value
