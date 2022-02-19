package com.showsrecommendations.adapters.repositories.inmemory

import com.showsrecommendations.domain.entities.Show
import com.showsrecommendations.domain.ports.ShowsRepository

class ShowsRepositoryInMemory(private val shows: MutableMap<String, Show> = mutableMapOf()): ShowsRepository, MutableMap<String, Show> by shows {
    override fun getShow(id: String): Show = shows[id]!!
}