package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.alpacas.AlpacaPartiesDataSource
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.alpacas.AlpacaPartiesRepository
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.votes.AggregatedVotesDataSource
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.votes.IndividualVotesDataSource
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.votes.VotesRepository
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.alpacas.PartyInfo
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.votes.District
import org.junit.Assert.assertTrue
import org.junit.Test

class AlpacaTests {

    @Test
    fun checkResponseNotEmptyAlpacaPartiesDC(){
        val datasource = AlpacaPartiesDataSource()
        runBlocking {                           //Bruker rundblocking fordi vi kaller en suspend funksjon (som må kalles fra en annen suspend)
            val result : List<PartyInfo> = datasource.getParties()
            assertTrue(result.isNotEmpty())

        }
    }

    @Test
    fun checkGetPartiesAlpacaPartiesDCIsCorrect(){
        val dataSource = AlpacaPartiesDataSource()
        runBlocking {
            val result = dataSource.getParties()
            assertEquals("1", result[0].id)
            assertEquals("AlpacaNorth", result[0].name)
            assertEquals("Chewpaca", result[0].leader)
            assertEquals("https://in2000-proxy.ifi.uio.no/alpacaapi/v2/assets/18788507266", result[0].img)
            assertEquals("#edb879", result[0].color)

        }
    }

    @Test
    fun testAlpacaPartiesRepository(){
        val repo = AlpacaPartiesRepository()
        runBlocking {
            val result = repo.getAllParties()
            assertEquals("1", result[0].id)
            assertEquals("AlpacaNorth", result[0].name)
            assertEquals("Chewpaca", result[0].leader)
            assertEquals("https://in2000-proxy.ifi.uio.no/alpacaapi/v2/assets/18788507266", result[0].img)
            assertEquals("#edb879", result[0].color)
        }
    }

    @Test
    fun testIndividualVotesNotEmpty(){
        val datasource = IndividualVotesDataSource()
        runBlocking {
            val result = datasource.getIndividualVotes()
            assertTrue(result.isNotEmpty())
            assertTrue(result.size == 8)
        }
    }

    @Test
    fun testAgrregatedVotesNotEmpty(){
        val dataSource = AggregatedVotesDataSource()
        runBlocking {
            val result = dataSource.getAggregatedVotes()
            assertTrue(result.isNotEmpty())
            assertTrue(result.size == 4)
        }
    }

    @Test
    fun testVotesRepositoryReturns4Elements(){
        val repo = VotesRepository()
        runBlocking {
            val result = repo.getAllVotes(District.DISTRICT_1)
            assertTrue(result.size == 4)
        }
    }
}
