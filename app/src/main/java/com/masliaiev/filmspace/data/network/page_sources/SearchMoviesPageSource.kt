package com.masliaiev.filmspace.data.network.page_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.masliaiev.filmspace.data.mapper.ModelsMapper
import com.masliaiev.filmspace.data.network.ApiService
import com.masliaiev.filmspace.domain.entity.Movie
import retrofit2.HttpException
import java.io.IOException

class SearchMoviesPageSource(
    private val apiService: ApiService,
    private val mapper: ModelsMapper,
    private val language: String,
    private val query: String
) :
    PagingSource
    <Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int {
        return FIRST_PAGE
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        val position = params.key ?: 1

        return try {
            val response = apiService.searchMovie(
                language = language,
                page = position,
                query = query
            )
            val moviesList = response.body()?.results?.map {
                mapper.mapMovieDtoToMovieEntity(it)
            }
            LoadResult.Page(
                data = moviesList!!,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (moviesList.isEmpty()) null else position + 1
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)

        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}