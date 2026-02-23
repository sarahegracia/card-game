package com.cardgame

import com.cardgame.deck.*
import sttp.tapir.ztapir.*
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import zio.{ZIO, ZIOAppDefault, ZLayer}
import zio.http.*
import zio.Ref

object MainApp extends ZIOAppDefault {

	override def run =
		for {
			initialHand <- Ref.make(Hand()) // Create the state container
			helloService: HelloWorldService = new HelloWorldService()
			deck = Deck()
			cardService: CardService = new CardService(deck, initialHand)

			businessEndpoints: List[ZServerEndpoint[Any, Any]] = List(
				helloService.helloRoute,
				cardService.getCard,
				cardService.addTohand,
				cardService.drawToHand,
				cardService.foldHand,
				cardService.revealHand,
			)

			// Generate Swagger endpoints FROM server logic
			// This ensures the Swagger endpoints share the same effect type (zio.Task)
			swaggerEndpoints: List[ZServerEndpoint[Any, Any]] =
				SwaggerInterpreter().fromServerEndpoints[zio.Task](businessEndpoints, "Card Game", "1.0")

			// Combine into ZIO-HTTP Routes
			routes: Routes[Any, Response] = ZioHttpInterpreter().toHttp(
				businessEndpoints ++ swaggerEndpoints
			)

			apiDocEndpoints = businessEndpoints.map(_.endpoint)

			// Export the YAML file for Kotlin before the server starts
			_ <- ZIO.attempt(ApiDocGenerator.generate(apiDocEndpoints)).orDie

			_ <- ZIO.logInfo("Starting server...") *> zio.Console.printLine(
				"""
					|-------------------------------------------------------
					| Server started at http://0.0.0.0:8080
					| Swagger UI:       http://0.0.0.0:8080/docs
					|-------------------------------------------------------
				""".stripMargin
			).orDie

			// start server
			_ <- Server.serve(routes).provide(
				ZLayer.succeed(Server.Config.default.copy(address = new java.net.InetSocketAddress("0.0.0.0", 8080))),
				Server.live
			)
		} yield ()

}