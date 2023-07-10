package com.qiniu.qbaseframe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.qiniu.qbaseframe.vm.LoadingObserverView
import java.lang.reflect.ParameterizedType
import java.util.Objects

abstract class QBindingFragment<T : ViewBinding> : Fragment(), LoadingObserverView {

    protected lateinit var binding: T
    private fun createContainerView(container: ViewGroup?): View {
        var sup = this.javaClass.genericSuperclass
        if (sup !is ParameterizedType) {
            sup = Objects.requireNonNull(this.javaClass.superclass).genericSuperclass
        }
        assert(sup != null)
        binding =
            com.qiniu.qbaseframe.ViewBindingExt.create(sup as ParameterizedType, container, requireContext(), false)
        return binding.root
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return createContainerView(container)
    }

}