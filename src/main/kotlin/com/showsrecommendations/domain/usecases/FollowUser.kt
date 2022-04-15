package com.showsrecommendations.domain.usecases
import com.showsrecommendations.domain.ports.FollowedUsersRepository
import io.ktor.server.plugins.*

class FollowUser(private val followedUsersRepository: FollowedUsersRepository): UseCase<FollowUser.Request, FollowUser.Response> {

    override fun invoke(followUserRequest: Request): Response {

        val followedUser = followedUsersRepository.getFollowedUser(followUserRequest.userId, followUserRequest.followedUserId)

        if(followedUser != null){
            throw AlreadyFollowedUser()
        }

        val newFollowedUser =
            followedUsersRepository.followUser(followUserRequest.userId, followUserRequest.followedUserId)

        return Response(followedUserId = newFollowedUser.id, followedDate = newFollowedUser.followedDate)
    }

    data class Request(val userId: String, val followedUserId: String)
    data class Response(val followedUserId: String, val followedDate: String)
    class AlreadyFollowedUser : BadRequestException(message = "User is already followed")
}

