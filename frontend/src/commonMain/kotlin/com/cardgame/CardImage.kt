package com.cardgame

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio // Needed for sizing
import androidx.compose.foundation.layout.padding
import frontend.generated.resources.Res
import frontend.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.openapitools.client.models.Card
import org.openapitools.client.models.CardRank
import org.openapitools.client.models.CardSuit

@Composable
fun CardImage(card: Card) {
    var desc: String
    if (card.hidden) {
        desc = "Back of card"
    } else {
        val rank = card.rank.value
        val suit = card.suit.value
        desc = "$rank of $suit"
    }
    Image(
        painter = painterResource(card.drawable()),
        contentDescription = desc,
        modifier = Modifier
            .width(100.dp)
            .aspectRatio(0.7f)
            .padding(4.dp)
    )
}

fun Card.drawable(): DrawableResource =
    if (hidden) {
        Res.drawable.card_back
    } else {
        cardDrawable(rank, suit)
    }

fun cardDrawable(rank: CardRank, suit: CardSuit): DrawableResource =
    when (rank to suit) {
        CardRank.ACE   to CardSuit.SPADES   -> Res.drawable.spades_ace
        CardRank.ACE   to CardSuit.HEARTS   -> Res.drawable.hearts_ace
        CardRank.ACE   to CardSuit.DIAMONDS -> Res.drawable.diamonds_ace
        CardRank.ACE   to CardSuit.CLUBS    -> Res.drawable.clubs_ace

        CardRank.TWO   to CardSuit.SPADES   -> Res.drawable.spades_2
        CardRank.TWO   to CardSuit.HEARTS   -> Res.drawable.hearts_2
        CardRank.TWO   to CardSuit.DIAMONDS -> Res.drawable.diamonds_2
        CardRank.TWO   to CardSuit.CLUBS    -> Res.drawable.clubs_2

        CardRank.THREE to CardSuit.SPADES -> Res.drawable.spades_3
        CardRank.THREE to CardSuit.HEARTS -> Res.drawable.hearts_3
        CardRank.THREE to CardSuit.DIAMONDS -> Res.drawable.diamonds_3
        CardRank.THREE to CardSuit.CLUBS -> Res.drawable.clubs_3

        CardRank.FOUR to CardSuit.SPADES -> Res.drawable.spades_4
        CardRank.FOUR to CardSuit.HEARTS -> Res.drawable.hearts_4
        CardRank.FOUR to CardSuit.DIAMONDS -> Res.drawable.diamonds_4
        CardRank.FOUR to CardSuit.CLUBS -> Res.drawable.clubs_4

        CardRank.FIVE to CardSuit.SPADES -> Res.drawable.spades_5
        CardRank.FIVE to CardSuit.HEARTS -> Res.drawable.hearts_5
        CardRank.FIVE to CardSuit.DIAMONDS -> Res.drawable.diamonds_5
        CardRank.FIVE to CardSuit.CLUBS -> Res.drawable.clubs_5

        CardRank.SIX to CardSuit.SPADES -> Res.drawable.spades_6
        CardRank.SIX to CardSuit.HEARTS -> Res.drawable.hearts_6
        CardRank.SIX to CardSuit.DIAMONDS -> Res.drawable.diamonds_6
        CardRank.SIX to CardSuit.CLUBS -> Res.drawable.clubs_6

        CardRank.SEVEN to CardSuit.SPADES -> Res.drawable.spades_7
        CardRank.SEVEN to CardSuit.HEARTS -> Res.drawable.hearts_7
        CardRank.SEVEN to CardSuit.DIAMONDS -> Res.drawable.diamonds_7
        CardRank.SEVEN to CardSuit.CLUBS -> Res.drawable.clubs_7

        CardRank.EIGHT to CardSuit.SPADES -> Res.drawable.spades_8
        CardRank.EIGHT to CardSuit.HEARTS -> Res.drawable.hearts_8
        CardRank.EIGHT to CardSuit.DIAMONDS -> Res.drawable.diamonds_8
        CardRank.EIGHT to CardSuit.CLUBS -> Res.drawable.clubs_8

        CardRank.NINE to CardSuit.SPADES -> Res.drawable.spades_9
        CardRank.NINE to CardSuit.HEARTS -> Res.drawable.hearts_9
        CardRank.NINE to CardSuit.DIAMONDS -> Res.drawable.diamonds_9
        CardRank.NINE to CardSuit.CLUBS -> Res.drawable.clubs_9

        CardRank.TEN to CardSuit.SPADES -> Res.drawable.spades_10
        CardRank.TEN to CardSuit.HEARTS -> Res.drawable.hearts_10
        CardRank.TEN to CardSuit.DIAMONDS -> Res.drawable.diamonds_10
        CardRank.TEN to CardSuit.CLUBS -> Res.drawable.clubs_10

        CardRank.JACK to CardSuit.SPADES -> Res.drawable.spades_jack
        CardRank.JACK to CardSuit.HEARTS -> Res.drawable.hearts_jack
        CardRank.JACK to CardSuit.DIAMONDS -> Res.drawable.diamonds_jack
        CardRank.JACK to CardSuit.CLUBS -> Res.drawable.clubs_jack

        CardRank.QUEEN to CardSuit.SPADES -> Res.drawable.spades_queen
        CardRank.QUEEN to CardSuit.HEARTS -> Res.drawable.hearts_queen
        CardRank.QUEEN to CardSuit.DIAMONDS -> Res.drawable.diamonds_queen
        CardRank.QUEEN to CardSuit.CLUBS -> Res.drawable.clubs_queen

        CardRank.KING to CardSuit.SPADES -> Res.drawable.spades_king
        CardRank.KING to CardSuit.HEARTS -> Res.drawable.hearts_king
        CardRank.KING to CardSuit.DIAMONDS -> Res.drawable.diamonds_king
        CardRank.KING to CardSuit.CLUBS -> Res.drawable.clubs_king

        else -> error("Unhandled card: $rank of $suit")
    }