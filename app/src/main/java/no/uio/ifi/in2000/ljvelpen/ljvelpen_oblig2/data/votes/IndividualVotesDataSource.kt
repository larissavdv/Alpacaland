package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.votes

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.network.KtorCLient
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.votes.District
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.votes.DistrictVotes



//Lager en privat dataklasse Vote fordi API-kallet returner en liste av "stemme" objekter
@Serializable
private data class Vote(
    val id: String
)



class IndividualVotesDataSource {

    suspend fun getIndividualVotes(): List<DistrictVotes> {
        val d1 = KtorCLient.client.get("https://in2000-proxy.ifi.uio.no/alpacaapi/v2/district1")
        val d2 = KtorCLient.client.get("https://in2000-proxy.ifi.uio.no/alpacaapi/v2/district2")

        val individualVotes: MutableList<DistrictVotes> = mutableListOf()

        alter(d1, District.DISTRICT_1, individualVotes)
        alter(d2, District.DISTRICT_2, individualVotes)

        return individualVotes

    }

    //En private hjelpemetode for å transformere resultatene til DistrictVotes
    private suspend fun alter(
        response: HttpResponse,
        district: District,
        individualVotes: MutableList<DistrictVotes>
        ){

        var votes: List<Vote>

        try {
            println("Prøver å hente data for $district:")
            votes = response.body()
        } catch (e: Exception){
            println("Klarte ikke å hente data for $district")
            println("Årsak:\n${e.message}")
            votes = listOf()
        }

        val occurrence = votes
            .groupingBy { it.id } //Grupperer alle Votes etter ID
            .eachCount()        //.. og teller hvor mange ganger en ID forekommer. x forekomster tilsvarer x stemmer.

        for((key, value ) in occurrence){               //Legger til i individualvotes lista som er deklarert i getIndividualVotes
            individualVotes.add(
                DistrictVotes(
                    district = district,
                    alpacaPartyId = key,
                    numberOfVotesForParty = value
            ))
        }
    }
}

