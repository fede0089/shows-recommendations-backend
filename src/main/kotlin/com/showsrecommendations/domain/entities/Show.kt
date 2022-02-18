package com.showsrecommendations.domain.entities

data class Show(val id:String,
                val title: String,
                val genre: String,
                val year:String,
                val cover:String,
                val type:String,
                val externalId:String)
