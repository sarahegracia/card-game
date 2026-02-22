package com.cardgame.deck

class CardRankValues(val map: Map[CardRank, Int]) {
  
  def get(rank: CardRank): Int =
    map.getOrElse(rank, throw new IllegalArgumentException("Error: card value undefined"))

  
}
