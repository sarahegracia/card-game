package com.cardgame

import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*
import sttp.tapir.ztapir.*
import zio.*
import zio.json.*
import com.cardgame.deck.*
import sttp.tapir.generic.auto.*

class CardService:
	// Use jsonBody[Card] - Tapir will look for the JsonCodec[Card]
	val getCard: ZServerEndpoint[Any, Any] =
		endpoint.get
			.in("card")
			.out(jsonBody[Card])
			.zServerLogic(_ =>
				ZIO.succeed(Card(CardName.Ace, CardSuit.Spades, hidden = false))
			)