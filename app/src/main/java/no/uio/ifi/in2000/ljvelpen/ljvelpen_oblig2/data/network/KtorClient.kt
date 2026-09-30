package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorCLient {

    //Lager selve HTTPclienten i en egen (object) klasse KrotClient
    //Da slipper vi å gjøre det i selve AplacaPartiesDataSource klassen, men kan kalle KtorClient.client direkte derfra

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true  //Denne gjør at den kan "ignorere" enkelte syntaksfeil i JSON-responsen
                ignoreUnknownKeys = true  //I tilfelle det er noen keys i JSON responsen som vi ikke har deklarert i dataklassen
            })
        }
    }

}