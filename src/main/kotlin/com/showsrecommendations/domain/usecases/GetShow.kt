package com.showsrecommendations.domain.usecases

import com.showsrecommendations.domain.entities.Show
import com.showsrecommendations.domain.ports.ShowsRepository

class GetShow(private val showsRepository: ShowsRepository): UseCase<String, Show> {
    override operator fun invoke(showId: String): Show =
        showsRepository.getShow(showId)
}