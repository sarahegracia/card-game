package com.cardgame.deck

import sttp.tapir.Schema
import zio.json.{DeriveJsonCodec, JsonCodec}

case class Hand(cards: Seq[Card] = Seq.empty) {

  def handSize: Int = cards.size
  
  def add(card: Card) = copy(cards :+ card)

  def add(cards: Card*) = copy(this.cards :++ cards)

  def play(card: Card) = copy(cards.filterNot(_ == card))

  /**
   * this lets us do play(card1, card2, card3..., cardn, cardn+1, ..)
   * combines them into a Seq[Card]
   * @param cards
   * @return
   */
  def play(cards: Card*) = copy(this.cards diff cards)
  
  def fold(): Hand = Hand()

  /** this is equivalent to <code>cards.foreach(card => card.reveal())</code> */
  def reveal(): Hand =
    cards.foreach(_.reveal())
    this

  def handValue(rankValues: CardRankValues): Int =
    cards.map(card => rankValues.get(card.rank)).sum

  override def toString(): String = cards.toString()
}

object Hand:
  given JsonCodec[Hand] = DeriveJsonCodec.gen
  given Schema[Hand] = Schema.derived
