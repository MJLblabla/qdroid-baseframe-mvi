package  com.qiniu.qbaseframe.adapter

import android.view.View
import androidx.viewbinding.ViewBinding

open class QRecyclerViewBindHolder<T : ViewBinding>(var binding: T, view: View) :
    QRecyclerViewHolder(view)