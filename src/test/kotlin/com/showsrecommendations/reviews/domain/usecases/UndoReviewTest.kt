package com.showsrecommendations.reviews.domain.usecases

import com.showsrecommendations.reviews.domain.entities.Review
import com.showsrecommendations.reviews.domain.ports.ReviewsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class UndoReviewTest : Spek({

    Feature("UndoReview") {

        Scenario("Undoing a review on a reviewed show") {

            val reviewsRepository = mockk<ReviewsRepository>(relaxed = true)
            val undoReview = UndoReview(reviewsRepository = reviewsRepository)
            lateinit var request: UndoReview.Request

            Given("An undo review request from user 1 on show 1") {
                request = UndoReview.Request(
                    userId = "1",
                    showId = "1"
                )
            }

            And("user 1 has a previous review on show 1") {
                every {
                    reviewsRepository.getReview(
                        userId = request.userId,
                        showId = request.showId
                    )
                } returns Review(showId = request.showId, rating = 0f, createdDate = "20220223")
            }

            When("The review is undo") {
                undoReview(request)
            }

            Then("The review is undo") {
                verify(exactly = 1) {
                    reviewsRepository.undoReview(
                        userId = request.userId,
                        showId = request.showId
                    )
                }
            }
        }

        Scenario("Undoing a review on a not reviewed show") {

            val reviewsRepository = mockk<ReviewsRepository>(relaxed = true)
            val undoReview = UndoReview(reviewsRepository = reviewsRepository)
            lateinit var request: UndoReview.Request
            lateinit var exception: Throwable

            Given("An undo review request from user 1 on show 1") {
                request = UndoReview.Request(
                    userId = "1",
                    showId = "1"
                )
            }

            And("user 1 has not a previous review on show 1") {
                every {
                    reviewsRepository.getReview(
                        userId = request.userId,
                        showId = request.showId
                    )
                } returns null
            }

            When("The review is undo") {
                exception = assertThrows<UndoReview.NotReviewedShowException> {
                    undoReview(request)
                }
            }

            Then("Review is not undo") {
                verify(exactly = 0) {
                    reviewsRepository.undoReview(
                        userId = request.userId,
                        showId = request.showId
                    )
                }
            }

            And("An exception is thrown") {
                assertNotNull(exception)
            }
        }

    }
})