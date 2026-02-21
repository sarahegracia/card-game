package com.cardgame

import sttp.tapir.ztapir.*
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import zio.{ZIO, ZIOAppDefault}
import zio.http.*

object MainApp extends ZIOAppDefault {
	val helloService = new HelloWorldService()
	val cardService = new CardService()

	val businessEndpoints: List[ZServerEndpoint[Any, Any]] = List(
		helloService.helloRoute,
		cardService.getCard,
	)

	// Generate Swagger endpoints FROM server logic
	// This ensures the Swagger endpoints share the same effect type (zio.Task)
	val swaggerEndpoints: List[ZServerEndpoint[Any, Any]] =
		SwaggerInterpreter().fromServerEndpoints[zio.Task](businessEndpoints, "Card Game", "1.0")

	// Combine into ZIO-HTTP Routes
	val routes: Routes[Any, Response] = ZioHttpInterpreter().toHttp(
		businessEndpoints ++ swaggerEndpoints
	)

	override def run =
		val apiDocEndpoints = businessEndpoints.map(_.endpoint)

		// Export the YAML file for Kotlin before the server starts
		ZIO.attempt(ApiDocGenerator.generate(apiDocEndpoints)).orDie *>
			ZIO.logInfo("Starting server...") *>
			zio.Console.printLine(
				"""
					|-------------------------------------------------------
					| Server started at http://localhost:8080
					| Swagger UI:       http://localhost:8080/docs
					|-------------------------------------------------------
				""".stripMargin
			).orDie *>
			// start server
			Server.serve(routes).provide(Server.default)
}