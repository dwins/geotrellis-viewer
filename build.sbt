name := "geotrellis-ingest-test"
version := "0.1.0"
scalaVersion := "2.10.5"
crossScalaVersions := Seq("2.11.5", "2.10.5")
organization := "com.azavea"
licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))
scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-Yinline-warnings",
  "-language:implicitConversions",
  "-language:reflectiveCalls",
  "-language:higherKinds",
  "-language:postfixOps",
  "-language:existentials",
  "-feature")
publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }

resolvers += Resolver.bintrayRepo("azavea", "geotrellis")

fork in run := true

libraryDependencies ++= Seq(
  "com.azavea.geotrellis" %% "geotrellis-spark-etl" % "0.10.0",
  "com.azavea.geotrellis" %% "geotrellis-s3" % "0.10.0",
  "org.apache.spark" %% "spark-core" % "1.5.2" % "provided",
  "com.azavea.geotrellis" %% "geotrellis-raster"    % "0.10.0-RC4",
  "io.spray"              %% "spray-routing"        % "1.3.3",
  "io.spray"              %% "spray-can"            % "1.3.3",
  "org.scalatest"       %%  "scalatest"      % "2.2.0" % "test"
)

// When creating fat jar, remote some files with
// bad signatures and resolve conflicts by taking the first
// versions of shared packaged types.
assemblyMergeStrategy in assembly := {
  case "reference.conf" => MergeStrategy.concat
  case "application.conf" => MergeStrategy.concat
  case "META-INF/MANIFEST.MF" => MergeStrategy.discard
  case "META-INF\\MANIFEST.MF" => MergeStrategy.discard
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.discard
  case "META-INF/ECLIPSEF.SF" => MergeStrategy.discard
  case _ => MergeStrategy.first
}
