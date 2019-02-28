package com.gcml.module_guardianship.presenter;

import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.mvp.base.IView;

import java.util.List;

public interface IGuardianshipView extends IView {
    void loadVipSuccess(List<ResidentBean> residentBeans);

    void loadNorSuccess(List<ResidentBean> residentBeans);
}
