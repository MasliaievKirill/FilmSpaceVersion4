package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.entity.Video
import com.masliaiev.filmspace.domain.repository.AppRepository
import com.masliaiev.filmspace.helpers.ResultParams
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun getVideos(movieId: Int): Pair<ResultParams, Video?> {
        return repository.getVideos(movieId)
    }
}