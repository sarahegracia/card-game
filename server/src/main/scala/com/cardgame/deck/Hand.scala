package com.cardgame.deck

import scala.collection.mutable.ListBuffer

class Hand {
  val cards: ListBuffer[Card] = ListBuffer.empty
  
  def handSize: Int = cards.size
  
  def add(card: Card) = cards += card

  def remove(card: Card) = cards -= card
  
  def reveal(): Unit =
    cards.foreach(_.reveal())

  override def toString(): String = cards.toString()
}
