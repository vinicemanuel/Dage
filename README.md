# Dage

## Proposta
- https://docs.google.com/document/d/1--hoMu2vB_EsYUMzDxLIAHMkMga_m8BR4hY6OLOvTC0/edit#

## Mockup
- https://marvelapp.com/15i032gg/screen/41648357

## Dependências
- Kotlin
- Font Awesome Android
- Picasso
- OkHttp
- Spotify Auth
- Spotify Player
- Room
- Gson

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
