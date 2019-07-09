package com.gcml.module_health_manager.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gcml.module_health_manager.R
import com.gcml.module_health_manager.api.HealthManageService
import com.gcml.module_health_manager.bean.ConstractBean
import com.gzq.lib_core.base.Box
import com.gzq.lib_core.http.observer.CommonObserver
import com.gzq.lib_core.utils.RxUtils
import com.gzq.lib_core.utils.ToastUtils
import com.gzq.lib_resource.LazyFragment
import com.gzq.lib_resource.bean.ResidentBean
import com.gzq.lib_resource.bean.UserEntity
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_contracted_residen.view.*

private const val ARG_PARAM1 = "state"//签约类型
private const val ARG_PARAM2 = "param2"

class ContractNotResidenFragment : LazyFragment() {
    private var param1: String? = null
    private var param2: String? = null

    var residents = mutableListOf<ResidentBean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    var inflateView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contracted_residen, container, false)

        view?.rvResident?.apply {
            layoutManager = LinearLayoutManager(this@ContractNotResidenFragment.activity)

            adapter = object : BaseQuickAdapter<ResidentBean, BaseViewHolder>(R.layout.item_resident, residents) {
                override fun convert(helper: BaseViewHolder?, item: ResidentBean?) {
                    helper?.setText(R.id.tvName, item?.bname)
                    helper?.setText(R.id.tvSex, if (TextUtils.isEmpty(item?.sex)) "未填" else item?.sex)
                    Glide.with(activity!!)
                            .load(item?.userPhoto)
                            .into(helper?.getView<View>(R.id.ivHead) as ImageView)

                    helper?.getView<TextView>(R.id.tvSee).setOnClickListener {
                        ToastUtils.showLong(item?.bname)
                    }
                }
            }
        }


        view?.refresh?.apply {
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

    private val limit = 20
    private fun refresh() {
        page = 1
        Box.getRetrofit(HealthManageService::class.java)
                .getResidents(
                        Box.getSessionManager()?.getUser<UserEntity>()?.docterid!!,
                        0,
                        page,
                        limit
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.httpResponseTransformer())
                .doFinally {
                    view?.refresh?.apply { finishRefresh() }
                }
                .`as`(RxUtils.autoDisposeConverter(this))
                .subscribe(object : CommonObserver<ConstractBean>() {
                    override fun onNext(bean: ConstractBean) {
                        residents.clear()
                        residents.addAll(bean.data)

                        inflateView?.rvResident?.adapter?.notifyDataSetChanged()
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {

                    }
                })
    }

    var page = 1
    private fun loadMore() {
        page++
        Box.getRetrofit(HealthManageService::class.java)
                .getResidents(
                        Box.getSessionManager()?.getUser<UserEntity>()?.docterid!!,
                        0,
                        page,
                        limit
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.httpResponseTransformer())
                .doFinally {
                    view?.refresh?.apply { finishLoadMore() }
                }
                .`as`(RxUtils.autoDisposeConverter(this))
                .subscribe(object : CommonObserver<ConstractBean>() {
                    override fun onNext(bean: ConstractBean) {
                        residents.addAll(bean.data)

                        inflateView?.rvResident?.adapter?.notifyDataSetChanged()

                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {

                    }
                })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ContractNotResidenFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
