package com.showsrecommendations.adapters.repositories.inmemory

import com.showsrecommendations.domain.entities.FollowedUser
import com.showsrecommendations.domain.entities.Review
import com.showsrecommendations.domain.entities.User
import com.showsrecommendations.domain.ports.UsersRepository
import java.time.LocalDateTime

class UsersRepositoryInMemory(val users: MutableMap<String, User> = mutableMapOf()): UsersRepository, MutableMap<String, User> by users{
    override fun getUser(userId: String): User =
        users[userId]!!
}