package com.showsrecommendations.domain.usecases

import com.showsrecommendations.domain.entities.Show
import com.showsrecommendations.domain.ports.ShowsRepository

class GetShow(private val showsRepository: ShowsRepository): UseCase<GetShow.Request, GetShow.Response> {

    override operator fun invoke(getShowRequest: Request): Response =
        Response(show = showsRepository.getShow(getShowRequest.showId))

    data class Request(val showId: String)
    data class Response(val show: Show)
}