package com.cardgame

import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.apispec.openapi.circe.yaml._
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets

object ApiDocGenerator {
	def generate(endpoints: List[sttp.tapir.AnyEndpoint]): Unit = {
		val docs = OpenAPIDocsInterpreter().toOpenAPI(
			endpoints,
			"Card Game API",
			"0.1.0"
		)//.openapi("3.0.3")
		System.out.println("OpenAPI version: " + docs.openapi)

		val yaml = docs.toYaml
		val path = Paths.get("../specs/api-docs.yaml")
		Files.createDirectories(path.getParent)
		Files.write(path, yaml.getBytes(StandardCharsets.UTF_8))

		println(s"API documentation successfully exported to $path")
	}
}