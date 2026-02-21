package com.cardgame.deck

import com.cardgame.deck.Card

import scala.util.Random

/**
 * A deck (or deck of decks) of cards.
 * @param numDecks the number of decks to create; defaults to a single deck.
 */
class Deck(val numDecks: Integer = 1) {
	val deckSize = CardName.values.length * CardSuit.values.length
	val decksSize: Int = numDecks * deckSize

	var deck: Seq[Card] = createDeck()

	def createDeck(): Seq[Card] =
		for
			value <- CardName.values
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
				val newCard = deck.head
				deck = deck.tail
				newCard

	def shuffle(): Unit = deck = Random.shuffle(deck)

	override def toString(): String = deck.toString()
}
