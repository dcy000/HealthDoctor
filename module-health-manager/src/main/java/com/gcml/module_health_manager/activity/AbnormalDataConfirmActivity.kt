package com.gcml.module_health_manager.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.gcml.module_health_manager.R
import com.gcml.module_health_manager.fragment.AbnormalDataFragment
import com.githang.statusbar.StatusBarCompat
import com.gzq.lib_core.base.Box
import kotlinx.android.synthetic.main.activity_contract_manage.*
import kotlinx.android.synthetic.main.layout_common_toobar.*
import java.util.*

class AbnormalDataConfirmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abnormal_data)
        initfragments()
        initView()
        StatusBarCompat.setStatusBarColor(this, Box.getColor(com.gzq.lib_resource.R.color.white))
    }

    var fragments = ArrayList<Fragment>()
    private fun initfragments() {
        fragments?.add(AbnormalDataFragment.newInstance("0", ""))
        fragments?.add(AbnormalDataFragment.newInstance("1", ""))
    }

    private fun initView() {
        llLeft?.setOnClickListener { finish() }
        tvTitle.text = "异常数据确认"

        tabLayout.setViewPager(vpConstractResident, arrayOf("待确认", "已确认"), this, fragments)
    }
}
