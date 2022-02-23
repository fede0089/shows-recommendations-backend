package com.showsrecommendations.domain.usecases

interface UseCase<Request, Response> {
    operator fun invoke(request: Request): Response
}