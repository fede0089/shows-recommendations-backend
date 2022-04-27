package com.showsrecommendations.motherobjects

import com.showsrecommendations.domain.entities.Show

class ShowObjectMother {
    companion object {
        fun buildShow(id: String): Show {
            return Show(
                id = id,
                title = "Title",
                genres = listOf("Thriller"),
                year = "year",
                cover = "cover",
                type = "movie",
                overview = "bleble",
                imdbId = "tt_id_ble",
                externalId = "e_id_ble"
            )
        }
    }
}