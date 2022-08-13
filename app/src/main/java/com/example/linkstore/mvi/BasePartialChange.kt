package com.example.linkstore.mvi

interface BasePartialChange<STATE> {

    fun reduce(oldState: STATE): STATE {
        return oldState
    }

}