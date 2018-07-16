val catsVersion = "1.1.0"
val enumeratumVersion = "1.5.13"
val circeVersion = "0.9.3"
val jsonRpcVersion = "0.9.3"
val monixVersion = "3.0.0-RC1"
val http4sVersion = "0.19.0-M1"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7")

val dependencies = Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % "1.0.0-RC2",
  "com.chuusai" %% "shapeless" % "2.3.3",
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-core" % http4sVersion,
  "org.http4s" %% "http4s-client" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "javax.ws.rs" % "javax.ws.rs-api" % "2.1" jar() withSources() withJavadoc(),

  "javax.mail" % "mail" % "1.4.1",
  "org.knowm.xchange" % "xchange-core" % "4.3.8",
  "org.knowm.xchange" % "xchange-bittrex" % "4.3.8",
  "com.google.guava" % "guava" % "19.0"
)

lazy val root = (project in file("."))
  .settings(
    name := "batm-ext-decent",
    version := "0.2",
    scalaVersion := "2.12.6",
    libraryDependencies ++= dependencies
  )

assemblyExcludedJars in assembly := {
  val cp = (fullClasspath in assembly).value
  cp filter { _.data.getParent.endsWith("lib") }
}

val meta = """META.INF(.)*""".r
assemblyMergeStrategy in assembly := {
  case meta(_) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)