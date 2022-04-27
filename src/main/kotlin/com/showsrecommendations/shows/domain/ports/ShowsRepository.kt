package com.showsrecommendations.shows.domain.ports

import com.showsrecommendations.shows.domain.entities.Show

interface ShowsRepository {
    fun getShow(id: String): Show?
}