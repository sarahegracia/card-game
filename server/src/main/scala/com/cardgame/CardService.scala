package com.cardgame

import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*
import sttp.tapir.ztapir.*
import zio.*
import zio.json.*
import com.cardgame.deck.*
import sttp.tapir.generic.auto.*

class CardService(deck: Deck):

	val getCard: ZServerEndpoint[Any, Any] =
		endpoint.get
			.in("card")
			.out(jsonBody[Card])
			.zServerLogic(_ =>
				ZIO.succeed(deck.deal())
			)