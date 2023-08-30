package com.shottracker.feature.home.data


import com.shottracker.feature.home.models.pbp.PlayByPlay
import com.shottracker.network.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PlayByPlayRepositoryImpl(
    private val remoteDataSource: PlayByPlayDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlayByPlayRepository {

    override suspend fun getPlayByPlay(gameId: String): Result<PlayByPlay> {
        return remoteDataSource.getPlayByPlay(gameId)
    }

}