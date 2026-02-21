package com.cardgame.deck

class Card(val value: CardName, val suit: CardSuit, var hidden: Boolean = true):
  
  def flip(): Boolean = 
    hidden = !hidden
    hidden

  def reveal(): Boolean =
    hidden = false
    hidden

  def hide(): Boolean = 
    !reveal()
  