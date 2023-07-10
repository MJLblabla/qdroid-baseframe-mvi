package com.qiniu.qbaseframe.mvirecycler

import com.qiniu.qbaseframe.adapter.QBaseLoadMoreModule
import com.qiniu.qbaseframe.adapter.QRecyclerAdapter

fun <T> QRecyclerAdapter<T>.attachEffect(effect: RecyclerItemEffect<T>) {
    when (effect) {
        is RecyclerItemEffect.ItemChange<T> -> {
            val position = data.indexOf(effect.item)
            notifyItemChanged(position)
        }

        is RecyclerItemEffect.ItemRemoved<T> -> {
            val position = data.indexOf(effect.item)
            notifyItemRemoved(position)
        }

        is RecyclerItemEffect.ItemInsert<T> -> {
            notifyItemRangeInserted(effect.from, effect.count)
            if (effect.count == 0) {
                this.selfGetLoadMoreModule()?.loadMoreEnd()
            } else {
                this.selfGetLoadMoreModule()?.loadMoreComplete()
            }
            this.selfGetLoadMoreModule()?.isEnableLoadMore = true
        }

        is RecyclerItemEffect.AddDate<T> -> {
            addData(effect.list)
            if (effect.list.size == 0) {
                this.selfGetLoadMoreModule()?.loadMoreEnd()
            } else {
                this.selfGetLoadMoreModule()?.loadMoreComplete()
            }
            this.selfGetLoadMoreModule()?.isEnableLoadMore = true
        }
    }
}

fun <T> QRecyclerAdapter<T>.setRefreshUIState(state: RefreshUIState<MutableList<T>>) {
    setNewInstance(state.snapshot)
    setRefreshUIState(state.loadingState)
}

fun <T> QRecyclerAdapter<T>.setRefreshUIState(state: LoadingState, snapshot: MutableList<T>) {
    setNewInstance(snapshot)
    setRefreshUIState(state)
}

fun <T> QRecyclerAdapter<T>.setRefreshUIState2(state: RefreshUIState<ListData<T>>) {
    setNewInstance(state.snapshot.list)
    setRefreshUIState(state.loadingState)
}

private fun <T> QRecyclerAdapter<T>.setRefreshUIState(loadingState: LoadingState) {
    if (loadingState is LoadingState.Idle) {
        this.selfGetUpFetchModule()?.isUpFetchEnable = true
        this.selfGetLoadMoreModule()?.isEnableLoadMore = true

        if (this.selfGetLoadMoreModule()?.isLoading == true) {
            if ((loadingState).error == null) {
                this.selfGetLoadMoreModule()?.loadMoreEnd()
            }
        }
        if (this.selfGetUpFetchModule()?.isUpFetching == true) {
            this.selfGetUpFetchModule()?.isUpFetching = false
        }
    }

    if (loadingState is LoadingState.Idle && selfGetUpFetchModule()?.isUpFetching == true) {
        selfGetUpFetchModule()?.isUpFetching = false
    }

    if (loadingState is LoadingState.Refreshing) {
        this.selfGetLoadMoreModule()?.isEnableLoadMore = false
    }

    if (loadingState is LoadingState.Loading) {
        this.selfGetUpFetchModule()?.isUpFetchEnable = false
        if (this.selfGetLoadMoreModule()?.isLoading != true) {
            if (this.selfGetLoadMoreModule() is QBaseLoadMoreModule?) {
                (this.selfGetLoadMoreModule() as QBaseLoadMoreModule?)?.reLoading()
            }
        }
    }
}