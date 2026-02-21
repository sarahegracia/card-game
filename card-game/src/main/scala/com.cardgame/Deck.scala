package com.cardgame

import javax.smartcardio.Card

class Deck(val numDecks: Integer = 1) {
	val deckSize = 52

	val deck = createDeck()

	def createDeck(): Seq[Card] = ???

	def deal(): Card = ???

	def shuffle(): Unit = ???

	override def toString(): String = deck.toString()
}
