package com.showsrecommendations.users.domain.ports

import com.showsrecommendations.users.domain.entities.FollowedUser

interface FollowedUsersRepository {
    fun getFollowedUser(userId: String, followedUserId: String): FollowedUser?
    fun getFollowedUsers(userId: String): List<FollowedUser>
    fun followUser(userId: String, followedUserId:String): FollowedUser
}