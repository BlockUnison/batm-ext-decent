val catsVersion = "1.1.0"
val enumeratumVersion = "1.5.13"
val circeVersion = "0.9.3"
val jsonRpcVersion = "0.9.3"
val monixVersion = "3.0.0-RC1"
val http4sVersion = "0.19.0-M1"

val dependencies = Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % "1.0.0-RC2",
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-core" % http4sVersion,
  "org.http4s" %% "http4s-client" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
)

lazy val root = (project in file("."))
  .settings(
    name := "batm-ext-decent",
    version := "0.1",
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

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = true)