package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.R
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.alpacas.PartyInfo
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.votes.District
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.AlpacaInfo
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.ErrorComponent
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.LoadingComponent
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.theme.Ljvelpen_oblig2Theme


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    vm: HomeScreenViewModel = viewModel(),   //Bruker viewModel() og ikke HomeScreenViewModel() fordi viewmodel "gjenbrukes" ved rekomposisjon
    navigate: (String) -> Unit
){

    val alpacaState by vm.alpacaUIstate.collectAsState()           //CollectAsState gjør at HomeScreen observerer Statene i viewmodel
    val chosenDistrict by vm.district.collectAsState()
    val voteState by vm.votesUIstate.collectAsState()

    when(alpacaState){
        is AlpacaUIstate.Loading ->
            LoadingComponent(
                modifier = modifier,
                loadingText = stringResource(R.string.loading))
        is AlpacaUIstate.Error ->
            ErrorComponent(
                modifier = modifier,
                errorMessage = (alpacaState as AlpacaUIstate.Error).message)  //"Sjekker" at AlpacaUIstate faktisk er i Error
        is AlpacaUIstate.Success -> {
            val parties = (alpacaState as AlpacaUIstate.Success).parties //Sjekker at AlpacaUIstate faktisk er i Success
            BuildHomeScreen(
                modifier = modifier,
                parties = parties,
                navigate = navigate,

                chosenDistrict = chosenDistrict,
                changeDistrict = {district -> vm.changeDistrict(district)},
                voteState = voteState,
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)  //må ha med denne pga. TopAppBar
@Composable
fun BuildHomeScreen(
    modifier: Modifier = Modifier,
    parties: List<PartyInfo>,
    navigate: (String) -> Unit,

    chosenDistrict: District,
    changeDistrict: (District) -> Unit,
    voteState: VotesUIstate
){

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                    text = stringResource(R.string.partier),
                    style = MaterialTheme.typography.titleLarge)
                }
            )
        }
    ) { innerPadding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium)),
                modifier = Modifier
                    .fillMaxSize()
                    .padding((innerPadding))
            ) {
                items(parties){
                    AlpacaCard(
                        party = it,
                        onClick = {navigate(it.id)}
                    )
                }
                item(
                    span = { GridItemSpan(maxLineSpan) }        //maxLineSpan gjør at den ligger over hele bredden av gridet
                ){
                    DropDown(
                        chosenDistrict = chosenDistrict,
                        changeDistrict = changeDistrict
                        )
                }
                item(
                    span = {GridItemSpan(maxLineSpan)}
                ) {
                    when(voteState){
                        is VotesUIstate.Loading ->
                            LoadingComponent(
                                modifier = modifier,
                                loadingText = stringResource(R.string.loading_votes))
                        is VotesUIstate.Error ->
                            ErrorComponent(
                                modifier = modifier,
                                errorMessage = voteState.message)
                        is VotesUIstate.Success -> {
                            val results = voteState.votes
                            VoteList(votes =results)
                        }
                    }
                }
            }

    }
}


@Composable
fun AlpacaCard(
    modifier: Modifier = Modifier,
    party: PartyInfo,
    onClick: () -> Unit
){
    Card(
        modifier = modifier
            .clickable{onClick()}       //Hele Card feltet blir clickable
    ){
        AlpacaInfo(party = party)
    }
}


@Composable
fun DropDown(
    modifier: Modifier = Modifier,
    chosenDistrict: District,
    changeDistrict: (District) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .padding(
                bottom = dimensionResource(R.dimen.padding_medium),
                top = dimensionResource(R.dimen.padding_medium)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.valgresultater),
            style = MaterialTheme.typography.titleSmall)

        Box (
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(color = MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Row(
                modifier = Modifier
                    .clickable { expanded = true }
                    .padding(
                        horizontal = dimensionResource(R.dimen.padding_small),
                        vertical = dimensionResource(R.dimen.padding_small)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chosenDistrict.id.lowercase(),
                    style = MaterialTheme.typography.titleSmall)
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = stringResource(R.string.velg_distrikt)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                for (district in District.entries) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = district.id,
                                textAlign = TextAlign.Center
                            )
                        },
                        onClick = {
                            changeDistrict(district)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PrevAlpacaScreen(modifier: Modifier = Modifier){     //Visning av bilde i preview fungerer ikke fordi preview ikke gjør internett-kall
    Ljvelpen_oblig2Theme {
        //Lager en liste med "mock" data
        val list : MutableList<PartyInfo> = mutableListOf()

        for(i in 1..4){
            list.add(PartyInfo("ID: i","Name nr. $i", "Leader nr. $i", "https://www.saginawzoo.com/wp-content/uploads/2025/05/alpaca.jpg", "#FF0000", "Description nr. $i" ))
            }

        BuildHomeScreen(
            modifier = modifier,
            parties = list,
            navigate = {},
            chosenDistrict = District.DISTRICT_3,
            changeDistrict = {},
            voteState = VotesUIstate.Success(listOf("AlpacaNorth,1234", "AlpacaWest,5678", "AlpacaEast,9112", "AlpacaSouth,3456")))
    }
}


