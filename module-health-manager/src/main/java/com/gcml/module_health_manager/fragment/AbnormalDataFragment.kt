package com.gcml.module_health_manager.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gcml.module_health_manager.R
import com.gcml.module_health_manager.api.HealthManageService
import com.gcml.module_health_manager.bean.DetectionBean
import com.gzq.lib_core.base.Box
import com.gzq.lib_core.http.observer.CommonObserver
import com.gzq.lib_core.utils.RxUtils
import com.gzq.lib_core.utils.ToastUtils
import com.gzq.lib_resource.LazyFragment
import com.gzq.lib_resource.bean.UserEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_abnormal_data.view.*


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

                    var time: String = parseTime(item?.detectionTime)
                    helper?.setText(R.id.tvTime, time)

                    if (item?.lowPressure == 0) {
                        helper?.setText(R.id.textView2, "血糖（mmol/L）")
                        helper?.setText(R.id.tvPressrueValue, "" + item?.bloodSugar)
                    } else {
                        helper?.setText(R.id.textView2, "血压（mmHg）")
                        helper?.setText(R.id.tvPressrueValue, String.format("%s/%s", item?.lowPressure, item?.highPressure))
                    }

                   /* helper?.getView<TextView>(R.id.tvConfirm)?.setOnClickListener {
                        ToastUtils.showLong("")
                    }*/
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
                .subscribe(object : CommonObserver<List<DetectionBean>>() {
                    override fun onNext(list: List<DetectionBean>) {
                        abnormalItems.addAll(list)

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
                .subscribe(object : CommonObserver<List<DetectionBean>>() {
                    override fun onNext(list: List<DetectionBean>) {
                        abnormalItems.addAll(list)

                        inflateView?.rvItems?.adapter?.notifyDataSetChanged()

                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {

                    }
                })
    }


    private fun parseTime(detectionTime: Long?): String {
        return "昨天"
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AbnormalDataFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
