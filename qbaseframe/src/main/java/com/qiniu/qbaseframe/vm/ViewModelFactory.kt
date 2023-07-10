package com.qiniu.qbaseframe.vm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch


fun QViewModel.attachOwner(activity: FragmentActivity) {

    this.initFinishedPageCall {
        activity.finish()
    }
    this.initFragmentManagerCall {
        activity.supportFragmentManager
    }
    activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            removeCall()
            super.onDestroy(owner)
        }
    })
    activity.lifecycleScope.launch {
        activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
            innerEffect.collect {
                when (it) {
                    is InnerEffect.ShowToast -> {
                        if (activity is ToastObserverView) {
                            activity.showToast(it.msg)
                        }
                    }

                    is InnerEffect.ShowLoading -> {
                        if (activity is LoadingObserverView) {
                            activity.showLoading(it.show)
                        }
                    }
                }
            }
        }
    }

}

fun QViewModel.attachOwner(fragment: Fragment) {

    this.initFinishedPageCall {
        fragment.requireActivity().finish()
    }

    this.initFragmentManagerCall {
        fragment.childFragmentManager
    }
    fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            removeCall()
            super.onDestroy(owner)
        }
    })

    fragment.lifecycleScope.launch {
        fragment.repeatOnLifecycle(Lifecycle.State.STARTED) {
            innerEffect.collect {
                when (it) {
                    is InnerEffect.ShowToast -> {
                        if (fragment is ToastObserverView) {
                            fragment.showToast(it.msg)
                        }
                    }

                    is InnerEffect.ShowLoading -> {
                        if (fragment is LoadingObserverView) {
                            fragment.showLoading(it.show)
                        }
                    }
                }
            }
        }
    }
}


