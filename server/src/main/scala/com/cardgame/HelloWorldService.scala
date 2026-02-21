package com.cardgame

import zio.*
import zio.json.*
import sttp.tapir.ztapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*

import zio.json.DeriveJsonCodec
import sttp.tapir.generic.auto.* // Magic happens here

// 1. Define the Data Model
case class HelloResponse(message: String)

// 2. Generate the JSON Codec (for zio-json)
object HelloResponse {
	implicit val codec: JsonCodec[HelloResponse] = DeriveJsonCodec.gen[HelloResponse]
}

class HelloWorldService {
	// Now "HelloResponse" will be found and have a valid JSON schema
	val helloEndpoint = endpoint.get
		.in("hello")
		.in(query[String]("name"))
		.out(jsonBody[HelloResponse])

	val helloRoute = helloEndpoint.zServerLogic[Any] { name =>
		ZIO.succeed(HelloResponse(s"Hello, $name!"))
	}
}