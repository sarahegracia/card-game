package com.cardgame.blackjack

import com.cardgame.deck.{Deck, Hand}
import com.cardgame.player.Player

class Blackjack(players: Seq[Player]) {
  val hands: Map[Player, Hand] = players.map(player => (player -> Hand())).toMap
  val deck: Deck = Deck()
  val initialCards = 2
 def initialDeal(): Unit =
   for {
     i <- 1 to initialCards
     hand <- hands.values
   } {
     val card = deck.deal()
     // TODO: hide cards 
     hand.add(card)
     // TODO: make better :)
   }
 
 def showPlayerOptions(): Unit = ()
   
}
