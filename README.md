# Dage

Onde o exercício e a música se fundem.

## Proposta
- https://docs.google.com/document/d/1--hoMu2vB_EsYUMzDxLIAHMkMga_m8BR4hY6OLOvTC0/edit#

## Mockup
- https://marvelapp.com/15i032gg/screen/41648357

## Descrição básica

O código do App foi dividido de acordo com a estrurura abaixo:

```
app/src/main/java/com/if1001/cin/dage/
│   AppDatabase.kt
│   Extensions.kt
|   ListPointConverter.kt
|   MainActivity.kt
|   MenuActivity.kt    
│
└───DAO
│   PlayListDao.kt
│   UserDao.kt
|   WorkoutDao.kt    
│   
|───adapters
|   MusicAdapter.kt
|   PastWorkoutsAdapter.kt
|   PlayListsAdapter.kt    
|
|───fragments
|   HomeFragment.kt
|   MapPlaylistFragment.kt
|   PastWorkoutsFragment.kt
|   PlayingFragment.kt
|
└───model
    Music.kt
    PlayList.kt
    User.kt
    Workout.kt
```

Onde as pastas:

- model: Classes de domínio que representam as entidades utilizadas pelo BD e em todo o App
- fragments: Classes responsáveis por cada um dos fragments
- adapters: Adapters personalizados para os tipos de dados (Música, Playlist, Workout)
- DAO: Classes de acesso a dados (Usando Room) dos dados a serem salvos pelo App

Onde os Arquivos:

- AppDatabase.kt: Define a base de dados da aplicação
- Extensions.kt: Declarações e funções úteis a várias classes
- ListPointConverter.kt: Usado na converção de pontos de GPS
- MainActivity.kt: Activity inicial ligada à tela de Login
- MenuActivity.kt: Activity principal que coordena menu e todos os fragments

Os fragments (na pasta fragments), são:

- HomeFragment.kt: Fragment principal (Mapa e botão de play)
- MapPlaylistFragment.kt: Fragmente de mapa + lista de plylists do usuário logado
- PastWorkoutsFragment.kt: Lista os workouts salvos
- PlayingFragment.kt: Fragment que toca a música faz o tracking do workout (GPS) em si



Os layouts utilizados foram os seguintes:

```
app/src/main/res/layout/
    activity_main.xml
    activity_menu.xml
    cell_past_workouts.xml
    cell_play_lists.xml
    fragment_home.xml
    fragment_map_playlist.xml
    fragment_past_workouts.xml
    fragment_playing.xml
    nav_header_menu.xml    
```

Onde:

- activity_main.xml: Activity inicial do App (Login)
- activity_menu.xml: Activity principal que comporta todos os fragments
- cell_past_workouts.xml: Célula que representa uma instância de workout em listagem
- cell_play_lists.xml: Célula que representa uma instância de playlist (ou música) em listagem
- fragment_home.xml: Fragment principal (mapa + botão de play)
- fragment_map_playlist.xml: Fragment que mostra o mapa e as playlists do usuário
- fragment_past_workouts.xml: Fragment dos workouts salvos
- fragment_playing.xml: Fragment com mapa + lista de músicas da playlist selecionada e controles de música
- nav_header_menu.xml: header do menu lateral

## Dependências
- Kotlin
- Font Awesome Android
- Picasso
- OkHttp
- Spotify Auth
- Spotify Player
- Room
- Gson

## Vídeo demonstrativo

[![Vídeo](https://img.youtube.com/vi/FX3oEYrS2qg/0.jpg)](https://youtu.be/FX3oEYrS2qg)
