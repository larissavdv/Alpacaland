package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.R
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.theme.Ljvelpen_oblig2Theme


@Composable
fun VoteList(
    modifier: Modifier = Modifier,
    votes: List<String>) {
    Card(
        modifier = modifier){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Row{
                Text(
                    text = stringResource(R.string.parti),
                    fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(R.string.antall_stemmer),
                    fontWeight = FontWeight.Bold)
            }

            votes.forEach {
                val parts = it.split(",")
                Row{
                    Text(text = parts[0])
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = parts[1])
                }
            }
        }
}
    }

@Preview
@Composable
fun PrevVoteList() {
    Ljvelpen_oblig2Theme {
        val votes = listOf("AlpacaNorth,1234", "AlpacaWest,5678", "AlpacaEast,9112", "AlpacaSouth,3456")
        VoteList(votes = votes)
    }
}