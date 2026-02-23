package com.cardgame.games

import sttp.tapir.ztapir.ZServerEndpoint

class CardGameRoutes(service: CardGameService) {

  // Define the endpoint: POST /game/blackjack with a list of player IDs
  val startBlackjack: ZServerEndpoint[Any, Any] = endpoint.post
    .in("game" / "blackjack")
    .in(jsonBody[List[String]]) // List of player IDs
    .out(stringBody)
    .zServerLogic { playerIds =>
      service.startBlackjack(playerIds).as("Blackjack game started!")
    }

  val handUpdates: ZServerEndpoint[Any, Any] =
    endpoint.get
      .in("hand" / "updates" / path[String]("playerId"))
      .out(serverSentEvents)
      .zServerLogic { playerId =>
        ZIO.succeed(
          gameService.getHandStream(playerId).map { update =>
            sttp.model.sse.ServerSentEvent(Some(update.hand.toJson))
          }
        )
      }
}
