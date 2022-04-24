package com.showsrecommendations.domain.usecases

import com.showsrecommendations.domain.ports.ShowsRepository

class GetShow(private val showsRepository: ShowsRepository): UseCase<GetShow.Request, GetShow.Response> {

    override operator fun invoke(getShowRequest: Request): Response {
        val show = showsRepository.getShow(getShowRequest.showId)
        return Response(
            id = show.id,
            title = show.title,
            genres = show.genres,
            year = show.year,
            cover = show.cover,
            type = show.type,
            overview = show.overview
        )
    }

    data class Request(val showId: String)
    data class Response(val id:String,
                        val title: String,
                        val genres: List<String>,
                        val year:String,
                        val cover:String,
                        val type:String,
                        val overview:String)
}