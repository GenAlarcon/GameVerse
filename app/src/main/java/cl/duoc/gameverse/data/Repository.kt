package cl.duoc.gameverse.data

import cl.duoc.gameverse.R
import cl.duoc.gameverse.domain.model.Game

class Repository {

    fun getJuegosDelCatalogo(): List<Game> {

        return listOf(
            Game(
                id = "1",
                nombre = "Elden Ring",
                genero = "RPG de Acción",
                imageResId = R.drawable.eldenring
            ),
            Game(
                id = "2",
                nombre = "Cyberpunk 2077",
                genero = "RPG de Acción",
                imageResId = R.drawable.cyberpunk
            ),
            Game(
                id = "3",
                nombre = "Stardew Valley",
                genero = "Simulación",
                imageResId = R.drawable.stardewvalley
            ),
            Game(
                id = "4",
                nombre = "Valorant",
                genero = "Shooter Táctico",
                imageResId = R.drawable.valorant
            ),
            Game(
                id = "5",
                nombre = "The Witcher 3",
                genero = "RPG",
                imageResId = R.drawable.thewitcher
            )
        )
    }

}