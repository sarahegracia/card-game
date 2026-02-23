package com.cardgame.games

import zio.*
import zio.json.*
import com.cardgame.deck.*
import com.cardgame.games.blackjack.Blackjack
import com.cardgame.player.Player
import zio.stream.ZStream 

case class HandUpdate(playerId: String, hand: Hand)

trait CardGameService {
  def getHandStream(playerId: String): ZStream[Any, Nothing, HandUpdate]
  def dealInitialHands(players: Seq[Player]): UIO[Unit]
}

case class CardGameServiceImpl(
  hub: Hub[HandUpdate], 
  state: Ref[Map[String, Hand]],
  activeGame: Ref[Option[Blackjack]]
) extends CardGameService {

  def getHandStream(playerId: String): ZStream[Any, Nothing, HandUpdate] =
    ZStream.fromHub(hub).filter(_.playerId == playerId)

  def startBlackjack(playerIds: Seq[String]): UIO[Unit] = {
    val players = playerIds.map(id => Player(id, "dummy player " + id, 10))
    val onUpdate = (id: String, hand: Hand) => 
      hub.publish(HandUpdate(id, hand)) *> state.update(_ + (id -> hand))
    val game = new Blackjack(players, onUpdate)

    for {
      _ <- ZIO.succeed(game.initialDeal())
      _ <- activeGame.set(Some(game))
    } yield ()
  }
}

object CardGameService {
  val live: ZLayer[Any, Nothing, CardGameService] = ZLayer {
    for {
      hub   <- Hub.unbounded[HandUpdate]
      state <- Ref.make(Map.empty[String, Hand])
      active <- Ref.make(Option.empty[Blackjack])
    } yield CardGameServiceImpl(hub, state, active)
  }
}