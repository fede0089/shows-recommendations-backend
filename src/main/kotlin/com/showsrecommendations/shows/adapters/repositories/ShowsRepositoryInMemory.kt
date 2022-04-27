package com.showsrecommendations.shows.adapters.repositories

import com.showsrecommendations.shows.domain.entities.Show
import com.showsrecommendations.shows.domain.ports.ShowsRepository

class ShowsRepositoryInMemory(private val shows: MutableMap<String, Show> = mutableMapOf()): ShowsRepository, MutableMap<String, Show> by shows {
    override fun getShow(id: String): Show? = shows[id]
}