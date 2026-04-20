package com.cardgame.games

import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*
import sttp.tapir.ztapir.*
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.server.http4s.ztapir.serverSentEventsBody
import sttp.tapir.server.ziohttp.*
import sttp.tapir.{CodecFormat, Schema, Validator}
import zio.http.MediaType
import zio.json.*
import zio.{ZIO, *}

class CardGameRoutes(service: CardGameService):

  // Define the endpoint: POST /game/blackjack with a list of player IDs
  val startBlackjack: ZServerEndpoint[Any, Any] =
    endpoint.post
      .in("game" / "blackjack")
      .in(jsonBody[Seq[String]].description("List of player IDs").example(List("player1")))
      .out(stringBody)
      .zServerLogic { playerIds =>
        service.startBlackjack(playerIds).as("Blackjack game started!")
      }

  val handUpdates: ZServerEndpoint[Any, ZioStreams] =
    endpoint.get
      .in("hand" / "updates" / path[String]("playerId"))
      .out(serverSentEventsBody)
      .zServerLogic { playerId =>
        ZIO.succeed(
          service.getHandStream(playerId).map { update =>
            sttp.model.sse.ServerSentEvent(Some(update.hand.toJson))
          }
        )
      }
