package com.cardgame

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

import org.openapitools.client.apis.DefaultApi
import org.openapitools.client.models.*


fun main() = application {
    val api = DefaultApi(baseUrl = "http://localhost:8080")

    Window(onCloseRequest = ::exitApplication, title = "Card Game") {
        App(api)
    }
}