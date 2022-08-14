package com.example.linkstore.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@Suppress("LeakingThis")
abstract class MviViewModel<STATE, INTENT, PARTIAL_CHANGE : BasePartialChange<STATE>, SIDE_EFFECT> : ViewModel() {

    private val intentFlow = MutableSharedFlow<INTENT>()

    private val sideEffectChannel = Channel<SIDE_EFFECT>(capacity = Channel.BUFFERED)
    val sideEffect: Flow<SIDE_EFFECT> = sideEffectChannel.receiveAsFlow()

    val state: StateFlow<STATE>

    abstract fun getInitialState(): STATE
    abstract fun Flow<INTENT>.toPartialChange(): Flow<PARTIAL_CHANGE>
    abstract fun getSideEffects(change: PARTIAL_CHANGE): List<SIDE_EFFECT>

    init {
        val initialState = getInitialState()

        state = intentFlow
            .onEach { Log.d("ViewModelStuff", "intent: $it") }
            .toPartialChange()
            .onEach { Log.d("ViewModelStuff", "change: $it") }
            .splitSideEffect()
            .scan(initialState) { oldState, change -> change.reduce(oldState) }
            .catch { Log.d("ViewModelStuff", "catch3: $it") }
            .stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = initialState)
    }

    suspend fun processIntent(intent: INTENT) = intentFlow.emit(intent)

    private fun Flow<PARTIAL_CHANGE>.splitSideEffect(): Flow<PARTIAL_CHANGE> {
        return this.onEach { change ->
            val sideEffects = getSideEffects(change)
            sideEffects.forEach { effect ->
                sideEffectChannel.send(effect)
            }
        }
    }

}
