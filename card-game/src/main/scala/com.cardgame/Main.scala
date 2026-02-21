package com.cardgame

import com.cardgame.deck.Deck

object Main {
  def main(args: Array[String]): Unit = {
    println("Creating a deck of cards...")
    val deck: Deck = Deck()
    println("Dealing deck:")
    deck.shuffle()
    for i <- 1 to 5 do
      println(deck.deal())
  }
}
