package com.cardgame.deck

import zio.json.*
import sttp.tapir.Schema

case class Card(rank: CardRank, suit: CardSuit, var hidden: Boolean = true):
  
  def flip(): Boolean = 
    hidden = !hidden
    hidden

  def reveal(): Boolean =
    hidden = false
    hidden

  def hide(): Boolean =
    hidden = true
    hidden

object Card:
  given JsonCodec[Card] = DeriveJsonCodec.gen
  given Schema[Card] = Schema.derived