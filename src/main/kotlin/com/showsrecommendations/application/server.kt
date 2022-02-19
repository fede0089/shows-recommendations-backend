package com.showsrecommendations.application

import com.google.gson.Gson
import com.showsrecommendations.adapters.controllers.RecommendationsController
import com.showsrecommendations.adapters.repositories.inmemory.RecommendationsRepositoryInMemory
import com.showsrecommendations.adapters.repositories.inmemory.ReviewsRepositoryInMemory
import com.showsrecommendations.adapters.repositories.inmemory.ShowsRepositoryInMemory
import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.entities.Review
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
    val recommendationsController = buildRecommendationsController()
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

fun buildRecommendationsController(): RecommendationsController{

    val reviewsRepository = ReviewsRepositoryInMemory()
    reviewsRepository["user1"] = listOf(Review(showId = "MovieVI", rating = 1f, createdDate = "20220218", lastUpdated = "20220218"))

    val recommendationsRepository = RecommendationsRepositoryInMemory()
    recommendationsRepository["user1"] = listOf(
        Recommendation(showId = "MovieI", recommendationsQty = 4, unrecommendationsQty = 0),
        Recommendation(showId = "MovieIII", recommendationsQty = 4, unrecommendationsQty = 1),
        Recommendation(showId = "MovieV", recommendationsQty = 1, unrecommendationsQty = 1),
        Recommendation(showId = "MovieVI", recommendationsQty = 1, unrecommendationsQty = 0)
    )

    //FIXME if show is not found, it crashes
    val showsRepository = ShowsRepositoryInMemory()
    showsRepository["MovieI"] = Show(id = "MovieI", title = "Movie I", genre = "Action", year = "2022", cover = "cover.jpg", type = "MOVIE", externalId = "imdb#movieI")
    showsRepository["MovieIII"] = Show(id = "MovieIII", title = "Movie III", genre = "Suspense", year = "2022", cover = "cover.jpg", type = "MOVIE", externalId = "imdb#movieIII")
    showsRepository["MovieV"] = Show(id = "MovieV", title = "Movie V", genre = "Horror", year = "2012", cover = "cover.jpg", type = "MOVIE", externalId = "imdb#movieV")
    showsRepository["MovieVI"] = Show(id = "MovieVI", title = "Movie VI", genre = "Comedy", year = "2021", cover = "cover.jpg", type = "MOVIE", externalId = "imdb#movieVI")

    val getRecommendations = GetRecommendations(
        recommendationsRepository= recommendationsRepository,
        reviewsRepository = reviewsRepository)

    val getShow = GetShow(showsRepository = showsRepository)

    return RecommendationsController(getRecommendations = getRecommendations, getShow = getShow)

}