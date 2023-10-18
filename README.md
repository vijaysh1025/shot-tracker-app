# NBA Shot Mapping Android App

### App In Progress

<img src="/video/github_video.gif" width="300">

## Overview
Pick an NBA game from any date and display the shot chart data of any given player from that game. Mobile app technologies have seen a new shift in how developers think and how they develop. This application utilizes the latest mobile technologies to provide the best user experience.

* Architecture - Clean Architecture (Data / Domain / Presentation)
* Data Layer - Network Repository (using Retrofit). Eventually would like to implement caching using Room.
* Domain Layer - Use Cases
* Presentation Layer - MVVM
* Key Technologies - Retrofit, RxJava, Dagger, JUnit, Mockito

## Application Breakdown
### Game Selection Screen
User will use the Date Picker to select a date in history. The Date Picker is bound to `BehaviorSubject<LocalDate>` (Observable in the `DailyScheduleViewModel`) which will emit every time the date changes. Every time the date changes, a request is made to the NBA SportRadar API through Retrofit ( `@GET("/nba/trial/v5/en/games/{year}/{month}/{day}/schedule.json")`) using the given date to pull the schedule of games from that day. Retrofit GSONConverter is used to map the incoming data into an equalent DailySchedule object and a `Single<DailySchedule>` is returned by the retrofit call. This observable is sent to the Data Model to further refine the data which is eventually fed to ViewModel to pull out the relevant info required to display a clickable list of games from the given date.

*Note* - The `debounce` operator is very useful in making sure that when the user is selecting a date, multiple emissions of quick date changes don't overload the app with multiple API requests. 

The game list is bound to the `mSelectedGame` BehaviorSubject. which emits a new `Game` object whenever a list item is clicked.

Once a game is clicked, it will retrieve data for that game from the Play-By-Play endpoint (Retrofit call `@GET("/nba/trial/v5/en/games/{game_id}/pbp.json")` and emit a PlayerStats object from the 'BehaviorSubject<PlayerStats>' in the AppState singleton that is loaded into each ViewModel. We subscribe to this Behavior Subject so that we know when the data is available and can launch the `ShotChartActivity`.

### Shot Chart Screen

The initialization of this screen is heavily dependent on the unique property of `BehaviorSubject`(RxJava Library). `BehaviorSubjects` will always emit their last value to new subscribers which is what we want in order to initialize this screen with data for the selected game. We can load the Home and Away team logos on the respective buttons, and the list of player numbers in the `NumberPicker` component.

##### Team Selection
`mSelectedTeam` Behavior subject is bound to the Home and Away team buttons on this screen emitting `TeamType.HOME` and `TeamType.Away` respectively. This Behavior subject will always emit the Home team first.

##### Player Selection (NumberPicker Widget)
This list is bound by a subscription to `getTeamPlayers()` (an `Observable<List<PlayersItem>>` created by adding a `flatmap` modifier to `mSelectedTeam`. 

##### Shot Chart
A mutable list of `ShotSpotView` (custom view for made / miss shots) subscribes to the `mSelectedPlayer` SubjectBehavior and gets location data from the `PlayerStats` for a specific player and maps out all made/missed shots. 

*Important* - All UI interaction should be communicated to the ViewModel and any UI that needs updating from these interactions should be updated by subscribing to ViewModel states (`BehaviorSubject`). 

