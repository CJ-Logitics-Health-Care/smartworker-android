package com.devjsg.cj_logistics_future_technology.presentation.home.admin

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.AdminViewModel

@Composable
fun AdminHomeScreen(
    navController: NavController,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val members = viewModel.members.collectAsLazyPagingItems()

    LazyColumn {
        items(members.itemCount) { index ->
            val member = members[index]
            member?.let {
                MemberItem(member = it)
            }
        }

        members.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { CircularProgressIndicator() }
                }
                loadState.append is LoadState.Loading -> {
                    item { CircularProgressIndicator() }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = members.loadState.refresh as LoadState.Error
                    item { Text("Error: ${e.error.localizedMessage}") }
                }
                loadState.append is LoadState.Error -> {
                    val e = members.loadState.append as LoadState.Error
                    item { Text("Error: ${e.error.localizedMessage}") }
                }
            }
        }
    }

    Button(onClick = {
        viewModel.logout {
            navController.navigate("login") {
                popUpTo("worker_home") { inclusive = true }
            }
        }
    }) {
        Text(text = "로그아웃")
    }
}