package com.showsrecommendations.application

import com.google.gson.FieldNamingPolicy
import com.showsrecommendations.recommendations.adapters.controllers.RecommendationsController
import com.showsrecommendations.reviews.adapters.controllers.ReviewController
import com.showsrecommendations.users.adapters.controllers.FollowedUsersController
import com.showsrecommendations.shows.adapters.controllers.ShowController
import com.showsrecommendations.users.domain.entities.FollowedUser
import com.showsrecommendations.reviews.domain.entities.Review
import com.showsrecommendations.shows.domain.entities.Show
import com.showsrecommendations.users.domain.ports.FollowedUsersRepository
import com.showsrecommendations.reviews.domain.ports.ReviewsRepository
import com.showsrecommendations.shows.domain.ports.ShowsRepository
import com.showsrecommendations.recommendations.adapters.repositories.RecommendationsRepositoryInMemory
import com.showsrecommendations.recommendations.domain.usecases.GetRecommendations
import com.showsrecommendations.reviews.adapters.repositories.ReviewsRepositoryInMemory
import com.showsrecommendations.reviews.domain.usecases.AddReview
import com.showsrecommendations.shows.adapters.repositories.ShowsRepositoryInMemory
import com.showsrecommendations.shows.domain.usecases.GetShowForLoggedUser
import com.showsrecommendations.shows.domain.usecases.GetShowForUnloggedUser
import com.showsrecommendations.users.adapters.repositories.FollowedUsersRepositoryInMemory
import com.showsrecommendations.users.domain.usecases.FollowUser
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
    val reviewsRepository = buildReviewsRepository()
    val showsRepository = buildShowsRepository()
    val followedUsersRepository = buildUsersRepository()
    val recommendationsRepository = RecommendationsRepositoryInMemory(followedUsersRepository = followedUsersRepository, reviewsRepository = reviewsRepository)

    //use cases
    val getRecommendations = GetRecommendations(
        recommendationsRepository= recommendationsRepository,
        reviewsRepository = reviewsRepository,
        showsRepository = showsRepository
    )
    val addReview = AddReview(reviewsRepository = reviewsRepository)
    val followUser = FollowUser(followedUsersRepository = followedUsersRepository)
    val getShowForUnLoggedUser = GetShowForUnloggedUser(showsRepository = showsRepository)
    val getShowForLoggedUser = GetShowForLoggedUser(showsRepository = showsRepository, reviewsRepository = reviewsRepository, recommendationsRepository = recommendationsRepository )

    //controllers
    val recommendationsController = RecommendationsController(getRecommendations = getRecommendations)
    val reviewController = ReviewController(addReview = addReview)
    val followedUsersController = FollowedUsersController(followUser = followUser)
    val showController = ShowController(getShowForUnloggedUser = getShowForUnLoggedUser, getShowForLoggedUser = getShowForLoggedUser)

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {

        //plugins
        install(StatusPages) {
            exception<BadRequestException>{ call, cause ->
                call.respond(HttpStatusCode.BadRequest, cause)
            }

            exception<NotFoundException>{ call, cause ->
                call.respond(HttpStatusCode.NotFound)
            }
        }

        install(ContentNegotiation) {
            gson(){
                serializeNulls()
                setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            }
        }

        routing {

            get("/{userId}/shows/recommended") {
                val userId = call.parameters["userId"]!!
                val recommendedShows = recommendationsController.getRecommendedShows(userId)
                call.respond(recommendedShows)
            }

            get("/{userId}/shows/{showId}") {
                val userId = call.parameters["userId"]!!
                val showId = call.parameters["showId"]!!
                val getShowResponse = showController.getShow(userId = userId, showId = showId) //fixme its using recomms repository (pre-loaded)
                call.respond(getShowResponse)
            }

            get("/shows/{showId}") {
                val showId = call.parameters["showId"]!!
                val getShowResponse = showController.getShow(showId)
                call.respond(getShowResponse)
            }

            post("/{userId}/shows/{showId}/review/{rating}") {
                val userId = call.parameters["userId"]!!
                val showId = call.parameters["showId"]!!
                val rating = call.parameters["rating"]!!?.toFloat()
                val addReviewResponse = reviewController.addReview(userId, showId, rating)
                call.respond(addReviewResponse)
            }

            post("/{userId}/followedUsers/{followedUserId}") {
                val userId = call.parameters["userId"]!!
                val followedUserId = call.parameters["followedUserId"]!!
                val followUserResponse = followedUsersController.followUser(userId = userId, followedUserId = followedUserId)
                call.respond(followUserResponse)
            }

        }
    }.start(wait = true)
}

