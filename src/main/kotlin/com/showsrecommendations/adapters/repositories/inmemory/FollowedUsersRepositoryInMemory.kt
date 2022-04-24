package com.showsrecommendations.adapters.repositories.inmemory

import com.showsrecommendations.domain.entities.FollowedUser
import com.showsrecommendations.domain.entities.Review
import com.showsrecommendations.domain.entities.User
import com.showsrecommendations.domain.ports.FollowedUsersRepository
import com.showsrecommendations.domain.ports.UsersRepository
import java.time.LocalDateTime

class FollowedUsersRepositoryInMemory(private val followedUsers: MutableMap<String, MutableList<FollowedUser>> = mutableMapOf()): FollowedUsersRepository, MutableMap<String, MutableList<FollowedUser>> by followedUsers {

    override fun getFollowedUsers(userId: String): List<FollowedUser> =
        followedUsers[userId]?: emptyList()

    override fun getFollowedUser(userId: String, followedUserId: String): FollowedUser? =
        getFollowedUsers(userId).find { it.id == followedUserId }

    override fun followUser(userId: String, followedUserId: String): FollowedUser {
        val now:String = LocalDateTime.now().toString()
        val followedUser = FollowedUser(id = followedUserId, followedDate = now)
        followedUsers
            .getOrPut(userId) {mutableListOf()}
            .add(followedUser)
        return followedUser
    }
}