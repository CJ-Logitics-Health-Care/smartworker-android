package com.devjsg.cj_logistics_future_technology.presentation.home.admin.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.devjsg.cj_logistics_future_technology.data.model.EditableMember
import com.devjsg.cj_logistics_future_technology.data.model.Member
import com.devjsg.cj_logistics_future_technology.presentation.home.admin.MemberItem
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.AdminViewModel

@Composable
fun MemberListScreen(
    navController: NavController,
    viewModel: AdminViewModel,
    members: LazyPagingItems<Member>,
    selectedMember: EditableMember?,
    searchResult: List<Member>?,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onEditClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("회원 아이디로 검색") },
            trailingIcon = {
                IconButton(onClick = onSearch) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        searchResult?.let { searchResults ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(searchResults) { _, member ->
                    MemberItem(
                        member = member,
                        onEditClick = { onEditClick(member.memberId.toString()) },
                        onItemClick = { navController.navigate("detail_member/${member.memberId}") }
                    )
                }
            }
        } ?: LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(members.itemCount) { index ->
                val member = members[index]
                member?.let {
                    MemberItem(
                        member = it,
                        onEditClick = { onEditClick(it.memberId.toString()) },
                        onItemClick = { navController.navigate("detail_member/${it.memberId}") }
                    )
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.logout {
                    navController.navigate("login") {
                        popUpTo("worker_home") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "로그아웃")
        }
    }
}