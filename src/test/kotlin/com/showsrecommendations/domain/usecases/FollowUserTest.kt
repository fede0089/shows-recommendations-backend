package com.showsrecommendations.domain.usecases

import com.showsrecommendations.domain.entities.FollowedUser
import com.showsrecommendations.domain.entities.Review
import com.showsrecommendations.domain.ports.FollowedUsersRepository
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

            Given("A follow user request from User1 to follow User2"){
                followUserRequest = FollowUser.Request(userId = "User1", followedUserId = "User2")
            }

            And("User1 is not following UserII") {
                every {
                    followedUsersRepository.getFollowedUser(
                        userId = followUserRequest.userId,
                        followedUserId = followUserRequest.followedUserId
                    )
                } returns null
            }

            When("User1 follows User2") {
                followUser(followUserRequest)
            }
            Then("User2 was added to User1 circle") {
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

            Given("A follow user request from User1 to follow User2"){
                followUserRequest = FollowUser.Request(userId = "User1", followedUserId = "User2")
            }

            And("User1 is already following UserII") {
                every {
                    followedUsersRepository.getFollowedUser(
                        userId = followUserRequest.userId,
                        followedUserId = followUserRequest.followedUserId
                    )
                } returns FollowedUser(id = followUserRequest.followedUserId, followedDate = "20220223")
            }

            When("User1 follows User2") {
                exception = assertThrows<FollowUser.AlreadyFollowedUser> {
                    followUser(followUserRequest)
                }
            }

            Then("User2 is not followed by User1") {
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