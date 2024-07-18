package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.devjsg.cj_logistics_future_technology.data.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MemberDetailViewModel @Inject constructor(
    private val repository: MemberRepository
) : ViewModel() {



}