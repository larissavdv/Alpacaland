package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.party

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.R
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.alpacas.PartyInfo
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.AlpacaInfo
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.ErrorComponent
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.LoadingComponent
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.theme.Ljvelpen_oblig2Theme


@Composable
fun PartyScreen(
    modifier: Modifier = Modifier,
    partyVM: PartyViewModel = viewModel(),
    partyID: String,
    goback: () -> Unit
){
    val state by partyVM.partyState.collectAsState()

    LaunchedEffect(partyID) //Siden vi ikke kan kjøre getInfo med init i PartyViewModel (fordi den trenger parameter) må vi kalle den herfra med LaunchedEffekt
    {
        partyVM.getInfo(partyID)
    }

    when(state){
        is PartyUIstate.Loading -> {
            LoadingComponent(
                modifier = modifier,
                loadingText = stringResource(R.string.loading)
            )
        }
        is PartyUIstate.Error -> {
            ErrorComponent(
                modifier = modifier,
                errorMessage = (state as PartyUIstate.Error).message)
        }
        is PartyUIstate.Success -> {
            val partyInfo = (state as PartyUIstate.Success).info
            BuildPartyScreen(
                modifier = modifier,
                party = partyInfo,
                goback = goback)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildPartyScreen(
    modifier: Modifier = Modifier,
    party: PartyInfo,
    goback: () -> Unit
){
   Scaffold(
       modifier = modifier,
       topBar = {
           TopAppBar(
               title ={
                   Text(
                       stringResource(R.string.informasjon),
                       style = MaterialTheme.typography.titleSmall
                       ) },
               navigationIcon = {
                   IconButton(onClick = goback) {
                       Icon(
                           imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                           contentDescription = stringResource(R.string.tilbake)
                       )
                   }
               }
           )
       }
   ) {
       innerpadding ->
       LazyColumn(
           modifier = Modifier
               .padding(innerpadding)
               .fillMaxWidth(),
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
       ) {
           item { AlpacaInfo(party = party) }
           item { Text(text = party.description) }
       }
   }
}


@Preview
@Composable
fun PrevPartyScreen(
){
    val party = PartyInfo(name = "Partynavn", id = "5", color = "#FF0000", img = "img", description = "Beskrivelse", leader = "ledernavn")
    Ljvelpen_oblig2Theme {
        BuildPartyScreen(party = party, goback = {})
    }
}