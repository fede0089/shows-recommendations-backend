package com.showsrecommendations.shows.domain.usecases

import com.showsrecommendations.recommendations.domain.entities.Recommendation
import com.showsrecommendations.recommendations.domain.ports.RecommendationsRepository
import com.showsrecommendations.reviews.domain.entities.Review
import com.showsrecommendations.reviews.domain.ports.ReviewsRepository
import com.showsrecommendations.shows.domain.ports.ShowsRepository
import com.showsrecommendations.shows.motherobjects.ShowMotherObject
import io.ktor.server.plugins.*
import io.mockk.every
import io.mockk.mockk

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.time.LocalDateTime

class GetShowForLoggedUserTest : Spek({

    Feature("GetShowForLoggedUser") {


        Scenario("Getting an existing unseen show with no recommendations") {

            val showsRepository = mockk<ShowsRepository>(relaxed = true)
            val recommendationsRepository = mockk<RecommendationsRepository>(relaxed = true)
            val reviewsRepository = mockk<ReviewsRepository>(relaxed = true)

            val getShowForLoggedUser = GetShowForLoggedUser(
                showsRepository = showsRepository,
                recommendationsRepository = recommendationsRepository,
                reviewsRepository = reviewsRepository
            )

            lateinit var request: GetShowForLoggedUser.Request
            lateinit var response: GetShowForLoggedUser.Response

            Given("A request from user 1 to get show 1") {
                request = GetShowForLoggedUser.Request(userId = "1", showId = "1")
            }

            And("Show 1 exists") {
                every {
                    showsRepository.getShow(
                        id = request.showId
                    )
                } returns ShowMotherObject.buildShow(id = request.showId)
            }

            And("it was not reviewed by user 1"){
                every {
                    reviewsRepository.getReview(
                        showId = request.showId,
                        userId = request.userId
                    )
                } returns null
            }

            And("it was not reviewed by any followed users"){
                every {
                    recommendationsRepository.getRecommendation(
                        showId = request.showId,
                        userId = request.userId
                    )
                } returns null
            }

            When("The show is retrieved") {
                response =  getShowForLoggedUser(request)
            }

            Then("The show is returned") {
                assertNotNull(response)
                assertEquals(response.id, request.showId)
                assertNull(response.userReview)
                assertEquals(response.positiveReviewsQty, 0)
                assertEquals(response.negativeReviewsQty, 0)
            }
        }

        Scenario("Getting an existing unseen show with recommendations") {

            val showsRepository = mockk<ShowsRepository>(relaxed = true)
            val recommendationsRepository = mockk<RecommendationsRepository>(relaxed = true)
            val reviewsRepository = mockk<ReviewsRepository>(relaxed = true)

            val getShowForLoggedUser = GetShowForLoggedUser(
                showsRepository = showsRepository,
                recommendationsRepository = recommendationsRepository,
                reviewsRepository = reviewsRepository
            )

            lateinit var request: GetShowForLoggedUser.Request
            lateinit var response: GetShowForLoggedUser.Response

            Given("A request from user 1 to get show 1") {
                request = GetShowForLoggedUser.Request(userId = "1", showId = "1")
            }

            And("Show 1 exists") {
                every {
                    showsRepository.getShow(
                        id = request.showId
                    )
                } returns ShowMotherObject.buildShow(id = request.showId)
            }

            And("it was not reviewed by user 1"){
                every {
                    reviewsRepository.getReview(
                        showId = request.showId,
                        userId = request.userId
                    )
                } returns null
            }

            And("it was reviewed by some followed users"){
                every {
                    recommendationsRepository.getRecommendation(
                        showId = request.showId,
                        userId = request.userId
                    )
                } returns Recommendation(showId = request.showId, positiveReviewsQty = 10, negativeReviewsQty = 1)
            }

            When("The show is retrieved") {
                response =  getShowForLoggedUser(request)
            }

            Then("The show is returned") {
                assertNotNull(response)
                assertEquals(response.id, request.showId)
                assertNull(response.userReview)
                assertEquals(response.positiveReviewsQty, 10)
                assertEquals(response.negativeReviewsQty, 1)
            }
        }

        Scenario("Getting an existing seen show with no recommendations") {

            val showsRepository = mockk<ShowsRepository>(relaxed = true)
            val recommendationsRepository = mockk<RecommendationsRepository>(relaxed = true)
            val reviewsRepository = mockk<ReviewsRepository>(relaxed = true)

            val getShowForLoggedUser = GetShowForLoggedUser(
                showsRepository = showsRepository,
                recommendationsRepository = recommendationsRepository,
                reviewsRepository = reviewsRepository
            )

            lateinit var request: GetShowForLoggedUser.Request
            lateinit var response: GetShowForLoggedUser.Response

            Given("A request from user 1 to get show 1") {
                request = GetShowForLoggedUser.Request(userId = "1", showId = "1")
            }

            And("Show 1 exists") {
                every {
                    showsRepository.getShow(
                        id = request.showId
                    )
                } returns ShowMotherObject.buildShow(id = request.showId)
            }

            And("it was reviewed by user 1"){
                every {
                    reviewsRepository.getReview(
                        showId = request.showId,
                        userId = request.userId
                    )
                } returns Review(showId = request.showId, rating = 1f, createdDate = LocalDateTime.now().toString())
            }

            And("it was not recommended by any followed users"){
                every {
                    recommendationsRepository.getRecommendation(
                        showId = request.showId,
                        userId = request.userId
                    )
                } returns null
            }

            When("The show is retrieved") {
                response =  getShowForLoggedUser(request)
            }

            Then("The show is returned") {
                assertNotNull(response)
                assertEquals(response.id, request.showId)
                assertEquals(response.userReview?.rating, 1f)
                assertEquals(response.positiveReviewsQty, 0)
                assertEquals(response.negativeReviewsQty, 0)
            }
        }

        Scenario("Getting an un-existing show") {

            val showsRepository = mockk<ShowsRepository>(relaxed = true)
            val recommendationsRepository = mockk<RecommendationsRepository>(relaxed = true)
            val reviewsRepository = mockk<ReviewsRepository>(relaxed = true)

            val getShowForLoggedUser = GetShowForLoggedUser(
                showsRepository = showsRepository,
                recommendationsRepository = recommendationsRepository,
                reviewsRepository = reviewsRepository
            )

            lateinit var request: GetShowForLoggedUser.Request
            lateinit var exception: Throwable

            Given("A request from user 1 to get show 1") {
                request = GetShowForLoggedUser.Request(userId = "1", showId = "1")
            }

            And("Show 1 does not exists") {
                every {
                    showsRepository.getShow(
                        id = request.showId
                    )
                } returns null
            }

            When("The show is retrieved") {
                exception = assertThrows<NotFoundException> {
                    getShowForLoggedUser(request)
                }
            }

            Then("An exception is thrown") {
                assertNotNull(exception)
            }
        }

    }
})