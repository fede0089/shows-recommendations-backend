package com.showsrecommendations

interface UseCase<Request, Response> {
    operator fun invoke(request: Request): Response
}