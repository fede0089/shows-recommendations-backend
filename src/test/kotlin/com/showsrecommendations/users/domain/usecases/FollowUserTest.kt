package com.showsrecommendations.users.domain.usecases

import com.showsrecommendations.users.domain.entities.FollowedUser
import com.showsrecommendations.users.domain.ports.FollowedUsersRepository
import com.showsrecommendations.users.domain.usecases.FollowUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class FollowUserTest : Spek({
    Feature("Follow User") {

        Scenario("Following a new user") {

            val followedUsersRepository = mockk<FollowedUsersRepository>(relaxed = true)
            val followUser = FollowUser(
                followedUsersRepository = followedUsersRepository
            )
            lateinit var followUserRequest: FollowUser.Request

            Given("A follow user request from user 1 to follow user 2"){
                followUserRequest = FollowUser.Request(userId = "1", followedUserId = "2")
            }

            And("user 1 is not following user 2") {
                every {
                    followedUsersRepository.getFollowedUser(
                        userId = followUserRequest.userId,
                        followedUserId = followUserRequest.followedUserId
                    )
                } returns null
            }

            When("user 1 follows user 2") {
                followUser(followUserRequest)
            }
            Then("user 2 was added to user 1 circle") {
                verify(exactly = 1) {
                    followedUsersRepository.followUser(
                        userId = followUserRequest.userId,
                        followedUserId = followUserRequest.followedUserId
                    )
                }
            }
        }

        Scenario("Following an already followed user") {

            val followedUsersRepository = mockk<FollowedUsersRepository>(relaxed = true)
            val followUser = FollowUser(
                followedUsersRepository = followedUsersRepository
            )
            lateinit var followUserRequest: FollowUser.Request
            lateinit var exception: Throwable

            Given("A follow user request from user 1 to follow user 2"){
                followUserRequest = FollowUser.Request(userId = "1", followedUserId = "2")
            }

            And("user 1 is already following UserII") {
                every {
                    followedUsersRepository.getFollowedUser(
                        userId = followUserRequest.userId,
                        followedUserId = followUserRequest.followedUserId
                    )
                } returns FollowedUser(id = followUserRequest.followedUserId, followedDate = "20220223")
            }

            When("user 1 follows user 2") {
                exception = assertThrows<FollowUser.AlreadyFollowedUser> {
                    followUser(followUserRequest)
                }
            }

            Then("user 2 is not followed by user 1") {
                verify(exactly = 0) {
                    followedUsersRepository.followUser(
                        userId = followUserRequest.userId,
                        followedUserId = followUserRequest.followedUserId
                    )
                }
            }
        }
    }
})