package com.cardgame

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.openapitools.client.apis.DefaultApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun App(api: DefaultApi) {
    var hand by remember { mutableStateOf<org.openapitools.client.models.Hand?>(null) }
    var cardText by remember { mutableStateOf("No card drawn") }
    val scope = rememberCoroutineScope()

    val darkTheme = isSystemInDarkTheme()
    val colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display full hand
                Text("Current Hand", style = MaterialTheme.typography.headlineLarge)

                Spacer(modifier = Modifier.height(20.dp))

                // The lazy loop: print every card in the hand
                hand?.cards?.forEach { card ->
                    Text(
                        text = "🎴 ${card.rank} of ${card.suit}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // Pushes button to bottom

                Button(onClick = {
                    scope.launch {
                        try {
                            val response = api.getDraw()
                            hand = response.body()
//                            val response = api.getCard()
//                            val card = response.body()
//                            cardText = "${card.value} of ${card.suit}"
//                            println("Card drawn: ${card.value} of ${card.suit}")
                        } catch (e: Exception) {
                            e.printStackTrace()
                            cardText = "[Error fetching card]"
                        }
                    }
                }) {
                    Text("Draw a Card")
                }
            }
        }
    }
}