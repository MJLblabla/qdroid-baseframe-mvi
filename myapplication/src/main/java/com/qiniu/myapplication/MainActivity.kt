package com.qiniu.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qiniu.myapplication.databinding.ActivityMainBinding
import com.qiniu.qbaseframe.QBindingActivity
import com.qiniu.qbaseframe.adapter.QRecyclerViewBindAdapter
import com.qiniu.qbaseframe.adapter.QRecyclerViewBindHolder

class MainActivity : QBindingActivity<ActivityMainBinding>() {
    override fun initView() {
    }

    override fun showLoading(toShow: Boolean) {
    }

    class Ada : QRecyclerViewBindAdapter<String,ActivityMainBinding>(){
        override fun convertViewBindHolder(
            helper: QRecyclerViewBindHolder<ActivityMainBinding>,
            item: String
        ) {

        }

    }
}