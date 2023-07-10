package com.qiniu.qbaseframe.mvirecycler

import com.qiniu.qbaseframe.refresh.QRefreshLayout

fun QRefreshLayout.setRefreshUIState(state: LoadingState) {
    when (state) {
        is LoadingState.Loading -> {
            if (loadMoreModule?.isLoading != true) {
                loadMoreModule?.setLoading()
            }
            refreshModule?.isReFreshEnable = false
        }

        is LoadingState.Refreshing -> {
            if (refreshModule?.isRefreshing != true) {
                refreshModule?.setRefreshing()
            }
            loadMoreModule?.isLoadMoreEnable = false
        }

        is LoadingState.Idle -> {
            refreshModule?.isReFreshEnable = true
            loadMoreModule?.isLoadMoreEnable = true

            if (loadMoreModule?.isLoading == true) {
                loadMoreModule?.finishLoadMore(state.sureNoMore)
            }
            if (refreshModule?.isRefreshing == true) {
                refreshModule?.finishRefresh()
            }
        }
    }
}

