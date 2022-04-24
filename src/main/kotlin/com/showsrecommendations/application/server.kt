package com.showsrecommendations.application

import com.google.gson.Gson
import com.showsrecommendations.adapters.controllers.RecommendationsController
import com.showsrecommendations.adapters.controllers.ReviewController
import com.showsrecommendations.adapters.controllers.FollowedUsersController
import com.showsrecommendations.adapters.controllers.ShowController
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
    val showController = ShowController(getShow = getShow)

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
                val addReviewResponse = reviewController.addReview(userId, showId, rating)
                call.respondText(Gson().toJson(addReviewResponse))
            }

            post("/{userId}/followedUsers/{followedUserId}") {
                val userId = call.parameters["userId"]!!
                val followedUserId = call.parameters["followedUserId"]!!
                val followUserResponse = followedUsersController.followUser(userId = userId, followedUserId = followedUserId)
                call.respondText(Gson().toJson(followUserResponse))
            }

            get("/shows/{showId}") {
                val showId = call.parameters["showId"]!!
                val getShowResponse = showController.getShow(showId)
                call.respondText(Gson().toJson(getShowResponse))
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
        Review(showId = "6", rating = 1f, createdDate = "20220218")
    )
    reviewsRepository["user2"] = mutableListOf(
        Review(showId = "1", rating = 1f, createdDate = "20220218"),
        Review(showId = "3", rating = 1f, createdDate = "20220218"),
        Review(showId = "5", rating = 0f, createdDate = "20220218"),
        Review(showId = "6", rating = 1f, createdDate = "20220218")
    )
    reviewsRepository["user3"] = mutableListOf(
        Review(showId = "1", rating = 1f, createdDate = "20220218"),
        Review(showId = "6", rating = 1f, createdDate = "20220218")
    )

    return reviewsRepository
}

fun buildRecommendationsRepository(): RecommendationsRepository {
    val recommendationsRepository = RecommendationsRepositoryInMemory()
    recommendationsRepository["user1"] = listOf(
        Recommendation(showId = "1", positiveReviewsQty = 1, negativeReviewsQty = 0),
        Recommendation(showId = "3", positiveReviewsQty = 1, negativeReviewsQty = 1),
        Recommendation(showId = "5", positiveReviewsQty = 0, negativeReviewsQty = 1),
        Recommendation(showId = "6", positiveReviewsQty = 1, negativeReviewsQty = 0)
    )
    return recommendationsRepository
}

fun buildShowsRepository(): ShowsRepository{
    val showsRepository = ShowsRepositoryInMemory()
    showsRepository["1"] = Show(id = "1", title = "The Gift", overview = "When a local woman disappears and the police can't seem to find any leads, her father turns to a poor young woman with psychic powers. Slowly she starts having visions of the woman chained and in a pond. Her visions lead to the body and the arrest of an abusive husband, but did he really do it?",genres = listOf("Thriller"), year = "2000", cover = "/nQdBE1P0r4ZrgGqy5EX8sL2kXG6.jpg", type = "MOVIE", imdbId = "tt0219699" ,externalId = "tmdb#2046")
    showsRepository["3"] = Show(id = "3", title = "Harry Potter and the Philosopher's Stone", overview = "Harry Potter has lived under the stairs at his aunt and uncle's house his whole life. But on his 11th birthday, he learns he's a powerful wizard—with a place waiting for him at the Hogwarts School of Witchcraft and Wizardry. As he learns to harness his newfound powers with the help of the school's kindly headmaster, Harry uncovers the truth about his parents' deaths—and about the villain who's to blame.", genres = listOf("Fantasy"), year = "2001", cover = "/wuMc08IPKEatf9rnMNXvIDxqP4W.jpg", type = "MOVIE", imdbId = "tt0241527", externalId = "tmdb#671")
    showsRepository["5"] = Show(id = "5", title = "Harry Potter and the Chamber of Secrets", overview = "Cars fly, trees fight back, and a mysterious house-elf comes to warn Harry Potter at the start of his second year at Hogwarts. Adventure and danger await when bloody writing on a wall announces: The Chamber Of Secrets Has Been Opened. To save Hogwarts will require all of Harry, Ron and Hermione’s magical abilities and courage.", genres = listOf("Fantasy"), year = "2002", cover = "/sdEOH0992YZ0QSxgXNIGLq1ToUi.jpg", type = "MOVIE", imdbId = "tt0295297" ,externalId = "tmdb#672")
    showsRepository["6"] = Show(id = "6", title = "Titanic", overview = "101-year-old Rose DeWitt Bukater tells the story of her life aboard the Titanic, 84 years later. A young Rose boards the ship with her mother and fiancé. Meanwhile, Jack Dawson and Fabrizio De Rossi win third-class tickets aboard the ship. Rose tells the whole story from Titanic's departure through to its death—on its first and last voyage—on April 15, 1912.", genres = listOf("Drama"), year = "1997", cover = "/9xjZS2rlVxm8SFx8kPC3aIGCOYQ.jpg", type = "MOVIE", imdbId = "tt0120338", externalId = "tmdb#597")
    return showsRepository
}
