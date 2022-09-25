package com.ramalwi.paging.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramalwi.paging.ui.components.UserList
import com.ramalwi.paging.ui.theme.PagingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PagingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val lazyList = viewModel.usersPager.collectAsLazyPagingItems()
                    val combinedLoadStates = lazyList.loadState

                    UserList(usersList = lazyList, combinedLoadStates = combinedLoadStates)
                }
            }
        }
    }
}
