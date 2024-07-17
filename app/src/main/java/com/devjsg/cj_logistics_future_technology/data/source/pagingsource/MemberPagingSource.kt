package com.devjsg.cj_logistics_future_technology.data.source.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.devjsg.cj_logistics_future_technology.data.model.Member
import com.devjsg.cj_logistics_future_technology.data.network.MemberApiService

class MemberPagingSource(
    private val apiService: MemberApiService,
    private val token: String
) : PagingSource<Int, Member>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Member> {
        return try {
            val currentIndex = params.key ?: 0
            val response = apiService.getMembers("Bearer $token", currentIndex)
            val members = response.data.value
            val nextKey = if (response.data.hasNext) response.data.lastIndex else null
            LoadResult.Page(
                data = members,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Member>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}