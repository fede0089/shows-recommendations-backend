package com.showsrecommendations.domain.usecases

import com.showsrecommendations.domain.ports.ShowsRepository
import com.showsrecommendations.motherobjects.ShowObjectMother
import io.ktor.server.plugins.*
import io.mockk.every
import io.mockk.mockk

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class GetShowForUnloggedUserTest : Spek({

    Feature("GetShowForUnloggedUser") {


        Scenario("Getting an un-existing show") {

            val showsRepository = mockk<ShowsRepository>(relaxed = true)
            val getShowForUnloggedUser = GetShowForUnloggedUser(showsRepository = showsRepository)
            lateinit var request: GetShowForUnloggedUser.Request
            lateinit var exception: Throwable

            Given("A request to get show 1") {
                request = GetShowForUnloggedUser.Request(showId = "1")
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
                    getShowForUnloggedUser(request)
                }
            }

            Then("An exception is thrown") {
                assertNotNull(exception)
            }
        }

    }
})