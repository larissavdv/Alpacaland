package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.alpacas

import io.ktor.client.call.body
import io.ktor.client.request.get
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.network.KtorCLient
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.alpacas.Parties
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.alpacas.PartyInfo


class AlpacaPartiesDataSource{

    //Må bruke suspend for at UIet ikke skal "fryses" underveis mens kallet kjøres
    suspend fun getParties() : List<PartyInfo>{
        val httpResponse = KtorCLient.client.get("https://in2000-proxy.ifi.uio.no/alpacaapi/v2/alpacaparties")
        var parties: Parties

        try {
            println("Prøver å hente informasjon om Alpaca partiene")
            parties = httpResponse.body()
        } catch (e: Exception) {
            println("Kunne ikke hente informasjon Alpaca partiene")
            println("Årsak:\n${e.message}")
            parties = Parties(listOf())   //Lager en tom liste så den ikke kræsjer
        }

        return parties.parties

    }
}