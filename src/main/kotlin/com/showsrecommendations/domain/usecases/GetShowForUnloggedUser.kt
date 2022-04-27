package com.showsrecommendations.domain.usecases

import com.showsrecommendations.domain.ports.ShowsRepository
import io.ktor.server.plugins.*

class GetShowForUnloggedUser(private val showsRepository: ShowsRepository): UseCase<GetShowForUnloggedUser.Request, GetShowForUnloggedUser.Response> {

    override operator fun invoke(getShowRequest: Request): Response {
        val show = showsRepository.getShow(getShowRequest.showId) ?: throw NotFoundException(message = "Show not found")

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