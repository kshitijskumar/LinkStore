package com.example.linkstore.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@Suppress("LeakingThis")
abstract class MviViewModel<STATE, INTENT, PARTIAL_CHANGE : BasePartialChange<STATE>, SIDE_EFFECT> : ViewModel() {

    private val intentChannel = Channel<INTENT>()

    private val _sideEffect = MutableSharedFlow<SIDE_EFFECT>()
    val sideEffect: Flow<SIDE_EFFECT> = _sideEffect.asSharedFlow()

    val state: StateFlow<STATE>

    abstract fun getInitialState(): STATE
    abstract fun Flow<INTENT>.toPartialChange(): Flow<PARTIAL_CHANGE>
    abstract fun getSideEffects(flow: Flow<PARTIAL_CHANGE>): List<SIDE_EFFECT>

    init {
        val initialState = getInitialState()

        state = intentChannel
            .receiveAsFlow()
            .toPartialChange()
            .splitSideEffect()
            .scan(initialState) { oldState, change -> change.reduce(oldState) }
            .stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = initialState)
    }

    fun processIntent(intent: INTENT) = intentChannel.trySend(intent)

    private fun Flow<PARTIAL_CHANGE>.splitSideEffect(): Flow<PARTIAL_CHANGE> {
        val sideEffects = getSideEffects(this)
        sideEffects.forEach {
            _sideEffect.tryEmit(it)
        }
        return this
    }

}
