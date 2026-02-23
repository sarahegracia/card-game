package com.cardgame.games

import zio.*
import zio.json.*
import com.cardgame.deck.*
import com.cardgame.player.Player
import zio.stream.ZStream 

case class HandUpdate(playerId: String, hand: Hand)

trait CardGameService {
  def getHandStream(playerId: String): ZStream[Any, Nothing, HandUpdate]
  def dealInitialHands(players: Seq[Player]): UIO[Unit]
}

case class CardGameServiceImpl(hub: Hub[HandUpdate], state: Ref[Map[String, Hand]]) extends CardGameService {

  def getHandStream(playerId: String): ZStream[Any, Nothing, HandUpdate] =
    ZStream.fromHub(hub).filter(_.playerId == playerId)

  def dealInitialHands(players: Seq[Player]): UIO[Unit] = {
    ZIO.foreachDiscard(players) { player =>
      val newHand = Hand(???) // Logic for dealing
      for {
        _ <- state.update(_ + (player.id -> newHand))
        _ <- hub.publish(HandUpdate(player.id, newHand))
      } yield ()
    }
  }
}

object CardGameService {
  val layer: ZLayer[Any, Nothing, CardGameService] = ZLayer {
    for {
      hub   <- Hub.unbounded[HandUpdate]
      state <- Ref.make(Map.empty[String, Hand])
    } yield CardGameServiceImpl(hub, state)
  }
}