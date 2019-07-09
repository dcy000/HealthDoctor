package com.gzq.lib_resource;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleRegistry;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public class LazyFragment extends Fragment implements LifecycleObserver {
    public static final String TAG = "LazyFragment";

    private boolean isPageResume;

    public boolean isPageResume() {
        return isPageResume;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Lifecycle lifecycle = getLifecycle();
        if (lifecycle instanceof LifecycleRegistry) {
            lifecycle.addObserver(this);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getView() != null && !isPageResume && !isHidden() && getUserVisibleHint()) {
            isPageResume = true;
            getView().post(new Runnable() {
                @Override
                public void run() {
                    onPageResume();
                }
            });
        }
    }

    @Override
    public void onResume() {
        if (getView() != null && !isPageResume && !isHidden() && getUserVisibleHint()) {
            isPageResume = true;
            onPageResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (isPageResume) {
            isPageResume = false;
            onPagePause();
        }
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            if (isPageResume && getUserVisibleHint()) {
                isPageResume = false;

                handleEvent(Lifecycle.Event.ON_PAUSE);

                onPagePause();
            }
        } else {
            if (getView() != null && !isPageResume && getUserVisibleHint()) {
                isPageResume = true;

                handleEvent(Lifecycle.Event.ON_RESUME);

                onPageResume();
            }
        }
        super.onHiddenChanged(hidden);
    }

    private void handleEvent(Lifecycle.Event event) {
        Lifecycle lifecycle = getLifecycle();
        if (lifecycle instanceof LifecycleRegistry) {
            ((LifecycleRegistry) lifecycle).handleLifecycleEvent(event);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (getView() != null && !isPageResume && !isHidden()) {
                isPageResume = true;

                handleEvent(Lifecycle.Event.ON_RESUME);

                onPageResume();
            }
        } else {
            if (isPageResume && !isHidden()) {
                isPageResume = false;

                handleEvent(Lifecycle.Event.ON_PAUSE);

                onPagePause();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    protected void onPageResume() {

    }

    protected void onPagePause() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Lifecycle lifecycle = getLifecycle();
        if (lifecycle instanceof LifecycleRegistry) {
            lifecycle.removeObserver(this);
        }
    }
}
