package com.gcml.module_guardianship

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gcml.module_guardianship.api.GuardianshipApi
import com.gcml.module_guardianship.bean.UserTypeBean
import com.gzq.lib_core.base.Box
import com.gzq.lib_core.utils.RxUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DefaultObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_modify_health_status.*
import android.widget.Toast
import com.gcml.module_guardianship.bean.UpdateHealthStatusBean
import com.gzq.lib_core.utils.ToastUtils


class ModifyHealthStatusActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_health_status)
        initView()
        initRV()
    }

    val professionItems = mutableListOf<UserTypeBean>()
    val slowItems = mutableListOf<UserTypeBean>()
    private fun initView() {

        tvNormal?.setOnClickListener {
            it.isSelected = true

            slowItems?.forEach { it?.select = false }
            professionItems?.forEach { it.select = false }

            rvSlow.adapter?.notifyDataSetChanged()
            rvProfession.adapter?.notifyDataSetChanged()
        }

        ivBack?.setOnClickListener {
            finish()
        }

        tvConfirm.setOnClickListener {
            var data = UpdateHealthStatusBean()
            data.bid = intent.getIntExtra("userId", 0)
            data.userType = ""
            when {
                tvNormal?.isSelected == true -> data.userType = "正常居民"
                else -> {
                    professionItems?.filter { it.select }?.forEach { data.userType = data.userType + it.text + "," }
                    slowItems?.filter { it.select }?.forEach { data.userType = data.userType + it.text + "," }
                }
            }

            Box.getRetrofit(GuardianshipApi::class.java)
                    .updateOneUser(data)
                    .compose(RxUtils.httpResponseTransformer())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : DefaultObserver<Any?>() {
                        override fun onNext(t: Any) {
                            finish()
                        }

                        override fun onError(e: Throwable) {
                            ToastUtils.showLong(e.message)
                        }

                        override fun onComplete() {

                        }
                    })
        }

    }

    private fun initRV() {
        Box.getRetrofit(GuardianshipApi::class.java)
                .getItemByCode("professional_user_type")
                .compose(RxUtils.httpResponseTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DefaultObserver<List<UserTypeBean>>() {
                    override fun onNext(list: List<UserTypeBean>) {
                        professionItems.clear()
                        professionItems.addAll(list)

                        rvProfession?.run {
                            layoutManager = GridLayoutManager(this@ModifyHealthStatusActivity, 3)
                            adapter = object : BaseQuickAdapter<UserTypeBean, BaseViewHolder>(R.layout.item_user_type, professionItems) {
                                override fun convert(helper: BaseViewHolder?, item: UserTypeBean?) {
                                    helper?.getView<TextView>(R.id.tvItemName)?.isSelected = item?.select!!
                                    helper?.setText(R.id.tvItemName, item?.text)
                                }
                            }
                        }

                        (rvProfession?.adapter as BaseQuickAdapter<*, *>).onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, _, position ->
                            professionItems[position].select = !professionItems[position].select
                            adapter?.notifyItemChanged(position)

                            tvNormal?.isSelected = false
                        }
                    }

                    override fun onError(e: Throwable) {
                        ToastUtils.showLong(e.message)
                    }


                    override fun onComplete() {

                    }
                })

        Box.getRetrofit(GuardianshipApi::class.java)
                .getItemByCode("chronic_user_type")
                .compose(RxUtils.httpResponseTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DefaultObserver<List<UserTypeBean>>() {
                    override fun onNext(list: List<UserTypeBean>) {
                        slowItems.clear()
                        slowItems.addAll(list)

                        rvSlow?.run {
                            layoutManager = GridLayoutManager(this@ModifyHealthStatusActivity, 3)
                            adapter = object : BaseQuickAdapter<UserTypeBean, BaseViewHolder>(R.layout.item_user_type, slowItems) {
                                override fun convert(helper: BaseViewHolder?, item: UserTypeBean?) {
                                    helper?.getView<TextView>(R.id.tvItemName)?.isSelected = item?.select!!
                                    helper?.setText(R.id.tvItemName, item?.text)
                                }
                            }
                        }

                        (rvSlow?.adapter as BaseQuickAdapter<*, *>).onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, _, position ->
                            slowItems[position].select = !slowItems[position].select
                            adapter?.notifyItemChanged(position)

                            tvNormal?.isSelected = false
                        }
                    }

                    override fun onError(e: Throwable) {
                        ToastUtils.showLong(e.message)
                    }


                    override fun onComplete() {

                    }
                })

    }


}
