package com.showsrecommendations.domain.usecases

import com.showsrecommendations.adapters.repositories.mocked.RecommendationsRepositoryMocked
import com.showsrecommendations.domain.entities.Recommendation
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GetRecommendationsTest {

    private val getRecommendations: GetRecommendations =
        GetRecommendations(
            recommendationsRepository = RecommendationsRepositoryMocked(
                recommendations = mapOf(
                    "userId" to listOf(
                        Recommendation(showId = "MovieI", recommendationsQty = 4, unrecommendationsQty = 0),
                        Recommendation(showId = "MovieIII", recommendationsQty = 4, unrecommendationsQty = 1),
                        Recommendation(showId = "MovieV", recommendationsQty = 1, unrecommendationsQty = 1),
                        Recommendation(showId = "MovieVI", recommendationsQty = 1, unrecommendationsQty = 0)
                    )
                )
            )
        )

    @Test
    fun test() {
        val expectedRecommendations = 3
        val recommendations = getRecommendations(userId = "userId")
        assertEquals(expectedRecommendations, recommendations.size)
    }

}