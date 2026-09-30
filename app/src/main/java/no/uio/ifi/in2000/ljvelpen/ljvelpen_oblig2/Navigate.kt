package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.home.HomeScreen
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.party.PartyScreen

@Composable
fun Navigate(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ){
        composable<HomeRoute>{
            HomeScreen(
                navigate = { pID ->
                    navController.navigate(PartyRoute(pID))     //Sender inn en funksjon(lambda) til HomeScreen(). Ved klikk navigeres det til PartyRoute med valgt ID
                }
            )
        }
        composable<PartyRoute>{ backStackentry ->
            val route: PartyRoute = backStackentry.toRoute()  //Aksesserer PartyRoute objektet med backStackEntry.toRoute()
            PartyScreen(
                partyID = route.pID,//sender pID fra PartyRoute til PartyScreen
                goback = {navController.popBackStack()}
            )
        }

    }

}

//Definerer ruta som skal ha parametre som en dataklasse
@Serializable
data class PartyRoute(
    val pID : String
)

//For ordens skyld lager jeg også et objekt for Home ruten (men denne kan være object fordi den ikke trenger parametre)
@Serializable
object HomeRoute