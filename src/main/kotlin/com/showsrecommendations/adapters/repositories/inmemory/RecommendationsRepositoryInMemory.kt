package com.showsrecommendations.adapters.repositories.inmemory

import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.ports.RecommendationsRepository

class RecommendationsRepositoryInMemory(val recommendations: MutableMap<String, List<Recommendation>> = mutableMapOf()): RecommendationsRepository, MutableMap<String, List<Recommendation>> by recommendations {
      override fun getRecommendations(userId: String): List<Recommendation> =
          recommendations[userId]!!
}