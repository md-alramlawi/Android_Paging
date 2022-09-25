package com.ramalwi.paging.data

class UserRepository(
    private val api: UsersApi
) {
    suspend fun getUsers(page: Int, limit: Int): UsersResponse {
        return api.getUsers(page, limit)
    }
}