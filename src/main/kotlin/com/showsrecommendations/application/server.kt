package com.showsrecommendations.application

import com.google.gson.Gson
import com.showsrecommendations.adapters.controllers.RecommendationsController
import com.showsrecommendations.adapters.controllers.ReviewController
import com.showsrecommendations.adapters.controllers.FollowedUsersController
import com.showsrecommendations.adapters.repositories.inmemory.*
import com.showsrecommendations.domain.entities.FollowedUser
import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.entities.Review
import com.showsrecommendations.domain.entities.Show
import com.showsrecommendations.domain.ports.FollowedUsersRepository
import com.showsrecommendations.domain.ports.RecommendationsRepository
import com.showsrecommendations.domain.ports.ReviewsRepository
import com.showsrecommendations.domain.ports.ShowsRepository
import com.showsrecommendations.domain.usecases.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {

    //repositories impl.
    val recommendationsRepository = buildRecommendationsRepository()
    val reviewsRepository = buildReviewsRepository()
    val showsRepository = buildShowsRepository()
    val followedUsersRepository = buildUsersRepository()

    //use cases
    val getRecommendations = GetRecommendations(
        recommendationsRepository= recommendationsRepository,
        reviewsRepository = reviewsRepository)
    val calculateAndGetRecommendations = CalculateAndGetRecommendations(
        followedUsersRepository = followedUsersRepository,
        reviewsRepository = reviewsRepository
    )
    val getShow = GetShow(showsRepository = showsRepository)
    val addReview = AddReview(reviewsRepository = reviewsRepository)
    val followUser = FollowUser(followedUsersRepository = followedUsersRepository)

    //controllers
    val recommendationsController = RecommendationsController(getRecommendations = getRecommendations, getShow = getShow, calculateAndGetRecommendations = calculateAndGetRecommendations)
    val reviewController = ReviewController(addReview = addReview)
    val followedUsersController = FollowedUsersController(followUser = followUser)

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {

        //plugins
        install(StatusPages) {
            exception<BadRequestException>{ call, cause ->
                call.respond(HttpStatusCode.BadRequest, Gson().toJson(cause))
            }
        }

        install(ContentNegotiation) {
            gson()
        }

        routing {

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

            post("/{userId}/followedUsers/{followedUserId}") {
                val userId = call.parameters["userId"]!!
                val followedUserId = call.parameters["followedUserId"]!!
                val followUserRequest = FollowUser.Request(userId = userId, followedUserId = followedUserId)
                val followUserResponse = followedUsersController.followUser(followUserRequest)
                call.respondText(Gson().toJson(followUserResponse))
            }

            get("/{userId}/shows/recommended") {
                val userId = call.parameters["userId"]!!
                val recommendedShows = recommendationsController.calculateAndGetRecommendedShows(userId)
                call.respondText(Gson().toJson(recommendedShows))
            }

            get("/{userId}/shows/recommended-preloaded") {
                val userId = call.parameters["userId"]!!
                val recommendedShows = recommendationsController.getRecommendedShows(userId)
                call.respondText(Gson().toJson(recommendedShows))
            }

        }
    }.start(wait = true)
}

fun buildUsersRepository(): FollowedUsersRepository {
    val followedUsersRepository =  FollowedUsersRepositoryInMemory()
    followedUsersRepository["user1"] = mutableListOf(
        FollowedUser(id = "user2", followedDate = "20220210")
    )
    return followedUsersRepository
}

fun buildReviewsRepository(): ReviewsRepository {

    val reviewsRepository = ReviewsRepositoryInMemory()

    reviewsRepository["user1"] = mutableListOf(
        Review(showId = "MovieVI", rating = 1f, createdDate = "20220218")
    )
    reviewsRepository["user2"] = mutableListOf(
        Review(showId = "MovieI", rating = 1f, createdDate = "20220218"),
        Review(showId = "MovieIII", rating = 1f, createdDate = "20220218"),
        Review(showId = "MovieV", rating = 0f, createdDate = "20220218"),
        Review(showId = "MovieVI", rating = 1f, createdDate = "20220218")
    )
    reviewsRepository["user3"] = mutableListOf(
        Review(showId = "MovieI", rating = 1f, createdDate = "20220218"),
        Review(showId = "MovieVI", rating = 1f, createdDate = "20220218")
    )

    return reviewsRepository
}

fun buildRecommendationsRepository(): RecommendationsRepository {
    val recommendationsRepository = RecommendationsRepositoryInMemory()
    recommendationsRepository["user1"] = listOf(
        Recommendation(showId = "MovieI", positiveReviewsQty = 1, negativeReviewsQty = 0),
        Recommendation(showId = "MovieIII", positiveReviewsQty = 1, negativeReviewsQty = 1),
        Recommendation(showId = "MovieV", positiveReviewsQty = 0, negativeReviewsQty = 1),
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
