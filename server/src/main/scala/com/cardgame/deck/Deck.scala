package com.cardgame.deck

import scala.util.Random

/**
 * A deck (or deck of decks) of cards.
 * @param numDecks the number of decks to create; defaults to a single deck.
 */
class Deck(val numDecks: Int = 1, val shuffles: Int = 1) {

	val deckSize = CardRank.values.length * CardSuit.values.length
	val decksSize: Int = numDecks * deckSize

	var deck: Seq[Card] = createDeck()
	
	for
		i <- 1 to shuffles
		if shuffles > 0
	do
		shuffle()

	def createDeck(): Seq[Card] =
		for
			value <- CardRank.values
			suit <- CardSuit.values
		yield
			Card(value, suit)

	def deal(): Card =
		val card = deck.headOption
		card match
			case Some(value) =>
				deck = deck.tail
				value
			case None	=>
				// TODO: improve by using a discard pile
				val newDeck = createDeck()
				val newCard = newDeck.head
				deck = newDeck.tail
				newCard

	def shuffle(): Unit = deck = Random.shuffle(deck)

	def size: Int = deck.size
	
	override def toString(): String = deck.toString()
}
