# NBA Shot Mapping Android App V2

### App In Progress

|                  Sign In                   |                  Pick Games                   |                  Shot Chart                   |
|:------------------------------------------:|:---------------------------------------------:|:---------------------------------------------:|
| <img src="/video/sign-in.gif" width="200"> | <img src="/video/pick-games.gif" width="200"> | <img src="/video/shot-chart.gif" width="200"> |


## Overview
Pick an NBA game from any date and display the shot chart data of any given player from that game. 

* Architecture - Clean Architecture 
* Data Layer - Repository with Network (Retrofit) and Local (Room) data sources
* Presentation Layer - MVVM
* Key Technologies - Compose, Retrofit, LiveData, Room, Hilt, Coroutines, Navigation, Firebase Authentication

## Application Breakdown

### Sign In Screen
From settings screen user can sign in. Sign in is implemented using Firebase Authentication. Sign in has no use case in this application but will eventually be used to allow users to follow their favorite players and teams.

### Game Selection Screen
Select a specific date and user will see list of games played on that day. User can select a game and will be taken to the Shot Chart screen. Game data is retrieved from the SportRadar NBA API.

### Shot Chart Screen
Selected game data is presented on this screen. User can select a player from the list of players on the team and the shot chart will update to show the selected players shot data. Shot data is retrieved from the SportRadar NBA API.

