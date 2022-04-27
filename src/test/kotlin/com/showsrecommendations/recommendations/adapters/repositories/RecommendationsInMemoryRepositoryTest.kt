package com.showsrecommendations.recommendations.adapters.repositories

import com.showsrecommendations.users.domain.entities.FollowedUser
import com.showsrecommendations.recommendations.domain.entities.Recommendation
import com.showsrecommendations.reviews.domain.entities.Review
import com.showsrecommendations.users.domain.ports.FollowedUsersRepository
import com.showsrecommendations.reviews.domain.ports.ReviewsRepository
import io.mockk.every
import io.mockk.mockk

import org.junit.jupiter.api.Assertions.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature


class RecommendationsInMemoryRepositoryTest : Spek({

    Feature("GetRecommendations") {

        val reviewsRepository = mockk<ReviewsRepository>(relaxed = true)
        val followedUsersRepository = mockk<FollowedUsersRepository>(relaxed = true)

        val recommendationsRepository = RecommendationsRepositoryInMemory(
            followedUsersRepository = followedUsersRepository,
            reviewsRepository = reviewsRepository
        )

        var recommendedShows: List<Recommendation> = listOf()

        Scenario("Getting recommendations") {

            Given("user 1 that is following user 2, user 3, user 4, user 5 and user 6 ") {
                every {
                    followedUsersRepository.getFollowedUsers(userId = "1")
                } returns listOf(
                    FollowedUser(id = "2", followedDate = "20220218"),
                    FollowedUser(id = "3", followedDate = "20220218"),
                    FollowedUser(id = "4", followedDate = "20220218"),
                    FollowedUser(id = "5", followedDate = "20220218"),
                    FollowedUser(id = "6", followedDate = "20220218")
                )
            }

            And("user 1 has seen and recommended movie 6") {
                every {
                    reviewsRepository.getReviews(userId = "1")
                } returns listOf(
                    Review(showId = "6", rating = 1f, createdDate = "20220218")
                )
            }

            And("user 2 has recommended movie 1, movie 3, movie r and movie 5") {
                every {
                    reviewsRepository.getReviews(userId = "2")
                } returns listOf(
                    Review(showId = "1", rating = 1f, createdDate = "20220218"),
                    Review(showId = "3", rating = 1f, createdDate = "20220218"),
                    Review(showId = "5", rating = 1f, createdDate = "20220218"),
                    Review(showId = "6", rating = 1f, createdDate = "20220218")
                )
            }

            And("user3 has recommended movie 1, and unrecommended movie 3 & movie 5") {
                every {
                    reviewsRepository.getReviews(userId = "3")
                } returns listOf(
                    Review(showId = "1", rating = 1f, createdDate = "20220218"),
                    Review(showId = "3", rating = 0f, createdDate = "20220218"),
                    Review(showId = "5", rating = 0f, createdDate = "20220218")
                )
            }

            And("user 4, user 5 and user 7 have recommended movie 1 and movie 3") {
                every {
                    reviewsRepository.getReviews(match { it in listOf("4", "5", "6") })
                } returns listOf(
                    Review(showId = "1", rating = 1f, createdDate = "20220218"),
                    Review(showId = "3", rating = 1f, createdDate = "20220218")
                )
            }

            And("user 6 has recommended movie 3") {
                every {
                    reviewsRepository.getReviews(userId = "6")
                } returns listOf(
                    Review(showId = "3", rating = 1f, createdDate = "20220218")
                )
            }

            When("user 1 gets its recommendations") {
                recommendedShows = recommendationsRepository.getRecommendations(userId = "1")
            }

            Then("user 1 gets movie 1 with 4 recommendations, movie 3 with 4 recommendations and 1 un-recommendation, movie 4 with 1 recommendation and 1 un-recommendation and movie 6 with 1 recommendation") {
                val expectedRecommendedShows = listOf(
                    Recommendation(
                         showId = "1", positiveReviewsQty = 4, negativeReviewsQty = 0
                    ),
                    Recommendation(
                        showId = "3", positiveReviewsQty = 4, negativeReviewsQty = 1
                    ),
                    Recommendation(
                        showId = "6", positiveReviewsQty = 1, negativeReviewsQty = 0
                    ),
                    Recommendation(
                        showId = "5", positiveReviewsQty = 1, negativeReviewsQty = 1
                    )
                )
                assertEquals(4, recommendedShows.size)
                assertTrue(recommendedShows.containsAll(expectedRecommendedShows))
            }
        }
    }
})

