package com.gcml.module_health_manager.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gcml.module_health_manager.R
import com.gcml.module_health_manager.api.HealthManagerRouterApi
import com.sjtu.yifei.annotation.Route
import com.sjtu.yifei.route.Routerfit
import kotlinx.android.synthetic.main.fragment_business.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@Route(path = "/healthmanager/main")
class BusinessFragment : Fragment(), View.OnClickListener {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    var viewInflater: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewInflater = inflater.inflate(R.layout.fragment_business, container, false)
        initEvent(viewInflater)
        return viewInflater
    }

    private fun initEvent(view: View?) {
        view?.llAbnormalData?.setOnClickListener(this)
        view?.llSignDoctor?.setOnClickListener(this)
        view?.llHealthManage?.setOnClickListener(this)
        view?.llContractmanage?.setOnClickListener(this)
        view?.llFllowUpManage?.setOnClickListener(this)
        view?.llMedicalLterature?.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                BusinessFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onClick(v: View?) {
        when (v) {
            viewInflater?.llAbnormalData -> {

            }

            viewInflater?.llSignDoctor -> {
                Routerfit.register(HealthManagerRouterApi::class.java).skipFamilyDoctorServiceActivity()
            }

            viewInflater?.llHealthManage -> {
                Routerfit.register(HealthManagerRouterApi::class.java).skipHealthMeasureActivity()
            }

            viewInflater?.llContractmanage -> {

            }

            viewInflater?.llFllowUpManage -> {
            }

            viewInflater?.llMedicalLterature -> {
                Routerfit.register(HealthManagerRouterApi::class.java).skipMedicalLiteratureActivity()
            }
        }

    }
}
