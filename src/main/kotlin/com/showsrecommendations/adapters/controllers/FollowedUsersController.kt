package com.showsrecommendations.adapters.controllers

import com.showsrecommendations.domain.usecases.FollowUser

class FollowedUsersController(private val followUser: FollowUser) {
    fun followUser(followUserRequest: FollowUser.Request): FollowUser.Response =
        followUser.invoke(followUserRequest)
}
