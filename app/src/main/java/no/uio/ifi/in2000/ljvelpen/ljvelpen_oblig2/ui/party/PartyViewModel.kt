package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.party

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.data.alpacas.AlpacaPartiesRepository
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.alpacas.PartyInfo


class PartyViewModel(): ViewModel(){
    private val repository = AlpacaPartiesRepository()

    private val _partyState = MutableStateFlow<PartyUIstate>(PartyUIstate.Loading)
    val partyState = _partyState.asStateFlow()

    fun getInfo(partyID: String){
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.getPartyInfo(partyID)
            if (result != null){
                _partyState.value = PartyUIstate.Success(result)
            } else{
                _partyState.value = PartyUIstate.Error("Kunne ikke hente informasjon om partiet")
            }
        }
    }
}

sealed class PartyUIstate{
    data class Success(val info: PartyInfo) : PartyUIstate()
    object Loading : PartyUIstate()
    data class Error(val message: String): PartyUIstate()
}