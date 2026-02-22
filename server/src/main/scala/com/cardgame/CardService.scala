package com.cardgame

import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*
import sttp.tapir.ztapir.*
import zio.{ZIO, *}
import zio.json.*
import com.cardgame.deck.*
import sttp.tapir.generic.auto.*

class CardService(deck: Deck, hand: Ref[Hand]):

	val getCard: ZServerEndpoint[Any, Any] =
		endpoint.get
			.in("card")
			.out(jsonBody[Card])
			.zServerLogic(_ =>
				ZIO.succeed(deck.deal()) <* ZIO.logInfo("Cards left in deck: " + deck.size)
			)

	val addTohand: ZServerEndpoint[Any, Any] =
		endpoint.get
			.in("hand")
			.out(jsonBody[Hand])
			.zServerLogic(_ =>
				ZIO.succeed(Hand()) <* ZIO.logInfo("Created new hand.")
			)

	val drawToHand: ZServerEndpoint[Any, Any] =
		endpoint.get
			.in("draw")
			.out(jsonBody[Hand])
			.zServerLogic { _ =>
				for {
					newCard <- ZIO.succeed(deck.deal())
					updatedHand <- hand.updateAndGet(_.add(newCard))
					_ <- ZIO.logInfo(s"Added card. Hand size is now ${updatedHand.handSize}")
				} yield updatedHand
			}