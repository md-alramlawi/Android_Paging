package com.ramalwi.paging.di

import com.ramalwi.paging.data.UserRepository
import com.ramalwi.paging.data.UsersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideUsersApi(): UsersApi = UsersApi()

    @Provides
    fun provideUserRepository(api: UsersApi): UserRepository = UserRepository(api)
}