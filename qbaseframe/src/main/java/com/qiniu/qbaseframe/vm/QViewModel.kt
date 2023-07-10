package com.qiniu.qbaseframe.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.annotation.StringRes
import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class InnerEffect {
    data class ShowLoading(val show: Boolean) : InnerEffect()
    data class ShowToast(val msg: String) : InnerEffect()
}

/**
 * @author manjiale
 */


open class QViewModel(application: Application) : AndroidViewModel(application) {

    // 页面状态变更的 “副作用”，类似一次性事件，不需要重放的状态变更（例如 Toast）
    private val _innerEffect = MutableSharedFlow<InnerEffect>()

    // 对外接口使用不可变版本
    val innerEffect = _innerEffect.asSharedFlow()

    /**
     * 获取activity fm
     */
    protected var attachedFragmentManagerCall: (() -> androidx.fragment.app.FragmentManager)? = null

    /**
     * 结束页面 回调
     */
    protected var finishedPageCall: (() -> Unit)? = null

    override fun onCleared() {
        super.onCleared()
        removeCall()
    }

    internal fun removeCall() {
        finishedPageCall = null
        attachedFragmentManagerCall = null
    }

    protected fun appContext(): Application {
        return this.getApplication<Application>()
    }

    protected fun showToast(@StringRes msgRes: Int) {
        showToast(appContext().getString(msgRes))
    }

    protected fun showToast(msg: String?) {
        if (!TextUtils.isEmpty(msg)) {
            viewModelScope.launch {
                _innerEffect.emit(InnerEffect.ShowToast(msg!!))
            }
        }
    }

    protected fun showLoading(boolean: Boolean) {
        viewModelScope.launch {
            _innerEffect.emit(InnerEffect.ShowLoading(boolean))
        }
    }

    internal fun initFinishedPageCall(finishedActivityCall: (() -> Unit)) {
        this.finishedPageCall = finishedActivityCall
    }

    internal fun initFragmentManagerCall(getFragmentManagerCall: (() -> androidx.fragment.app.FragmentManager)) {
        this.attachedFragmentManagerCall = getFragmentManagerCall
    }
}