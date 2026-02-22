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
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun App(api: DefaultApi) {
    var hand by remember { mutableStateOf<org.openapitools.client.models.Hand?>(null) }
    var errorText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    AppContent(
        hand = hand,
        error = errorText,
        onDrawClick = {
            scope.launch {
                try {
                    val response = api.getDraw()
                    hand = response.body()
                    errorText = ""
                } catch (e: Exception) {
                    errorText = "[Error fetching card]"
                    e.printStackTrace()
                }
            }
        }
    )
}

@Composable
fun AppContent(
    hand: org.openapitools.client.models.Hand?,
    error: String,
    onDrawClick: () -> Unit = {}
) {
    val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

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

                hand?.cards?.forEach { card ->
                    Text(
                        text = "🎴 ${card.rank} of ${card.suit}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(onClick = onDrawClick) {
                    Text("Draw a Card")
                }
            }
        }
    }
}

@Preview
@Composable
fun AppPreview(apiLevel: Int = 35) {
    // val api = DefaultApi(baseUrl = "http://192.168.1.88:8080")
    App(DefaultApi())
}