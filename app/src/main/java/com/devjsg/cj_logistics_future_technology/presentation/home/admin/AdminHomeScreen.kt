package com.devjsg.cj_logistics_future_technology.presentation.home.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.AdminViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    navController: NavController,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val members = viewModel.members.collectAsLazyPagingItems()
    val selectedMember by viewModel.selectedMember.collectAsState()
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetShadowElevation = 20.dp,
        sheetContainerColor = Color.White,
        sheetContent = {
            selectedMember?.let {
                EditMemberBottomSheet(
                    member = it,
                    onDismiss = { scope.launch { sheetState.bottomSheetState.hide() } },
                    onSave = { updatedMember ->
                        viewModel.updateMember(updatedMember, {
                            scope.launch { sheetState.bottomSheetState.hide() }
                        }, {
                            // Handle error
                        })
                    }
                )
            }
        },
        containerColor = Color.White,
        sheetPeekHeight = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(members.itemCount) { index ->
                    val member = members[index]
                    member?.let {
                        MemberItem(
                            member = it,
                            onEditClick = {
                                viewModel.getMemberInfo(it.memberId.toString())
                                scope.launch { sheetState.bottomSheetState.expand() }
                            }
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
}