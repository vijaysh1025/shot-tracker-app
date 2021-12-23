package com.shottracker.di

import android.content.Context
import androidx.room.Room
import com.shottracker.feature.home.data.DailyScheduleDataSource
import com.shottracker.feature.home.data.DailyScheduleRepository
import com.shottracker.feature.home.data.DailyScheduleRepositoryImpl
import com.shottracker.feature.home.data.local.DailyScheduleLocalDataSource
import com.shottracker.feature.home.data.remote.DailyScheduleRemoteDataSource
import com.shottracker.feature.home.data.remote.NbaDataService
import com.shottracker.feature.home.utils.network.ApiKeyInterceptor
import com.shottracker.feature.home.utils.network.LoggingInterceptor
import com.shottracker.roomdb.AppDatabase
import com.wapo.flagship.network.retrofit.network.CallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * App Level Dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Annotation for Remote Data Source
     */
    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteDSDataSource

    /**
     * Annotation for Local Data Source
     */
    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalDSDataSource

    /**
     * Provide this Data Source for [DailyScheduleDataSource] marked with @RemoteDSDataSource annotation
     */
    @Singleton
    @RemoteDSDataSource
    @Provides
    fun provideTasksRemoteDataSource(nbaDataService: NbaDataService, @ApplicationContext context: Context): DailyScheduleDataSource {
        return DailyScheduleRemoteDataSource(nbaDataService, context)
    }

    /**
     * Provide this Data Source for [DailyScheduleDataSource] marked with @LocalDSDataSource annotation
     */
    @Singleton
    @LocalDSDataSource
    @Provides
    fun provideTasksLocalDataSource(
        database: AppDatabase,
        ioDispatcher: CoroutineDispatcher
    ): DailyScheduleDataSource {
        return DailyScheduleLocalDataSource(
            database.dailyScheduleDao(), ioDispatcher
        )
    }

    /**
     * Provide App DataBase
     */
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "NbaStats.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}

/**
 * NBA Data Service Dependency at App Scope
 */
@Module
@InstallIn(SingletonComponent::class)
object NbaDataServiceModule{

    /**
     * Provide Nba Reftrofit service
     */
    @Singleton
    @Provides
    fun provideNbaDataService(
        okHttpClient: OkHttpClient,
        callAdapterFactory: CallAdapter.Factory,
    ): NbaDataService {
        return Retrofit.Builder()
            .baseUrl(NbaDataService.BASE_URL)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient).build()
            .create(NbaDataService::class.java)
    }

    /**
     * Provide call adaptor Factory to convert Call to Result
     */
    @Singleton
    @Provides
    fun provideCallAdapterFactory(): CallAdapter.Factory =
        CallAdapterFactory()

    /**
     * Provide OkHttpClient with required interceptors
     * ApiKeyInterceptor - Add api_key param to request
     * LoggingInterceptor - Logs network request and response
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(NbaDataService.API_KEY))
            .addInterceptor(LoggingInterceptor(NbaDataService.Name))
            .build()
    }
}

/**
 * Nba Repository Module
 */
@Module
@InstallIn(SingletonComponent::class)
object NbaRepositoryModule {

    /**
     * Provide repository to get Daily Schedule of games.
     */
    @Singleton
    @Provides
    fun provideDailyScheduleRepository(
        @AppModule.RemoteDSDataSource remoteTasksDataSource: DailyScheduleDataSource,
        @AppModule.LocalDSDataSource localTasksDataSource: DailyScheduleDataSource,
        ioDispatcher: CoroutineDispatcher
    ): DailyScheduleRepository {
        return DailyScheduleRepositoryImpl(
            localTasksDataSource, remoteTasksDataSource, ioDispatcher
        )
    }
}