package com.showsrecommendations.users.adapters.repositories

import com.showsrecommendations.users.domain.entities.User
import com.showsrecommendations.users.domain.ports.UsersRepository

class UsersRepositoryInMemory(val users: MutableMap<String, User> = mutableMapOf()): UsersRepository, MutableMap<String, User> by users{
    override fun getUser(userId: String): User =
        users[userId]!!
}