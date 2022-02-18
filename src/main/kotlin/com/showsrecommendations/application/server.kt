package com.showsrecommendations.application

import com.google.gson.Gson
import com.showsrecommendations.adapters.controllers.RecommendationsController
import com.showsrecommendations.adapters.repositories.mocked.RecommendationsRepositoryMocked
import com.showsrecommendations.adapters.repositories.mocked.ShowsRepositoryMocked
import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.entities.Show
import com.showsrecommendations.domain.usecases.GetRecommendations
import com.showsrecommendations.domain.usecases.GetShow
import io.ktor.application.call
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty


fun main() {

    //config
    val getRecommendationsUseCase = GetRecommendations(
        recommendationsRepository = RecommendationsRepositoryMocked(
            recommendations = mapOf(
                "userId" to listOf(
                    Recommendation(showId = "MovieI", recommendationsQty = 4, unrecommendationsQty = 0),
                    Recommendation(showId = "MovieIII", recommendationsQty = 4, unrecommendationsQty = 1),
                    Recommendation(showId = "MovieV", recommendationsQty = 1, unrecommendationsQty = 1),
                    Recommendation(showId = "MovieVI", recommendationsQty = 1, unrecommendationsQty = 0)
                )
            )
        )
    )
    val getShowUseCase = GetShow(showsRepository = ShowsRepositoryMocked(
        shows = mapOf(
            "MovieI" to Show(id = "MovieI", title = "Movie I", genre = "Action", year = "2022", cover = "cover.jpg", type = "MOVIE", externalId = "imdb#movieI"),
            "MovieIII" to Show(id = "MovieIII", title = "Movie III", genre = "Suspense", year = "2022", cover = "cover.jpg", type = "MOVIE", externalId = "imdb#movieIII"),
            "MovieV" to Show(id = "MovieV", title = "Movie V", genre = "Horror", year = "2012", cover = "cover.jpg", type = "MOVIE", externalId = "imdb#movieV"),
            "MovieVI" to Show(id = "MovieVI", title = "Movie VI", genre = "Comedy", year = "2021", cover = "cover.jpg", type = "MOVIE", externalId = "imdb#movieVI")
        )
    ))
    val recommendationsController =
        RecommendationsController(getRecommendations = getRecommendationsUseCase, getShow = getShowUseCase)

    //server
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        routing {
            get("/{userId}/shows/recommended") {
                val userId = call.parameters["userId"]!!
                val recommendedShows = recommendationsController.getRecommendedShows(userId)
                call.respondText(Gson().toJson(recommendedShows))
            }
        }
    }.start(wait = true)
}