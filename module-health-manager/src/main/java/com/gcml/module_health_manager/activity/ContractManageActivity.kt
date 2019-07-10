package com.gcml.module_health_manager.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.gcml.module_health_manager.R
import com.gcml.module_health_manager.fragment.ContractNotResidenFragment
import com.gcml.module_health_manager.fragment.ContractedResidenFragment
import kotlinx.android.synthetic.main.activity_contract_manage.*
import kotlinx.android.synthetic.main.layout_common_toobar.*
import java.util.ArrayList

class ContractManageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contract_manage)
        initfragments()
        initView()
    }

    var fragments = ArrayList<Fragment>()
    private fun initfragments() {
        fragments.add(ContractNotResidenFragment.newInstance("", ""))
        fragments.add(ContractedResidenFragment.newInstance("", ""))
    }

    private fun initView() {
        llLeft?.setOnClickListener { finish() }
        tvTitle.text = "签约管理"

        tabLayout.setViewPager(vpConstractResident, arrayOf("待签约", "已签约"), this, fragments)
    }
}
