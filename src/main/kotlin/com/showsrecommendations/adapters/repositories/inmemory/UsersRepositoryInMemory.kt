package com.showsrecommendations.adapters.repositories.inmemory

import com.showsrecommendations.domain.entities.FollowedUser
import com.showsrecommendations.domain.entities.User
import com.showsrecommendations.domain.ports.UsersRepository

class UsersRepositoryInMemory(val users: MutableMap<String, User> = mutableMapOf(), val followedUsers: MutableMap<String, List<FollowedUser>> = mutableMapOf()): UsersRepository {

    override fun getUser(userId: String): User =
        users[userId]!!


    override fun getFollowedUsers(userId: String): List<FollowedUser> =
        followedUsers[userId]!!
}