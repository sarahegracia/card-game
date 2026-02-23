package com.cardgame.games

trait CardGame {

  /**
   * Initializes/does anything needed for games
   */
  def start(): Unit

  /**
   * New game or round of card game
   */
  def newRound(): Unit

}
