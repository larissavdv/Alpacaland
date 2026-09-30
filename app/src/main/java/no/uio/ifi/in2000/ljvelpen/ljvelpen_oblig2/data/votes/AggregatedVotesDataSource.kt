package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.votes

import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.network.KtorCLient
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.votes.District
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.votes.DistrictVotes


//Lager 2 private dataklasser for å kunne håndtere responsen fra API-kallet riktig.
@Serializable
private data class PartyVotes(val parties: List<AggregatedVotes>)  //API kallet returnerer en liste av "aggregated" stemmeobjekter

@Serializable
private data class AggregatedVotes(val partyId: String, val votes: Int)


class AggregatedVotesDataSource (){

    suspend fun getAggregatedVotes() : List<DistrictVotes>{
        val httpResponse = KtorCLient.client.get("https://in2000-proxy.ifi.uio.no/alpacaapi/v2/district3")

        var votes: PartyVotes
        val aggregatedVotes: MutableList<DistrictVotes> = mutableListOf()

        try {
            println("Prøver å hente kombinerte stemmer for distrikt 3")
            votes = httpResponse.body()

        }catch (e: Exception){
            println("Klarte ikke å hente kombinerte stemmer for distrikt 3")
            println("Årsak:\n${e.message}")
            votes = PartyVotes(listOf())
        }

        votes.parties.forEach {
            aggregatedVotes.add(DistrictVotes(district = District.DISTRICT_3, alpacaPartyId = it.partyId, numberOfVotesForParty = it.votes))

        }

        return aggregatedVotes

    }
}
