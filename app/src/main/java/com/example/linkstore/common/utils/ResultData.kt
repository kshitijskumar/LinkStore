package com.example.linkstore.common.utils

sealed class ResultData<out SUCCESS, out FAILURE> {

    data class Success<SUCCESS>(val data: SUCCESS) : ResultData<SUCCESS, Nothing>()
    data class Error<FAILURE>(val error: FAILURE) : ResultData<Nothing, FAILURE>()

}