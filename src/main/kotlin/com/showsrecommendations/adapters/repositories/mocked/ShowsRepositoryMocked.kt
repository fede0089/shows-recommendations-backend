package com.showsrecommendations.adapters.repositories.mocked

import com.showsrecommendations.domain.entities.Show
import com.showsrecommendations.domain.ports.ShowsRepository

class ShowsRepositoryMocked(private val shows: Map<String, Show> = mapOf()): ShowsRepository {
    override fun getShow(id: String): Show = shows[id]!!
}