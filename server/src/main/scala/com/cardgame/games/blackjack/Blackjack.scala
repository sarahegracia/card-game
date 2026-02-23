package com.cardgame.games.blackjack

import com.cardgame.deck.{Deck, Hand}
import com.cardgame.games.CardGame
import com.cardgame.player.Player

class Blackjack(players: Seq[Player]) extends CardGame {
  val hands: Map[Player, Hand] = players.map(player => (player -> Hand())).toMap
  val deck: Deck = Deck()
  val initialCards = 2

  /**
   * Initializes/does anything needed for games
   */
  override def start(): Unit = ()

  /**
   * New game or round of card game
   */
  override def newRound(): Unit = ???

  def initialDeal(): Unit = {
    for {
      i <- 1 to initialCards
      hand <- hands.values
    } {
      val card = deck.deal()
      // TODO: hide cards
      hand.add(card)
      // TODO: make better :)
    }
    hands.keys.foreach(player => updatePlayerHand(player))
  }

  def showPlayerOptions(): Unit = ()
}
