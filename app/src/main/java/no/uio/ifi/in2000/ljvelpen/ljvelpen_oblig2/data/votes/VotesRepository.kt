package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.votes

import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.votes.District
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.votes.DistrictVotes


interface CollectingVotesInterface{
    suspend fun getAllVotes(district: District): List<DistrictVotes>

}

class VotesRepository() : CollectingVotesInterface{

    private val aggregatedDS = AggregatedVotesDataSource()
    private val individualDS = IndividualVotesDataSource()

    override suspend fun getAllVotes(district: District) : List<DistrictVotes> {
        val aggregated = aggregatedDS.getAggregatedVotes()  //Partier og stemmer for district 3
        val individual = individualDS.getIndividualVotes()  //Partier og stemmer for district 1 og 2

        val all = aggregated + individual   //Kombinerer stemmene for all distrikter

        return  all.filter { it.district == district}           //Returnerer en liste med alle Districtvotes for alle partier i angitt distrikt


    }
}