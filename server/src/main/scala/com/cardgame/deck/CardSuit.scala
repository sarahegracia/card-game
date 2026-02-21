package com.cardgame.deck

import Color.*
import sttp.tapir.Schema
import sttp.tapir.Schema.schemaForMap
import zio.json.{DeriveJsonCodec, JsonCodec, jsonDiscriminator}
import sttp.tapir.generic.auto.*

enum CardSuit:
  case Diamonds, Hearts, Spades, Clubs

  def color: Color = this match
    case Diamonds | Hearts => Color.Red
    case Spades | Clubs => Color.Black

object CardSuit:
  given JsonCodec[CardSuit] = DeriveJsonCodec.gen
  given Schema[CardSuit] = Schema.derivedEnumeration[CardSuit].defaultStringBased

enum Color:
  case Black, Red

object Color:
  given JsonCodec[Color] = DeriveJsonCodec.gen

  given Schema[Color] = Schema.derivedEnumeration[Color].defaultStringBased

