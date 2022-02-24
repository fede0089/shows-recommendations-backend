package com.showsrecommendations.application

import com.google.gson.Gson
import com.showsrecommendations.adapters.controllers.RecommendationsController
import com.showsrecommendations.adapters.controllers.ReviewController
import com.showsrecommendations.adapters.repositories.inmemory.RecommendationsRepositoryInMemory
import com.showsrecommendations.adapters.repositories.inmemory.ReviewsRepositoryInMemory
import com.showsrecommendations.adapters.repositories.inmemory.ShowsRepositoryInMemory
import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.entities.Review
import com.showsrecommendations.domain.entities.Show
import com.showsrecommendations.domain.ports.RecommendationsRepository
import com.showsrecommendations.domain.ports.ReviewsRepository
import com.showsrecommendations.domain.ports.ShowsRepository
import com.showsrecommendations.domain.usecases.AddReview
import com.showsrecommendations.domain.usecases.GetRecommendations
import com.showsrecommendations.domain.usecases.GetShow
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {

    //repositories impl.
    val recommendationsRepository = buildRecommendationsRepository()
    val reviewsRepository = buildReviewsRepository()
    val showsRepository = buildShowsRepository()

    //use cases
    val getRecommendations = GetRecommendations(
        recommendationsRepository= recommendationsRepository,
        reviewsRepository = reviewsRepository)
    val getShow = GetShow(showsRepository = showsRepository)
    val addReview = AddReview(reviewsRepository = reviewsRepository)

    //controllers
    val recommendationsController = RecommendationsController(getRecommendations = getRecommendations, getShow = getShow)
    val reviewController = ReviewController(addReview = addReview)

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {

        //plugins
        install(StatusPages) {
            exception<BadRequestException> { cause ->
                call.respond(HttpStatusCode.BadRequest, Gson().toJson(cause))
            }
        }

        routing {

            get("/{userId}/shows/recommended") {
                val userId = call.parameters["userId"]!!
                val recommendedShows = recommendationsController.getRecommendedShows(userId)
                call.respondText(Gson().toJson(recommendedShows))
            }

            post("/{userId}/shows/{showId}/review/{rating}") {
                val userId = call.parameters["userId"]!!
                val showId = call.parameters["showId"]!!
                val rating = call.parameters["rating"]!!?.toFloat()
                val addReviewRequest = AddReview.Request(
                    userId = userId,
                    showId = showId,
                    rating = rating)
                val addReviewResponse = reviewController.addReview(addReviewRequest)
                call.respondText(Gson().toJson(addReviewResponse))
            }

        }
    }.start(wait = true)
}

fun buildReviewsRepository(): ReviewsRepository {
    val reviewsRepository = ReviewsRepositoryInMemory()
    reviewsRepository["user1"] = mutableListOf(Review(showId = "MovieVI", rating = 1f, createdDate = "20220218"))
    return reviewsRepository
}

fun buildRecommendationsRepository(): RecommendationsRepository {
    val recommendationsRepository = RecommendationsRepositoryInMemory()
    recommendationsRepository["user1"] = listOf(
        Recommendation(showId = "MovieI", positiveReviewsQty = 4, negativeReviewsQty = 0),
        Recommendation(showId = "MovieIII", positiveReviewsQty = 4, negativeReviewsQty = 1),
        Recommendation(showId = "MovieV", positiveReviewsQty = 1, negativeReviewsQty = 1),
        Recommendation(showId = "MovieVI", positiveReviewsQty = 1, negativeReviewsQty = 0)
    )
    return recommendationsRepository
}

fun buildShowsRepository(): ShowsRepository{
    val showsRepository = ShowsRepositoryInMemory()
    showsRepository["MovieI"] = Show(id = "MovieI", title = "Movie I", genre = "Action", year = "2022", cover = "cover.jpg", type = "MOVIE", externalId = "imdb#movieI")
    showsRepository["MovieIII"] = Show(id = "MovieIII", title = "Movie III", genre = "Suspense", year = "2022", cover = "cover.jpg", type = "MOVIE", externalId = "imdb#movieIII")
    showsRepository["MovieV"] = Show(id = "MovieV", title = "Movie V", genre = "Horror", year = "2012", cover = "cover.jpg", type = "MOVIE", externalId = "imdb#movieV")
    showsRepository["MovieVI"] = Show(id = "MovieVI", title = "Movie VI", genre = "Comedy", year = "2021", cover = "cover.jpg", type = "MOVIE", externalId = "imdb#movieVI")
    return showsRepository
}
