package com.androsmith.proxime.data.model

sealed interface ConnectionState {
    object Connected: ConnectionState
    object Disconnected: ConnectionState
    object Uninitialized: ConnectionState
    object Initializing: ConnectionState
}