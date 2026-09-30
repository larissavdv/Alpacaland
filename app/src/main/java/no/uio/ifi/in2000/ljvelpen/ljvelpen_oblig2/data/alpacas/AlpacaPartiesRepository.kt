package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.alpacas

import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.votes.VotesRepository
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.alpacas.PartyInfo
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.votes.District


//På gruppetime ble det sagt at det er "best practice" å bruke et interface til å implementere repository

interface AlpacaPartiesInterface{
    suspend fun getAllParties() : List<PartyInfo>                           //Brukes av HomeScreenViewModel for å vise frem HomeScreen
    suspend fun getPartyInfo(partyID: String) : PartyInfo?                  //Denne brukes i PartyViewModel, for å vise all informasjon om et gitt parti
    suspend fun combineNameAndVotes(district: District): List<String>       //Brukes i HomeScreen for å kunne lage VoteList
}


class AlpacaPartiesRepository() : AlpacaPartiesInterface {
    private val datasource = AlpacaPartiesDataSource()
    private val votesRepo = VotesRepository()


    override suspend fun getAllParties(): List<PartyInfo> {          //Må huske å bruke suspend her også, fordi getParties() er en suspendable også
        return datasource.getParties()                              //returnerer List<PartyInfo>
    }

    override suspend fun getPartyInfo(partyID: String): PartyInfo? {
        val parties = datasource.getParties()
        val thisPartyInfo = parties.find{it.id == partyID}

        return thisPartyInfo

    }

    //Hjelpemetode for å hente navnet til et parti
    private suspend fun getName(partyID: String): String?{
        val parties = datasource.getParties()   //returnerer en liste med PartyInfo
        val party = parties.find { it.id == partyID }

        if(party != null){
            return party.name
        }

        return null
    }

    //Hjelpemetode for å hente stemmene til et parti for et gitt district
    private suspend fun getVotes(district: District, partyID: String): Int?{
        val votes = votesRepo.getAllVotes(district = district) //returnerer en liste med DistrictVotes for alle partier for gitt district
        val party = votes.find { it.alpacaPartyId == partyID }

        if(party != null){
            return party.numberOfVotesForParty
        }
        return null
    }

    override suspend fun combineNameAndVotes(district: District): List<String> {
        val votes = votesRepo.getAllVotes(district) //Returnerer en liste med DistrictVotes for alle partier i det angitte districtet
        val nameAndVoteList : MutableList<String> = mutableListOf()

        votes.forEach {
            val name = getName(it.alpacaPartyId)
            val votes = getVotes(district, it.alpacaPartyId)

            val concatenatedString = "$name,$votes"

            nameAndVoteList.add(concatenatedString)

        }

        return nameAndVoteList
    }
}

