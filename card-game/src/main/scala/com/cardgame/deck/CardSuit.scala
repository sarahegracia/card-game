package com.cardgame.deck

import com.cardgame.deck.Color.*

enum CardSuit(val color: Color): 
  case Diamonds extends CardSuit(Red)
  case Hearts extends CardSuit(Red)
  case Spades extends CardSuit(Black)
  case Clubs extends CardSuit(Black)
  
enum Color:
  case Black, Red 