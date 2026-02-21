import sbt.Keys.libraryDependencies

lazy val root = (project in file("."))
	.settings(
		name := "scala-backend",
		scalaVersion := "3.3.1",

		libraryDependencies ++= Seq(
			"com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % "1.13.8",
			"com.softwaremill.sttp.tapir" %% "tapir-json-zio"        % "1.13.8",
			"com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.13.8"
		)
	)