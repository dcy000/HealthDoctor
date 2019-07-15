package com.gcml.module_health_manager.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gcml.module_health_manager.R
import com.gcml.module_health_manager.api.HealthManageService
import com.gcml.module_health_manager.bean.Menu
import com.gzq.lib_core.base.Box
import com.gzq.lib_core.http.exception.ApiException
import com.gzq.lib_core.http.observer.CommonObserver
import com.gzq.lib_core.utils.RxUtils
import com.gzq.lib_core.utils.ToastUtils
import com.gzq.lib_resource.bean.ResidentBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_deal_sign.*

class DealSignActivity : AppCompatActivity() {
    var userInfo: ResidentBean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_deal_sign)
        ivBack.setOnClickListener { finish() }

        userInfo = intent?.getParcelableExtra<ResidentBean>("userInfo")

        Glide.with(Box.getApp())
                .load(userInfo?.userPhoto)
                .into(ivHead)

        tvName.text = userInfo?.bname
        tvSexValue.text = "性别  " + userInfo?.sex
        tvAgeValue.text = "年龄  " + userInfo?.age

        var showButton: Boolean? = intent?.getBooleanExtra("showButton", false)
        when (showButton) {
            true -> {
                lldDealButton.visibility = View.VISIBLE
            }
            else -> {
                lldDealButton.visibility = View.GONE
            }
        }

        var menus = listOf(
                Menu("手机号", userInfo?.tel ?: "暂未填写"),
                Menu("血型", userInfo?.bloodType ?: "暂未填写"),
                Menu("身高", "" + userInfo?.height),
                Menu("体重", "" + userInfo?.weight))

        rvMenu?.apply {
            layoutManager = LinearLayoutManager(this@DealSignActivity)

            adapter = object : BaseQuickAdapter<Menu, BaseViewHolder>(R.layout.item_user_info, menus) {
                override fun convert(helper: BaseViewHolder?, item: Menu?) {
                    when (item?.name) {
                        "手机号" -> {
                            helper?.setText(R.id.tv_title, item?.name)
                            helper?.setText(R.id.tv_content, item?.value)
                        }

                        "血型" -> {
                            helper?.setText(R.id.tv_title, item?.name)
                            helper?.setText(R.id.tv_content, item?.value)
                        }
                        "身高" -> {
                            helper?.setText(R.id.tv_title, item?.name)
                            helper?.setText(R.id.tv_content, item?.value)
                        }
                        "体重" -> {
                            helper?.setText(R.id.tv_title, item?.name)
                            helper?.setText(R.id.tv_content, item?.value)
                        }
                    }
                }
            }

        }
        tvRefuse.setOnClickListener { updataQianyue("0") }
        tvAgree.setOnClickListener { updataQianyue("1") }
    }

    private fun updataQianyue(state: String) {

        var doctorSign = when (state) {
            "0" -> {
                null
            }
            else -> {
                "123"
            }
        }
        Box.getRetrofit(HealthManageService::class.java)?.updateQinaYue(
                userInfo?.bid!!,
                doctorSign,
                state)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.compose(RxUtils.httpResponseTransformer())
                ?.doFinally {

                }
                ?.`as`(RxUtils.autoDisposeConverter(this))
                ?.subscribe(object : CommonObserver<Any>() {
                    override fun onNext(t: Any) {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onError(e: Throwable) {
                        if (e is ApiException) {
                            if (e.code == 600) {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }
                    }

                    override fun onComplete() {

                    }
                })


    }
}
