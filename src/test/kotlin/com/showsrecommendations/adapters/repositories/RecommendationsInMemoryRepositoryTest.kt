package com.showsrecommendations.adapters.repositories

import com.showsrecommendations.adapters.repositories.inmemory.RecommendationsRepositoryInMemory
import com.showsrecommendations.domain.entities.FollowedUser
import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.entities.Review
import com.showsrecommendations.domain.ports.FollowedUsersRepository
import com.showsrecommendations.domain.ports.ReviewsRepository
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

            When("user1 gets its recommendations") {
                recommendedShows = recommendationsRepository.getRecommendations(userId = "user1")
            }

            Then("user1 gets Movie I with 4 recommendations, Movie III with 4 recommendations and 1 un-recommendation, Movie V with 1 recommendation and 1 un-recommendation and Movie VI with 1 recommendation") {
                val expectedRecommendedShows = listOf(
                    Recommendation(
                         showId = "MovieI", positiveReviewsQty = 4, negativeReviewsQty = 0
                    ),
                    Recommendation(
                        showId = "MovieIII", positiveReviewsQty = 4, negativeReviewsQty = 1
                    ),
                    Recommendation(
                        showId = "MovieVI", positiveReviewsQty = 1, negativeReviewsQty = 0
                    ),
                    Recommendation(
                        showId = "MovieV", positiveReviewsQty = 1, negativeReviewsQty = 1
                    )
                )
                assertEquals(4, recommendedShows.size)
                assertTrue(recommendedShows.containsAll(expectedRecommendedShows))
            }
        }
    }
})

