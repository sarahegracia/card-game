package com.cardgame.deck

import com.cardgame.deck.Card

import scala.util.Random

class Deck(val numDecks: Integer = 1) {
	val deckSize = 52

	var deck = createDeck()

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
