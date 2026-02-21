package com.cardgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.openapitools.client.apis.DefaultApi
import org.openapitools.client.infrastructure.ApiClient

class CardGameActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //    val api = DefaultApi(baseUrl = "http://10.0.2.2:8080")        // for emulators
        val api = DefaultApi(baseUrl = "http://192.168.x.x:8080")      // local address

        setContent {
            App(api)
        }
    }
}