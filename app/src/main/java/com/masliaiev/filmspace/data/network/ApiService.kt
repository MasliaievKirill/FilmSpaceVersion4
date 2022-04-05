package com.masliaiev.filmspace.data.network

import com.masliaiev.filmspace.data.network.models.requests.CreateSessionRequestDto
import com.masliaiev.filmspace.data.network.models.requests.DeleteSessionRequestDto
import com.masliaiev.filmspace.data.network.models.responses.CreateRequestTokenResponseDto
import com.masliaiev.filmspace.data.network.models.responses.CreateSessionResponseDto
import com.masliaiev.filmspace.data.network.models.responses.DeleteSessionResponseDto
import retrofit2.http.*

interface ApiService {

    @GET("authentication/token/new")
    suspend fun createRequestToken(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY
    ) : CreateRequestTokenResponseDto

    @POST("authentication/session/new")
    suspend fun createSession(
        @Body createSessionRequestDto: CreateSessionRequestDto
    ) : CreateSessionResponseDto

    @DELETE("authentication/session")
    suspend fun deleteSessionRequest(
        @Body deleteSessionRequestDto: DeleteSessionRequestDto
    ): DeleteSessionResponseDto

    @GET("account")
    suspend fun getAccountDetails(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_SESSION_ID) sessionId: String
    )

    companion object{

        private const val QUERY_PARAM_API_KEY = "api_key"
        private const val QUERY_PARAM_SESSION_ID = "session_id"

        private const val API_KEY = "9f9d136877ade7608f32a571c18756be"

    }
}