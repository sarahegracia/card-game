package com.cardgame

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
    var cardText by remember { mutableStateOf("No card drawn") }
    val scope = rememberCoroutineScope()

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(cardText, style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                scope.launch {
                    try {
                        val response = api.getCard()
                        val card = response.body()
                        cardText = "${card.value} of ${card.suit}"
                        println("Card drawn: ${card.value} of ${card.suit}")
                    } catch (e: Exception) {
                        cardText = "Error fetching card"
                    }
                }
            }) {
                Text("Draw a Card")
            }
        }
    }
}