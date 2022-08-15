package com.example.linkstore.common.utils

import kotlin.coroutines.CoroutineContext

interface IDispatchersProvider {
    val mainDispatcher: CoroutineContext
    val ioDispatcher: CoroutineContext
    val defaultDispatcher: CoroutineContext
}