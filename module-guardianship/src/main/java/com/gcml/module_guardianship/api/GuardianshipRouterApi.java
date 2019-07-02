package com.gcml.module_guardianship.api;

import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gcml.module_guardianship.bean.WatchInformationBean;
import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.sjtu.yifei.annotation.Extra;
import com.sjtu.yifei.annotation.Go;

import java.util.List;

public interface GuardianshipRouterApi {
    @Go("/guardianship/search/family")
    boolean skipSearchFamilyActivity();

    @Go("/guardianship/qrcode/scan")
    boolean skipQrCodeScanActivity();

    @Go("/guardianship/add/resident/information")
    boolean skipAddResidentInformationActivity();

    @Go("/guardianship/add/relationship")
    boolean skipAddRelationshipActivity(@Extra("watchInfo") WatchInformationBean watchInformationBean);

    @Go("/guardianship/resident/detail")
    boolean skipResidentDetailActivity(@Extra("data") ResidentBean guardianshipBean,@Extra("vip") int vip);

    @Go("/guardianship/resident/location/detail")
    boolean skipResidentLocationDetailActivity(@Extra("data") ResidentBean guardianshipBean);

    @Go("/health/manager/report")
    boolean skipHealthManagerReportActivity();

    @Go("/warning/information/record")
    boolean skipWarningInformationRecordActivity();

    @Go("/person/detail/")
    boolean skipPersonDetailActivity(@Extra("data") ResidentBean guardianshipBean);

    @Go("/custody/circle/me")
    boolean skipCustodyCircleActivity(@Extra("data") ResidentBean guardianshipBean);

    @Go("/add/custody/activity")
    boolean skipAddCustodyActivity(@Extra("userId") String userId);

    @Go("/fragment/vip/resident")
    StateBaseFragment getVipResidentFragment();

    @Go("/fragment/normal/resident")
    StateBaseFragment getNormalResidentFragment();
    @Go("/guardianship/remote/measure/activity")
    boolean skipRemoteMeasureActivity(@Extra("watchCode") String watchCode, @Extra("userId") String userId);

    @Go("/health/record/detection")
    boolean skipDetectionRecordTypeActivity(@Extra("data") ResidentBean guardianshipBean);
}
