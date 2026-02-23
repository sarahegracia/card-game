package com.cardgame

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
                    errorText = "[Error drawing card]"
                    e.printStackTrace()
                }
            }
        },
        onHandFold = {
            scope.launch {
                try {
                    val response = api.getFold()
                    hand = response.body()
                    errorText = ""
                } catch (e: Exception) {
                    errorText = "[Error folding hand]"
                    e.printStackTrace()
                }
            }
        },
        onHandReveal = {
            scope.launch {
                try {
                    val response = api.getReveal()
                    hand = response.body()
                    errorText = ""
                } catch (e: Exception) {
                    errorText = "[Error folding hand]"
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
    onDrawClick: () -> Unit = {},
    onHandFold: () -> Unit = {},
    onHandReveal: () -> Unit = {},
) {
    val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))
                Text("Hand", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(20.dp))

                // hand
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    hand?.cards?.forEach { card ->
                        Box(modifier = Modifier.weight(1f, fill = false).widthIn(max = 120.dp, min = 40.dp)) {
                            CardImage(card)
                        }
                    }
                }

                if (error.isNotBlank()) {
                    Text(error, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(onClick = onDrawClick, modifier = Modifier.padding(16.dp)) {
                        Text("Draw a Card")
                    }
                    Button(onClick = onHandFold, modifier = Modifier.padding(16.dp)) {
                        Text("Fold")
                    }
                    Button(onClick = onHandReveal, modifier = Modifier.padding(16.dp)) {
                        Text("Reveal")
                    }
                }

            }
        }
    }
}

@Composable
fun AppPreview() {
    AppContent(
        hand = null,
        error = "Preview Mode: No API",
        onDrawClick = {}
    )
}