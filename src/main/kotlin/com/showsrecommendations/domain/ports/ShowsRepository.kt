package com.showsrecommendations.domain.ports

import com.showsrecommendations.domain.entities.Show

interface ShowsRepository {
    fun getShow(id: String): Show?
}