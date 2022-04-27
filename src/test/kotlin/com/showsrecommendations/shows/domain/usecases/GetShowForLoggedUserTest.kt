package com.showsrecommendations.shows.domain.usecases

import com.showsrecommendations.recommendations.domain.ports.RecommendationsRepository
import com.showsrecommendations.reviews.domain.ports.ReviewsRepository
import com.showsrecommendations.shows.domain.ports.ShowsRepository
import io.ktor.server.plugins.*
import io.mockk.every
import io.mockk.mockk

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class GetShowForLoggedUserTest : Spek({

    Feature("GetShowForUnloggedUser") {


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
                        id = "1"
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