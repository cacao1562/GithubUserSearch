package com.example.githubusersearch.di

import com.example.githubusersearch.BuildConfig
import com.example.githubusersearch.Constants
import com.example.githubusersearch.api.GithubService
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor.Logger { message ->
            if (!message.startsWith("{") && !message.startsWith("[")) {
                Timber.tag("OkHttp").d(message)
                return@Logger
            }
            try {
                val source = Buffer().writeUtf8(message)
                val reader = JsonReader.of(source)
                val value = reader.readJsonValue()
                val adapter = Moshi.Builder().build().adapter<Any>(Any::class.java).indent("    ")
                val result = adapter.toJson(value)
                Timber.tag("OkHttp_Moshi").d(result)
            }catch (e: Exception) {
                e.printStackTrace()
                Timber.tag("OkHttp").d(message)
            }
        }
        val logging = HttpLoggingInterceptor(logger)
        if (BuildConfig.DEBUG) logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.interceptors().add(logging)
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit): GithubService {
        return retrofit.create(GithubService::class.java)
    }

}


