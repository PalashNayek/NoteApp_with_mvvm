package com.example.noteapp_with_mvvm.di

import com.example.noteapp_with_mvvm.api.AuthInterceptor
import com.example.noteapp_with_mvvm.api.MyApi
import com.example.noteapp_with_mvvm.api.NotesApi
import com.example.noteapp_with_mvvm.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): MyApi {
        return retrofitBuilder.build().create(MyApi::class.java)
    }

    @Singleton
    @Provides
    fun providesNoteAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): NotesApi {
        return retrofitBuilder.client(okHttpClient).build().create(NotesApi::class.java)
    }
}