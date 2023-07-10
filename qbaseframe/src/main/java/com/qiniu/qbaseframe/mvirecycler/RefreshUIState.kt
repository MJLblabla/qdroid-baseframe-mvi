package com.qiniu.qbaseframe.mvirecycler

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.Exception

interface ListData<T> {
    var list: MutableList<T>
}

data class RefreshUIState<T>(var loadingState: LoadingState, val snapshot: T)

sealed class LoadingState {
    object Refreshing : LoadingState()
    object Loading : LoadingState()
    data class Idle(val sureNoMore: Boolean = false, val error: Throwable? = null) : LoadingState()
}

sealed class RecyclerItemEffect<T> {
    data class ItemChange<T>(val item: T) : RecyclerItemEffect<T>()
    data class ItemRemoved<T>(val item: T) : RecyclerItemEffect<T>()
    data class ItemInsert<T>(val from: Int, val count: Int, val list: MutableList<T>) :
        RecyclerItemEffect<T>()

    data class AddDate<T>(val list: MutableList<T>) : RecyclerItemEffect<T>()
}

fun LoadingState.isIdle(): Boolean {
    return this is LoadingState.Idle
}

fun <T> RefreshUIState<MutableList<T>>.checkNeedRefresh(block: () -> Unit) {
    if (this.loadingState.isIdle() && this.snapshot.isEmpty()
    ) {
        block.invoke()
    }
}

fun <T, R : ListData<T>> RefreshUIState<R>.checkNeedRefresh2(block: () -> Unit) {
    if (this.loadingState.isIdle() && this.snapshot.list.isEmpty()
    ) {
        block.invoke()
    }
}


suspend fun <T> refresh(
    isRefresh: Boolean,
    state: MutableStateFlow<RefreshUIState<MutableList<T>>>,
    adapterEffect: Channel<RecyclerItemEffect<T>>,
    block: suspend (isRefresh: Boolean) -> MutableList<T>
) {
    //  this.launch(dispatcher) {
    try {
        state.value = state.value.copy(
            loadingState = if (isRefresh) LoadingState.Refreshing else LoadingState.Loading
        )
        val ret = block.invoke(isRefresh)
        if (isRefresh) {
            state.value = state.value.copy(LoadingState.Idle(), ret)
        } else {
            val from = state.value.snapshot.size
            val to = ret.size
            state.value.snapshot.addAll(ret)
            adapterEffect.send(RecyclerItemEffect.ItemInsert(from, to, ret))
            state.value = state.value.copy(LoadingState.Idle())
        }
    } catch (e: Exception) {
        state.value = state.value.copy(loadingState = LoadingState.Idle(error = e))
        throw e
    } finally {

    }
}

suspend fun <T, R : ListData<T>> refresh2(
    isRefresh: Boolean,
    state: MutableStateFlow<RefreshUIState<R>>,
    adapterEffect: Channel<RecyclerItemEffect<T>>,
    block: suspend (isRefresh: Boolean) -> MutableList<T>
) {
    //  this.launch(dispatcher) {
    try {
        state.value = state.value.copy(
            loadingState = if (isRefresh) LoadingState.Refreshing else LoadingState.Loading
        )
        val ret = block.invoke(isRefresh)
        if (isRefresh) {

            val listData = state.value.snapshot
            listData.list = ret
            state.value = state.value.copy(LoadingState.Idle(), listData)

        } else {
            val listData = state.value.snapshot
            val from = state.value.snapshot.list.size
            val to = ret.size
            listData.list.addAll(ret)
            adapterEffect.send(RecyclerItemEffect.ItemInsert(from, to, ret))
            state.value = state.value.copy(LoadingState.Idle())
        }
    } catch (e: Exception) {
        state.value = state.value.copy(loadingState = LoadingState.Idle(error = e))
        throw e
    } finally {

    }
}


suspend fun <T> refresh(
    state: MutableStateFlow<RefreshUIState<T>>,
    block: suspend () -> T
) {
    try {
        state.value = state.value.copy(loadingState = LoadingState.Refreshing)
        val ret = block.invoke()
        state.value = state.value.copy(LoadingState.Idle(), ret)
    } catch (e: Exception) {
        state.value = state.value.copy(loadingState = LoadingState.Idle(error = e))
        throw e
    } finally {

    }
}

