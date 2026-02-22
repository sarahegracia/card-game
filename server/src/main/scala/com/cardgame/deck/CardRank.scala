package com.cardgame.deck

import sttp.tapir.Schema
import zio.json.{DeriveJsonCodec, JsonCodec, jsonDiscriminator}

enum CardRank:
  case
  Ace,
  Two,
  Three,
  Four,
  Five,
  Six,
  Seven,
  Eight,
  Nine,
  Ten,
  Jack,
  Queen,
  King

object CardRank:
  given JsonCodec[CardRank] = DeriveJsonCodec.gen
  given Schema[CardRank] = Schema.derivedEnumeration[CardRank].defaultStringBased