package com.cardgame.deck

import sttp.tapir.Schema
import zio.json.{DeriveJsonCodec, JsonCodec, jsonDiscriminator}

enum CardName:
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

object CardName:
  given JsonCodec[CardName] = DeriveJsonCodec.gen
  given Schema[CardName] = Schema.derivedEnumeration[CardName].defaultStringBased