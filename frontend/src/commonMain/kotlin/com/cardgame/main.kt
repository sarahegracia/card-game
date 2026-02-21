package com.cardgame

//import io.ktor.client.*
//import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
//import io.ktor.serialization.kotlinx.json.json
//import kotlinx.coroutines.runBlocking
//import kotlinx.serialization.json.Json
//
//import org.openapitools.client.apis.DefaultApi
//import org.openapitools.client.models.*
//
//fun main() = runBlocking {
//    // Setup Ktor client
////    val api = DefaultApi(baseUrl = "http://localhost:8080")
//    val client = HttpClient {
//        install(ContentNegotiation) {
//            json(Json {
//                ignoreUnknownKeys = true
//                prettyPrint = true
//            })
//        }
//    }
//    val api = DefaultApi(baseUrl = "http://localhost:8080", httpClient = client)
//
//    try {
//        println("Connecting to ZIO server...")
//        val response = api.getHello("Test User")
//        if (response.status in 200..299) {
//            println("Success: ${response.body().message}")
//        } else {
//            println("Server error: ${response.status}")
//        }
//
//        println("Fetching a card...")
//        repeat(5) {
//            val cardResponse = api.getCard()
//            if (response.status in 200..299) {
//                println("Card: ${cardResponse.body().value} of ${cardResponse.body().suit}")
//                println("Hidden: ${cardResponse.body().hidden}")
//            } else {
//                println("Server error: ${response.status}")
//            }
//        }
//
//    } catch (e: Exception) {
//        println("Error: ${e.message}")
//    }
//}
//
