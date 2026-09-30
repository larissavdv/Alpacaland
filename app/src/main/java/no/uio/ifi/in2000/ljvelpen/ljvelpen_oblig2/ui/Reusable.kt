package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.R
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.alpacas.PartyInfo
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.theme.Ljvelpen_oblig2Theme

@Composable
fun AlpacaImage(
    modifier: Modifier = Modifier,
    party: PartyInfo
){
    AsyncImage(
        model = party.img,
        contentDescription = stringResource(R.string.partyleader_descr),
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .size(dimensionResource(R.dimen.image_size)),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun AlpacaInfo(
    modifier: Modifier = Modifier,
    party: PartyInfo
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        Text(text = party.name,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)))

        AlpacaImage(party = party)

        Text(
            text = "Leder: ${party.leader}",
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.party_color_bar))
                .background(Color(party.color.toColorInt()))
        )
    }
}


@Composable
fun LoadingComponent(
    modifier: Modifier = Modifier,
    loadingText: String
){
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        CircularProgressIndicator()
        Text(loadingText)
    }
}

@Composable
fun ErrorComponent(
    modifier: Modifier = Modifier,
    errorMessage: String
){
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(errorMessage)
    }
}

@Preview
@Composable
fun PrevInfo(){
    Ljvelpen_oblig2Theme {
        val party = PartyInfo(name = "Testname", id = "8", color = "#FF0000", img = "img", description = "Dette er en beskrivelse", leader = "lederen")
        AlpacaInfo(party = party)
    }
}

@Preview
@Composable
fun PrevLoading(){
    Ljvelpen_oblig2Theme {
        LoadingComponent(loadingText = "Loading")

    }
}

@Preview
@Composable
fun PrevError(){
    Ljvelpen_oblig2Theme {
        ErrorComponent(errorMessage = "Error")
    }
}


