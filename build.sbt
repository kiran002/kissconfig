name := "kissconfig"
lazy val commonSettings = Seq(
  organization := "com.github.kiran002",
  version := "1.0.1",
  scalaVersion := "2.12.10",
  crossScalaVersions := Seq("2.11.12", scalaVersion.value),
  resolvers += Resolver.typesafeIvyRepo("releases"),
  libraryDependencies += "com.typesafe"   % "config"         % "1.4.0",
  libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  libraryDependencies += "org.scala-lang" % "scala-reflect"  % scalaVersion.value,
  // for testing
  libraryDependencies += "org.scalatest"     % "scalatest_2.12"  % "3.1.1"         % "test",
  libraryDependencies += "junit"             % "junit"           % "4.12"          % "test",
  libraryDependencies += "org.scalatestplus" % "junit-4-12_2.12" % "3.3.0.0-SNAP2" % "test",
  publishMavenStyle := true,
  publishArtifact in Test := false,
  publishTo := Some(
    if (isSnapshot.value)
      Opts.resolver.sonatypeSnapshots
    else
      Opts.resolver.sonatypeStaging
  ),
  pomIncludeRepository := { _ => false },
  licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  homepage := Some(url("https://github.com/kiran002/kissconfig")),
  pomExtra := (<scm>
            <url>git://github.com/kiran002/kissconfig.git</url>
            <connection>scm:git://github.com/kiran002/kissconfig.git</connection>
        </scm>
            <developers>
                <developer>
                    <id>kiran002</id>
                    <name>Sai kiran Krishna murthy</name>
                    <url>https://github.com/kiran002/kissconfig</url>
                </developer>
            </developers>)
)

lazy val kissconfig = (project in file("."))
  .settings(
    commonSettings
  )
  .aggregate(`kissconfig-core`)

lazy val `kissconfig-core` = (project in file("kissconfig-core"))
  .settings(
    commonSettings
  )

lazy val `kissconfig-examples` = (project in file("kissconfig-examples"))
  .settings(
    commonSettings,
    publish / skip := true
  )
  .dependsOn(`kissconfig-core`)
