# Alpakkaland Election App

An Android app that shows the election results in the fictional Alpakkaland, along with information about the four alpaca parties and their leaders. Made as an assignment in IN2000 at the University of Oslo, spring 2026.

## Features

- **Party overview:** The home screen shows a card for each party in a two-column grid, with the party name, leader, leader photo and party color.
- **Party details:** Tapping a card opens a detail screen with a description of the party leader. The top app bar has a back button to return home.
- **Election results:** Below the party cards, a table shows the number of votes per party. A dropdown menu switches between the three districts. The selected district is kept when you return from the detail screen.
- **Loading and error states:** Every screen shows a loading indicator while data is fetched and an error message if nothing could be loaded.

## Tech stack

- Kotlin
- Jetpack Compose with Material Design 3
- Jetpack ViewModel with `StateFlow` for UI state
- Navigation Compose with type-safe routes (`@Serializable` route classes)
- Ktor client (CIO engine) with kotlinx.serialization for network requests
- Coil 3 for loading images from URLs
- JUnit for unit tests

## Architecture

The app follows Android's recommended app architecture, with a UI layer and a data layer.

```
UI layer
  HomeScreen  ->  HomeScreenViewModel ─┐
  PartyScreen ->  PartyViewModel ──────┤
                                       │
Data layer                             ▼
  AlpacaPartiesRepository ──> AlpacaPartiesDataSource ──> /alpacaparties
          │
          ▼
  VotesRepository ──> IndividualVotesDataSource ──> /district1, /district2
                  └─> AggregatedVotesDataSource ──> /district3
```

- **Data sources** fetch and deserialize data from the API. Districts 1 and 2 return one object per vote, which are counted with `groupingBy { it.id }.eachCount()`. District 3 returns vote totals per party. Both vote data sources return the same `DistrictVotes` format.
- **Repositories** implement interfaces (`AlpacaPartiesInterface`, `CollectingVotesInterface`) and give the ViewModels a single entry point to the data. `AlpacaPartiesRepository` combines party names with votes from `VotesRepository`.
- **ViewModels** hold the UI state as sealed classes (`Loading`, `Success`, `Error`) and fetch data with `viewModelScope`.
- **Screens** only observe the UI state and send events (like a district change) back to the ViewModel.

## Project structure

```
app/src/main/java/no/uio/ifi/in2000/ljvelpen/ljvelpen_oblig2/
├── MainActivity.kt
├── Navigate.kt                      NavHost and route definitions
├── data/
│   ├── alpacas/
│   │   ├── AlpacaPartiesDataSource.kt
│   │   └── AlpacaPartiesRepository.kt
│   ├── network/
│   │   └── KtorClient.kt            Shared Ktor HttpClient
│   └── votes/
│       ├── AggregatedVotesDataSource.kt
│       ├── IndividualVotesDataSource.kt
│       └── VotesRepository.kt
├── model/
│   ├── alpacas/
│   │   ├── Parties.kt               Wrapper for the API response
│   │   └── PartyInfo.kt
│   └── votes/
│       ├── District.kt
│       └── DistrictVotes.kt
└── ui/
    ├── Reusable.kt                  Composables shared by both screens
    ├── home/
    │   ├── HomeScreen.kt
    │   ├── HomeScreenViewModel.kt
    │   └── VoteList.kt
    ├── party/
    │   ├── PartyScreen.kt
    │   └── PartyViewModel.kt
    └── theme/
```

`Reusable.kt` contains the party info block (name, photo, leader and color bar) used on both screens, plus shared loading and error components.

## Running the app

1. Clone the repository and open it in Android Studio.
2. Let Gradle sync.
3. Run the app on an emulator or device with API level 34 or higher (minimum SDK is 24).

The app needs an internet connection to fetch data from the IN2000 proxy API.

## Tests

Unit tests are in `app/src/test/.../AlpacaTests.kt`. They check that the data sources and repositories return the expected data. The tests call the real API, so they need an internet connection.

```
./gradlew test
```

## Known issues

- Leader photos don't show in Compose previews, since previews don't make network requests. They load as expected in the running app.

## Acknowledgements

The assignment and API were provided by the IN2000 course staff at the Department of Informatics, University of Oslo.