package com.showsrecommendations.domain.ports

import com.showsrecommendations.domain.entities.FollowedUser
import com.showsrecommendations.domain.entities.User

interface FollowedUsersRepository {
    fun getFollowedUser(userId: String, followedUserId: String): FollowedUser?
    fun getFollowedUsers(userId: String): List<FollowedUser>
    fun followUser(userId: String, followedUserId:String): FollowedUser
}