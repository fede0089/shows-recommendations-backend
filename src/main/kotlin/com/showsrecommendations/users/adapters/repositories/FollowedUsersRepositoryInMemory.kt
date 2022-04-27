package com.showsrecommendations.users.adapters.repositories

import com.showsrecommendations.users.domain.entities.FollowedUser
import com.showsrecommendations.users.domain.ports.FollowedUsersRepository
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