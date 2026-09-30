package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.alpacas.AlpacaPartiesRepository
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.alpacas.PartyInfo
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.votes.District


class HomeScreenViewModel : ViewModel() {

    private val repository = AlpacaPartiesRepository()

    private val _alpacaUIstate = MutableStateFlow<AlpacaUIstate>(AlpacaUIstate.Loading)
    val alpacaUIstate = _alpacaUIstate.asStateFlow()

    private val _district = MutableStateFlow(District.DISTRICT_1)
    val district = _district.asStateFlow()

    private val _votesUIstate = MutableStateFlow<VotesUIstate>(VotesUIstate.Loading)
    val votesUIstate = _votesUIstate.asStateFlow()


    fun getParties(){
        viewModelScope.launch(Dispatchers.IO) {         //Må bruke coroutine for å kalle suspendable funksjoner, her er det viewModelScope

            val response = repository.getAllParties()  //trenger ikke try-catch her fordi det allerede ganges opp i AlpacaPartiesDataSource

            if(response.isNotEmpty()) {
                    _alpacaUIstate.value = AlpacaUIstate.Success(response)
            } else {
                _alpacaUIstate.value = AlpacaUIstate.Error("Klarte ikke å hente informasjon om partiene")
            }

        }
    }

    fun changeDistrict(newDistrict: District){
        _district.value = newDistrict
        getVotesForDistrict(newDistrict)
}

    fun getVotesForDistrict(district: District){
        viewModelScope.launch(Dispatchers.IO) {
            _votesUIstate.value = VotesUIstate.Loading          //Det er lag når man bytter districts, så har derfor med denne

            val results = repository.combineNameAndVotes(district)

            if(results.isNotEmpty()){
                _votesUIstate.value = VotesUIstate.Success(results)
            } else{
                _votesUIstate.value = VotesUIstate.Error("Klarte ikke å hente stemmer")
            }


        }
    }

    //Jeg vil at informasjonen skal hentes med en gang vi oppretter viewmodel
    init {
        getParties()
        getVotesForDistrict(_district.value)    //Kaller metoden med "default" value for _district
    }
}

sealed class AlpacaUIstate{
    data class Success(val parties : List<PartyInfo>) : AlpacaUIstate()  //Ved success har vi fått en liste med PartyInfo
    object Loading: AlpacaUIstate()
    data class Error(val message: String): AlpacaUIstate()
}

sealed class VotesUIstate{
    data class Success(val votes: List<String>) : VotesUIstate()
    object Loading: VotesUIstate()
    data class Error(val message: String): VotesUIstate()
}

