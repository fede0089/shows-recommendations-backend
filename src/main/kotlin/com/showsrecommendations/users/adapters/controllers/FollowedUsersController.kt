package com.showsrecommendations.users.adapters.controllers

import com.showsrecommendations.users.domain.usecases.FollowUser

class FollowedUsersController(private val followUser: FollowUser) {
    fun followUser(userId: String, followedUserId: String): FollowUser.Response {
        val followUserRequest = FollowUser.Request(userId = userId, followedUserId = followedUserId)
        return followUser.invoke(followUserRequest)
    }
}
