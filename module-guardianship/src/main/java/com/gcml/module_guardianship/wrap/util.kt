package com.gcml.module_guardianship.wrap

import android.content.Context
import com.gzq.lib_core.utils.ToastUtils

fun Context.showToast(text: String?) {
    text?.apply {
        ToastUtils.showLong(text)
    }
}