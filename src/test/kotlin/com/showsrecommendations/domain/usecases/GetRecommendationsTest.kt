package com.showsrecommendations.domain.usecases

import com.showsrecommendations.adapters.repositories.inmemory.RecommendationsRepositoryInMemory
import com.showsrecommendations.adapters.repositories.inmemory.ReviewsRepositoryInMemory
import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.entities.Review

import org.junit.jupiter.api.Assertions.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class GetRecommendationsTest: Spek({

    Feature("2.1 GetRecommendations") {

        val reviewsRepository = ReviewsRepositoryInMemory()
        val recommendationsRepository = RecommendationsRepositoryInMemory()

        val getRecommendations = GetRecommendations(
            recommendationsRepository= recommendationsRepository,
            reviewsRepository = reviewsRepository)

        var recommendedShowsIds: List<String> = listOf()

        Scenario("2.1.2 Getting recommendations") {

            Given("some recommendations already calculated for user1")  {

                recommendationsRepository["user1"] = listOf(
                    Recommendation(showId = "MovieI", positiveReviewsQty = 4, negativeReviewsQty = 0),
                    Recommendation(showId = "MovieIII", positiveReviewsQty = 4, negativeReviewsQty = 1),
                    Recommendation(showId = "MovieV", positiveReviewsQty = 1, negativeReviewsQty = 1),
                    Recommendation(showId = "MovieVI", positiveReviewsQty = 1, negativeReviewsQty = 0)
                )

            }

            And("one of them was already reviewed by user1"){
                reviewsRepository["user1"] = listOf(Review(showId = "MovieVI", rating = 1f, createdDate = "20220218", lastUpdated = "20220218"))
            }

            When("user1 gets its recommendations") {
                recommendedShowsIds = getRecommendations(userId = "user1").map {it.showId}
            }

            Then("the reviewed show is removed and the rest are ordered by its rating (desc)"){
                assertEquals(3, recommendedShowsIds.size)
                assertEquals(recommendedShowsIds, listOf("MovieI","MovieIII", "MovieV"))
            }
        }
    }
})