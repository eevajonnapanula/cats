package com.eevajonna.cats.data

import retrofit2.Response
import retrofit2.http.GET

interface CatsService {
    @GET("/cat?json=true")
    suspend fun getCat(): Response<CatResponse>
}
