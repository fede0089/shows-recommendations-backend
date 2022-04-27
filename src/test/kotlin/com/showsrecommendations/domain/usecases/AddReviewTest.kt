package com.showsrecommendations.domain.usecases

import com.showsrecommendations.domain.entities.Review
import com.showsrecommendations.domain.ports.ReviewsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class AddReviewTest : Spek({

    Feature("AddReview") {


        Scenario("Reviewing an unseen show") {

            val reviewsRepository = mockk<ReviewsRepository>(relaxed = true)
            val addReview = AddReview(reviewsRepository = reviewsRepository)
            lateinit var addReviewRequest: AddReview.Request

            Given("A positive review request from User1 on Movie I") {
                addReviewRequest = AddReview.Request(
                    userId = "user1",
                    showId = "MovieI",
                    rating = 1f
                )

            }

            And("User1 has no previous reviews") {
                every {
                    reviewsRepository.getReview(
                        userId = addReviewRequest.userId,
                        showId = addReviewRequest.showId
                    )
                } returns null
            }

            When("The review is added") {
                addReview(addReviewRequest)
            }

            Then("The review is stored") {
                verify(exactly = 1) {
                    reviewsRepository.addReview(
                        userId = addReviewRequest.userId,
                        showId = addReviewRequest.showId,
                        rating = addReviewRequest.rating
                    )
                }
            }
        }


        Scenario("? Reviewing a seen show") {

            val reviewsRepository = mockk<ReviewsRepository>(relaxed = true)
            val addReview = AddReview(reviewsRepository = reviewsRepository)
            lateinit var addReviewRequest: AddReview.Request
            lateinit var exception: Throwable


            Given("A positive review request from User1 on Movie I") {
                addReviewRequest = AddReview.Request(
                    userId = "user1",
                    showId = "MovieI",
                    rating = 1f
                )

            }

            And("User1 has a previous review on Movie I") {
                every {
                    reviewsRepository.getReview(
                        userId = addReviewRequest.userId,
                        showId = addReviewRequest.showId
                    )
                } returns Review(showId = addReviewRequest.showId, rating = 0f, createdDate = "20220223")
            }

            When("The review is added") {
                exception = assertThrows<AddReview.AlreadyReviewedShowException> {
                    addReview(addReviewRequest)
                }
            }

            Then("Review is not stored") {
                verify(exactly = 0) {
                    reviewsRepository.addReview(
                        userId = addReviewRequest.userId,
                        showId = addReviewRequest.showId,
                        rating = addReviewRequest.rating
                    )
                }
            }

            And("An exception is thrown") {
                assertNotNull(exception)
            }
        }

    }
})