package com.gcml.module_health_manager.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gcml.module_health_manager.R
import com.gcml.module_health_manager.api.HealthManageService
import com.gcml.module_health_manager.bean.AbNormalPageBean
import com.gcml.module_health_manager.bean.DetectionBean
import com.gzq.lib_core.base.Box
import com.gzq.lib_core.http.observer.CommonObserver
import com.gzq.lib_core.utils.RxUtils
import com.gzq.lib_resource.LazyFragment
import com.gzq.lib_resource.bean.UserEntity
import com.gzq.lib_resource.utils.data.TimeUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_abnormal_data.view.*
import java.text.SimpleDateFormat


private const val ARG_PARAM1 = "param1States"
private const val ARG_PARAM2 = "param2"

class AbnormalDataFragment : LazyFragment() {
    private var states: Int? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            states = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    var inflateView: View? = null
    var abnormalItems = mutableListOf<DetectionBean>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_abnormal_data, container, false)

        view?.rvItems?.apply {
            layoutManager = LinearLayoutManager(this@AbnormalDataFragment.activity)

            adapter = object : BaseQuickAdapter<DetectionBean, BaseViewHolder>(R.layout.item_abnomal, abnormalItems) {

                override fun convert(helper: BaseViewHolder?, item: DetectionBean?) {
                    helper?.setText(R.id.tvName, item?.userName)

                    helper?.setText(R.id.tvTime, when (states) {
                        0 -> {
                            parseTime(item?.detectionTime)
                        }
                        else -> {
                            parseTime(item?.verifyModify)
                        }
                    })

                    if (item?.lowPressure == 0) {
                        helper?.setText(R.id.textView2, "血糖（mmol/L）")
                        helper?.setText(R.id.tvPressrueValue, "" + item?.bloodSugar)
                    } else {
                        helper?.setText(R.id.textView2, "血压（mmHg）")
                        helper?.setText(R.id.tvPressrueValue, String.format("%s/%s", item?.lowPressure, item?.highPressure))
                    }

                    when (states) {
                        0 -> {
                            helper?.getView<TextView>(R.id.tvConfirm)?.isEnabled = true
                            helper?.getView<TextView>(R.id.tvConfirm)?.setOnClickListener {
                                update(item, helper)
                            }
                        }
                        1 -> {
                            helper?.getView<TextView>(R.id.tvConfirm)?.isEnabled = false
                        }
                    }

                }
            }

        }


        view?.srefresh?.apply {
            setEnableRefresh(true)
            setEnableLoadMore(true)

            setOnLoadMoreListener {
                loadMore()
            }

            setOnRefreshListener {
                refresh()
            }

        }
        inflateView = view
        return view
    }

    private fun RecyclerView.update(item: DetectionBean?, helper: BaseViewHolder) {
        item?.dataAnomalyId?.apply {
            Box.getRetrofit(HealthManageService::class.java)
                    .updateAnomalyId(this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(RxUtils.httpResponseTransformer())
                    .doFinally {

                    }
                    .`as`(RxUtils.autoDisposeConverter(this@AbnormalDataFragment))
                    .subscribe(object : CommonObserver<Any>() {
                        override fun onNext(list: Any) {
                           /* abnormalItems.removeAt(helper.layoutPosition)
                            adapter?.notifyItemChanged(helper.layoutPosition)*/

                            refresh()
                        }

                        override fun onError(e: Throwable) {
                        }

                        override fun onComplete() {

                        }
                    })
        }
    }

    private fun updateAnomalyId(id: String?) {
        Box.getRetrofit(HealthManageService::class.java)
                .updateAnomalyId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.httpResponseTransformer())
                .doFinally {

                }
                .`as`(RxUtils.autoDisposeConverter(this))
                .subscribe(object : CommonObserver<Any>() {
                    override fun onNext(list: Any) {
                        refresh()
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {

                    }
                })
    }

    override fun onPageResume() {
        arguments?.let {
            states = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        refresh()
    }

    private fun refresh() {
        page = 1
        Box.getRetrofit(HealthManageService::class.java)
                .getAbNomalDatas(
                        Box.getSessionManager()?.getUser<UserEntity>()?.docterid!!,
                        states ?: 0,
                        page,
                        limit
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.httpResponseTransformer())
                .doFinally {
                    view?.srefresh?.apply { finishRefresh() }
                }
                .`as`(RxUtils.autoDisposeConverter(this))
                .subscribe(object : CommonObserver<AbNormalPageBean>() {
                    override fun onNext(data: AbNormalPageBean) {
                        abnormalItems.clear()
                        abnormalItems.addAll(data?.list)

                        inflateView?.rvItems?.adapter?.notifyDataSetChanged()

                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {

                    }
                })
    }

    private var page = 1
    private val limit = 20
    private fun loadMore() {
        page++
        Box.getRetrofit(HealthManageService::class.java)
                .getAbNomalDatas(
                        Box.getSessionManager()?.getUser<UserEntity>()?.docterid!!,
                        states ?: 0,
                        page,
                        limit
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.httpResponseTransformer())
                .doFinally {
                    view?.srefresh?.apply { finishLoadMore() }
                }
                .`as`(RxUtils.autoDisposeConverter(this))
                .subscribe(object : CommonObserver<AbNormalPageBean>() {
                    override fun onNext(list: AbNormalPageBean) {
                        abnormalItems.addAll(list?.list)

                        inflateView?.rvItems?.adapter?.notifyDataSetChanged()

                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {

                    }
                })
    }


    private fun parseTime(detectionTime: Long?): String {
        return detectionTime?.let {
            TimeUtils.milliseconds2String(detectionTime, SimpleDateFormat("yyyy.MM.dd HH:mm"))
        } ?: ""
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
                AbnormalDataFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
