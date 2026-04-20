package com.cardgame.games.blackjack

import com.cardgame.deck.{Deck, Hand}
import com.cardgame.games.CardGame
import com.cardgame.player.Player
import zio.UIO

class Blackjack(
  players: Seq[Player], 
  onHandUpdate: (String, Hand) => UIO[Unit]
) extends CardGame {
  
  var hands: Map[Player, Hand] = Map.empty
  val deck: Deck = Deck()
  val initialCards = 2

  /**
   * Initializes/does anything needed for games
   */
  override def start(): Unit = ()

  /**
   * New game or round of card game
   */
  override def newRound(): Unit =
    hands = players.map(player => player -> Hand()).toMap
    initialDeal()

  def initialDeal(): Unit =
    for
      i <- 1 to initialCards
      hand <- hands.values
    do
      val card = deck.deal()
      // TODO: hide cards
      hand.add(card)
      // TODO: make better :)

    hands.foreach { case (player, hand) =>
      onHandUpdate(player.id, hand)
    }

  def showPlayerOptions(): Unit = ()
}
