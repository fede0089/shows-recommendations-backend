package com.showsrecommendations.domain.usecases

import com.showsrecommendations.domain.entities.FollowedUser
import com.showsrecommendations.domain.entities.Review
import com.showsrecommendations.domain.entities.Show
import com.showsrecommendations.domain.ports.FollowedUsersRepository
import com.showsrecommendations.domain.ports.ReviewsRepository
import com.showsrecommendations.domain.ports.ShowsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

import org.junit.jupiter.api.Assertions.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class CalculateAndGetRecommendationsTest : Spek({

    Feature("2.1 CalculateAndGetRecommendations") {

        val reviewsRepository = mockk<ReviewsRepository>(relaxed = true)
        val showsRepository = mockk<ShowsRepository>(relaxed = true)
        val followedUsersRepository = mockk<FollowedUsersRepository>(relaxed = true)

        val calculateAndGetRecommendations = CalculateAndGetRecommendations(
            followedUsersRepository = followedUsersRepository,
            reviewsRepository = reviewsRepository,
            showsRepository = showsRepository
        )

        var recommendedShows: List<CalculateAndGetRecommendations.RecommendedShow> = listOf()

        Scenario("2.1.1 Calculating and getting recommendations") {

            Given("user1 that is following user2, user3, user4, user5 and user6 ") {
                every {
                    followedUsersRepository.getFollowedUsers(userId = "user1")
                } returns listOf(
                    FollowedUser(id = "user2", followedDate = "20220218"),
                    FollowedUser(id = "user3", followedDate = "20220218"),
                    FollowedUser(id = "user4", followedDate = "20220218"),
                    FollowedUser(id = "user5", followedDate = "20220218"),
                    FollowedUser(id = "user6", followedDate = "20220218")
                )
            }

            And("user1 has seen and recommended Movie VI") {
                every {
                    reviewsRepository.getReviews(userId = "user1")
                } returns listOf(
                    Review(showId = "MovieVI", rating = 1f, createdDate = "20220218")
                )
            }

            And("user2 has recommended Movie I, Movie III, Movie V and Movie VI") {
                every {
                    reviewsRepository.getReviews(userId = "user2")
                } returns listOf(
                    Review(showId = "MovieI", rating = 1f, createdDate = "20220218"),
                    Review(showId = "MovieIII", rating = 1f, createdDate = "20220218"),
                    Review(showId = "MovieV", rating = 1f, createdDate = "20220218"),
                    Review(showId = "MovieVI", rating = 1f, createdDate = "20220218")
                )
            }

            And("user3 has recommended Movie I, and unrecommended Movie III & Movie V") {
                every {
                    reviewsRepository.getReviews(userId = "user3")
                } returns listOf(
                    Review(showId = "MovieI", rating = 1f, createdDate = "20220218"),
                    Review(showId = "MovieIII", rating = 0f, createdDate = "20220218"),
                    Review(showId = "MovieV", rating = 0f, createdDate = "20220218")
                )
            }

            And("user4, user5 and user7 have recommended Movie I and Movie III") {
                every {
                    reviewsRepository.getReviews(match { it in listOf("user4", "user5", "user7") })
                } returns listOf(
                    Review(showId = "MovieI", rating = 1f, createdDate = "20220218"),
                    Review(showId = "MovieIII", rating = 1f, createdDate = "20220218")
                )
            }

            And("user6 has recommended Movie III") {
                every {
                    reviewsRepository.getReviews(userId = "user6")
                } returns listOf(
                    Review(showId = "MovieIII", rating = 1f, createdDate = "20220218")
                )
            }

            And("a shows repository"){
                val showIdSlot = slot<String>()
                every {
                    showsRepository.getShow(id = capture(showIdSlot))
                } answers {
                    Show(
                        id = showIdSlot.captured,
                        title = showIdSlot.captured,
                        genres = listOf(""),
                        year = "unnecessary",
                        cover = "unnecessary",
                        type = "unnecessary",
                        overview = "unnecessary",
                        imdbId = "unnecessary",
                        externalId = "unnecessary"
                    )
                }
            }

            When("user1 gets its recommendations") {
                recommendedShows = calculateAndGetRecommendations(CalculateAndGetRecommendations.Request(userId = "user1")).recommendations
            }

            Then("user1 sees Movie I with 4 recommendations, Movie III with 4 recommendations and 1 un-recommendation and Movie V with 1 recommendation and 1 un-recommendation") {
                val expectedRecommendedShows = listOf(
                    CalculateAndGetRecommendations.RecommendedShow(
                        title = "MovieI", positiveReviewsQty = 4, negativeReviewsQty = 0, genres = listOf(""), year = "unnecessary", cover = "unnecessary", type = "unnecessary"
                    ),
                    CalculateAndGetRecommendations.RecommendedShow(
                        title = "MovieIII", positiveReviewsQty = 4, negativeReviewsQty = 1, genres = listOf(""), year = "unnecessary", cover = "unnecessary", type = "unnecessary"
                    ),
                    CalculateAndGetRecommendations.RecommendedShow(
                        title = "MovieV", positiveReviewsQty = 1, negativeReviewsQty = 1, genres = listOf(""), year = "unnecessary", cover = "unnecessary", type = "unnecessary"
                    )
                )
                assertEquals(3, recommendedShows.size)
                assertEquals(expectedRecommendedShows, recommendedShows)
            }
        }
    }
})

