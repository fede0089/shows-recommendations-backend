package com.showsrecommendations.adapters.controllers

import com.showsrecommendations.domain.usecases.FollowUser

class FollowedUsersController(private val followUser: FollowUser) {
    fun followUser(userId: String, followedUserId: String): FollowUser.Response {
        val followUserRequest = FollowUser.Request(userId = userId, followedUserId = followedUserId)
        return followUser.invoke(followUserRequest)
    }
}
