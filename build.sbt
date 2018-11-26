val catsVersion = "1.4.0"
val catsEffectVersion = "1.0.0"
val circeVersion = "0.10.1"
val http4sVersion = "0.19.0"
val xchangeVersion = "4.3.11"
val catsRetryVersion = "0.2.0"
val monocleVersion = "1.5.0-cats"
val logbackVersion = "1.2.3"

val slf4jVersion = "1.7.25"
resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "JCenter" at "https://jcenter.bintray.com/"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7")
addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.2.4")

val dependencies = Seq(
  "org.typelevel"               %% "cats-core"              % catsVersion,
  "org.typelevel"               %% "cats-effect"            % catsEffectVersion,
  "com.chuusai"                 %% "shapeless"              % "2.3.3",
  "com.github.cb372"            %% "cats-retry-core"        % catsRetryVersion,
  "com.github.cb372"            %% "cats-retry-cats-effect" % catsRetryVersion,
  "javax.ws.rs"                 % "javax.ws.rs-api"         % "2.1" jar() withSources() withJavadoc(),
  "org.slf4j"                   % "slf4j-api"               % slf4jVersion,
  "ch.qos.logback"              % "logback-classic"         % logbackVersion,
  "ch.qos.logback"              % "logback-core"            % logbackVersion,

  "org.scalatest"               %% "scalatest"              % "3.0.5"         % "test",
)

val libDependencies = Seq(
  "org.http4s"                  %% "http4s-dsl"             % http4sVersion,
  "org.http4s"                  %% "http4s-core"            % http4sVersion,
  "org.http4s"                  %% "http4s-client"          % http4sVersion,
  "org.http4s"                  %% "http4s-blaze-client"    % http4sVersion,
  "org.http4s"                  %% "http4s-circe"           % http4sVersion,
  "io.circe"                    %% "circe-core"             % circeVersion,
  "io.circe"                    %% "circe-generic"          % circeVersion,
  "io.circe"                    %% "circe-parser"           % circeVersion,
  "com.github.julien-truffaut"  %%  "monocle-core"          % monocleVersion,
  "com.github.julien-truffaut"  %%  "monocle-macro"         % monocleVersion,
)

val xchangeDependencies = Seq(
  "com.github.mmazi"                  % "rescu"                   % "2.0.2",
  "commons-io"                        % "commons-io"              % "2.6",
  "org.apache.commons"                % "commons-lang3"           % "3.7",
  "com.fasterxml.jackson.dataformat"  % "jackson-dataformat-csv"  % "2.9.6"
)

lazy val unmanagedSettings = Seq(
  unmanagedJars in Compile ++= Seq(
    file("lib/batm_server_extensions_api.jar")
  )
)

lazy val compilerOptions = Seq("-Ypartial-unification", "-unchecked", "-language:higherKinds", "-language:postfixOps")
lazy val commonSettings = Seq(
	scalacOptions ++= compilerOptions,
	updateOptions := updateOptions.value.withLatestSnapshots(false)
)

val meta = """META.INF(.)*""".r
lazy val assemblySettings = Seq(
  assemblyMergeStrategy in assembly := {
    case meta(_) => MergeStrategy.discard
    case _ => MergeStrategy.first
  },
  test in assembly := {},
  assemblyJarName in assembly := name.value + ".jar",
  assemblyExcludedJars in assembly := {
    val cp = (fullClasspath in assembly).value
    cp filter {_.data.getName.startsWith("batm_server_extensions") }
  }
)

lazy val common = project
  .in(file("common"))
  .settings(
    name := "batm-ext-common",
    commonSettings ++ unmanagedSettings,
    libraryDependencies ++= dependencies
  )
  .dependsOn(xchange)

lazy val decent = project
  .in(file("decent"))
  .settings(
    name := "batm-ext-decent",
    commonSettings ++ unmanagedSettings,
    assemblySettings,
    libraryDependencies ++= dependencies ++ libDependencies
  )
  .dependsOn(common, xchange)

lazy val coinmate = project
  .in(file("coinmate"))
  .settings(
    name := "batm-ext-coinmate",
    commonSettings ++ unmanagedSettings,
    assemblySettings,
    libraryDependencies ++= dependencies ++ libDependencies
  )
  .dependsOn(common, xchange)
  
lazy val xchange = project
  .in(file("xchange"))
  .settings(
    name := "xchange",
    commonSettings,
    libraryDependencies ++= dependencies ++ xchangeDependencies
  )

lazy val global = (project in file("."))
  .settings(commonSettings)
  .aggregate(
	xchange,
    common,
    decent,
    coinmate
  )
