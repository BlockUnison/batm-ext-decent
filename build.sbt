val catsVersion = "1.1.0"
val enumeratumVersion = "1.5.13"
val circeVersion = "0.9.3"
val jsonRpcVersion = "0.9.3"
val http4sVersion = "0.19.0-M1"
val xchangeVersion = "4.2.3"
val catsRetryVersion = "0.1.0"

resolvers ++= Seq(
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "JCenter" at "https://jcenter.bintray.com/"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7")

val dependencies = Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % "0.10.1",
  "com.chuusai" %% "shapeless" % "2.3.3",
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-core" % http4sVersion,
  "org.http4s" %% "http4s-client" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.github.cb372" %% "cats-retry-core"        % catsRetryVersion,
  "com.github.cb372" %% "cats-retry-cats-effect" % catsRetryVersion,

  "javax.ws.rs" % "javax.ws.rs-api" % "2.1" jar() withSources() withJavadoc(),

  "org.slf4j" % "slf4j-api" % "1.7.25",
  "com.google.zxing" % "core" % "2.3.0",
  "javax.mail" % "mail" % "1.4.1",
  "org.knowm.xchange" % "xchange-core" % xchangeVersion,
  "org.knowm.xchange" % "xchange-itbit" % xchangeVersion,
  "org.knowm.xchange" % "xchange-bittrex" % xchangeVersion,
  "org.knowm.xchange" % "xchange-bitfinex" % xchangeVersion,
  "org.knowm.xchange" % "xchange-hitbtc" % xchangeVersion,
  "com.google.guava" % "guava" % "19.0",
//  "com.azazar" % "bitcoin-json-rpc-client" % "1.0",
  "com.github.mmazi" % "rescu" % "1.9.1",
  "junit" % "junit" % "4.10",

  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

lazy val root = (project in file("."))
  .settings(
    name := "batm_server_extensions_decent",
    scalaVersion := "2.12.6",
    scalacOptions ++= Seq("-Ypartial-unification", "-unchecked", "-language:higherKinds", "-language:postfixOps"),
    libraryDependencies ++= dependencies
  )

val meta = """META.INF(.)*""".r
assemblyMergeStrategy in assembly := {
  case meta(_) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

test in assembly := {}


