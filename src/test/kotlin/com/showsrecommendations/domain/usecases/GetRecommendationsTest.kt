package com.showsrecommendations.domain.usecases
import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.entities.Review
import com.showsrecommendations.domain.ports.RecommendationsRepository
import com.showsrecommendations.domain.ports.ReviewsRepository
import com.showsrecommendations.domain.ports.ShowsRepository
import com.showsrecommendations.motherobjects.ShowObjectMother
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

import org.junit.jupiter.api.Assertions.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class GetRecommendationsTest : Spek({

    Feature("GetRecommendations") {

        val reviewsRepository = mockk<ReviewsRepository>(relaxed = true)
        val recommendationsRepository = mockk<RecommendationsRepository>(relaxed = true)
        val showsRepository = mockk<ShowsRepository>(relaxed = true)

        val getRecommendations = GetRecommendations(
            recommendationsRepository = recommendationsRepository,
            reviewsRepository = reviewsRepository,
            showsRepository = showsRepository
        )

        var recommendedShowsTitles: List<String> = listOf()

        Scenario("Getting recommendations") {

            Given("some recommendations already calculated for user1") {

                every {
                    recommendationsRepository.getRecommendations(userId = "user1")
                } returns listOf(
                    Recommendation(showId = "1", positiveReviewsQty = 4, negativeReviewsQty = 0),
                    Recommendation(showId = "3", positiveReviewsQty = 4, negativeReviewsQty = 1),
                    Recommendation(showId = "5", positiveReviewsQty = 1, negativeReviewsQty = 1),
                    Recommendation(showId = "6", positiveReviewsQty = 1, negativeReviewsQty = 0),
                    Recommendation(showId = "show-erased", positiveReviewsQty = 1, negativeReviewsQty = 0)
                )
            }

            And("one of them was already reviewed by user1") {
                every {
                    reviewsRepository.getReviews(userId = "user1")
                } returns listOf(
                    Review(showId = "6", rating = 1f, createdDate = "20220218")
                )
            }

            And("a shows repository"){
                val showIdSlot = slot<String>()
                every {
                    showsRepository.getShow(id = capture(showIdSlot))
                } answers {
                    if ((showIdSlot.captured) != "show-erased")
                        ShowObjectMother.buildShow(showIdSlot.captured)
                    else
                        null
                }
            }

            When("user1 gets its recommendations") {
                recommendedShowsTitles = getRecommendations(GetRecommendations.Request(userId = "user1")).recommendations.map { it.id}
            }

            Then("the reviewed show is removed and the rest are ordered by its rating (desc)") {
                val expectedShowsIds = listOf("1", "3", "5")
                assertEquals(expectedShowsIds.size, recommendedShowsTitles.size)
                assertEquals(expectedShowsIds, recommendedShowsTitles)
            }
        }
    }
})