fun buildUsersRepository(): FollowedUsersRepository {
    val followedUsersRepository =  FollowedUsersRepositoryInMemory()
    followedUsersRepository["1"] = mutableListOf(
        FollowedUser(id = "2", followedDate = "20220210")
    )
    return followedUsersRepository
}

fun buildReviewsRepository(): ReviewsRepository {

    val reviewsRepository = ReviewsRepositoryInMemory()

    reviewsRepository["1"] = mutableListOf(
        Review(showId = "6", rating = 1f, createdDate = "20220218")
    )
    reviewsRepository["2"] = mutableListOf(
        Review(showId = "1", rating = 1f, createdDate = "20220218"),
        Review(showId = "3", rating = 1f, createdDate = "20220218"),
        Review(showId = "5", rating = 0f, createdDate = "20220218"),
        Review(showId = "6", rating = 1f, createdDate = "20220218")
    )
    reviewsRepository["3"] = mutableListOf(
        Review(showId = "1", rating = 1f, createdDate = "20220218"),
        Review(showId = "6", rating = 1f, createdDate = "20220218")
    )

    return reviewsRepository
}

fun buildShowsRepository(): ShowsRepository {
    val showsRepository = ShowsRepositoryInMemory()
    showsRepository["1"] = Show(id = "1", title = "The Gift", overview = "When a local woman disappears and the police can't seem to find any leads, her father turns to a poor young woman with psychic powers. Slowly she starts having visions of the woman chained and in a pond. Her visions lead to the body and the arrest of an abusive husband, but did he really do it?",genres = listOf("Thriller"), year = "2000", cover = "/nQdBE1P0r4ZrgGqy5EX8sL2kXG6.jpg", type = "MOVIE", imdbId = "tt0219699" ,externalId = "tmdb#2046")
    showsRepository["3"] = Show(id = "3", title = "Harry Potter and the Philosopher's Stone", overview = "Harry Potter has lived under the stairs at his aunt and uncle's house his whole life. But on his 11th birthday, he learns he's a powerful wizard—with a place waiting for him at the Hogwarts School of Witchcraft and Wizardry. As he learns to harness his newfound powers with the help of the school's kindly headmaster, Harry uncovers the truth about his parents' deaths—and about the villain who's to blame.", genres = listOf("Fantasy"), year = "2001", cover = "/wuMc08IPKEatf9rnMNXvIDxqP4W.jpg", type = "MOVIE", imdbId = "tt0241527", externalId = "tmdb#671")
    showsRepository["5"] = Show(id = "5", title = "Harry Potter and the Chamber of Secrets", overview = "Cars fly, trees fight back, and a mysterious house-elf comes to warn Harry Potter at the start of his second year at Hogwarts. Adventure and danger await when bloody writing on a wall announces: The Chamber Of Secrets Has Been Opened. To save Hogwarts will require all of Harry, Ron and Hermione’s magical abilities and courage.", genres = listOf("Fantasy"), year = "2002", cover = "/sdEOH0992YZ0QSxgXNIGLq1ToUi.jpg", type = "MOVIE", imdbId = "tt0295297" ,externalId = "tmdb#672")
    showsRepository["6"] = Show(id = "6", title = "Titanic", overview = "101-year-old Rose DeWitt Bukater tells the story of her life aboard the Titanic, 84 years later. A young Rose boards the ship with her mother and fiancé. Meanwhile, Jack Dawson and Fabrizio De Rossi win third-class tickets aboard the ship. Rose tells the whole story from Titanic's departure through to its death—on its first and last voyage—on April 15, 1912.", genres = listOf("Drama"), year = "1997", cover = "/9xjZS2rlVxm8SFx8kPC3aIGCOYQ.jpg", type = "MOVIE", imdbId = "tt0120338", externalId = "tmdb#597")
    return showsRepository
}
