package com.gcml.call;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.gcml.call.floatwindow.CallFloatViewHelper;


import com.gzq.lib_core.utils.ToastUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand;
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatControlEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNotifyOption;
import com.netease.nimlib.sdk.avchat.model.AVChatOnlineAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;


/**
 * Created by afirez on 2018/6/1.
 */

public enum CallHelper {

    @SuppressLint("StaticFieldLeak")
    INSTANCE;

    public static void enterFullScreen(final Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, CallActivity.class);
        context.startActivity(intent);
    }

    public static void outgoingCall(final Context context, final String account) {
        outgoingCall(context, account, AVChatType.VIDEO.getValue(), SOURCE_INTERNAL);
    }

    public static void outgoingCall(Context context, String account, int callType, int source) {
        CallHelper.INSTANCE.setChatting(true);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, CallActivity.class);
        intent.putExtra(EXTRA_REMOTE_ACCOUNT, account);
        intent.putExtra(EXTRA_INCOMING_CALL, false);
        intent.putExtra(EXTRA_CALL_TYPE, callType);
        intent.putExtra(EXTRA_SOURCE, source);
        context.startActivity(intent);
    }

    public static void incomingCall(Context context, AVChatData config, int source) {
        CallHelper.INSTANCE.setChatting(true);
        Intent intent = new Intent();
        intent.setClass(context, CallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_CALL_CONFIG, config);
        intent.putExtra(EXTRA_INCOMING_CALL, true);
        intent.putExtra(EXTRA_SOURCE, source);
        context.startActivity(intent);
    }

    public static final String EXTRA_SOURCE = "extra_source";
    public static final String EXTRA_INCOMING_CALL = "extra_incoming_call";
    public static final String EXTRA_CALL_CONFIG = "extra_call_config";
    public static final String EXTRA_CALL_TYPE = "extra_call_type";
    public static final String EXTRA_REMOTE_ACCOUNT = "extra_peer_account";
    public static final int SOURCE_UNKNOWN = -1;
    public static final int SOURCE_BROADCAST = 0;
    public static final int SOURCE_INTERNAL = 1;


    CallHelper() {
        context = CallApp.INSTANCE.getApp();
    }


    public void setChatting(boolean chatting) {
        mChatting = chatting;
    }

    public boolean isChatting() {
        return mChatting;
    }

    public boolean isCallEstablished() {
        return mCallEstablished.get();
    }

    public interface OnCallStateChangeListener {
        void onCallStateChanged(CallState state);
    }

    private List<OnCallStateChangeListener> mOnCallStateChangeListeners =
            new ArrayList<>(2);

    public void addOnCallStateChangeListener(OnCallStateChangeListener listener) {
        mOnCallStateChangeListeners.add(listener);
        if (callingState != null) {
            notifyCallStateChanged(callingState);
        }
    }

    public void removeOnCallStateChangeListener(OnCallStateChangeListener listener) {
        mOnCallStateChangeListeners.remove(listener);
    }

    public void notifyCallStateChanged(CallState state) {
        Timber.i("notifyCallStateChanged state=%s", state);
        callingState = state;
        for (OnCallStateChangeListener listener : mOnCallStateChangeListeners) {
            if (listener != null) {
                listener.onCallStateChanged(state);
            }
        }
    }

    public interface OnCloseSessionListener {
        void onCloseSession();
    }

    private OnCloseSessionListener mOnCloseSessionListener;

    public void setOnCloseSessionListener(OnCloseSessionListener onCloseSessionListener) {
        mOnCloseSessionListener = onCloseSessionListener;
    }

    private Context context;

    private boolean mChatting;
    private boolean isIncomingCall;
    private CallState callingState;
    private AVChatData avChatData;
    private int callType;
    private String remoteAccount;
    private AtomicBoolean mCallEstablished = new AtomicBoolean(false);

    private AVChatCameraCapturer videoCapturer;

    private volatile FrameLayout flSmallContainer;
    private volatile FrameLayout flLargeContainer;

    private String largeAccount;
    private String smallAccount;

    private AVChatSurfaceViewRenderer smallRenderer;
    private AVChatSurfaceViewRenderer largeRenderer;

    private boolean isRemoteVideoOff;

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public FrameLayout getFlSmallContainer() {
        return flSmallContainer;
    }

    public synchronized void setSmallContainer(FrameLayout flSmallContainer) {
        this.flSmallContainer = flSmallContainer;
        if (flSmallContainer == null
                && mCallEstablished.get()
                && smallRenderer != null) {
            ViewParent parent = smallRenderer.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(smallRenderer);
            }
            return;
        }
        smallRenderer = null;
        initSmallRenderer(smallAccount);
    }

    public FrameLayout getFlLargeContainer() {
        return flLargeContainer;
    }

    public synchronized void setLargeContainer(FrameLayout flLargeContainer) {
        this.flLargeContainer = flLargeContainer;
        if (flLargeContainer == null
                && mCallEstablished.get()
                && largeRenderer != null) {
            ViewParent parent = largeRenderer.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(largeRenderer);
            }
            return;
        }
        largeRenderer = null;
        initLargeRenderer(largeAccount);
    }

    public void setIncomingCall(boolean incomingCall) {
        isIncomingCall = incomingCall;
    }

    public boolean isIncomingCall() {
        return isIncomingCall;
    }

    public String getRemoteAccount() {
        return remoteAccount;
    }

    public void parse(Bundle bundle) {
        isIncomingCall = bundle.getBoolean(EXTRA_INCOMING_CALL, false);
        int source = bundle.getInt(EXTRA_SOURCE, SOURCE_UNKNOWN);
        switch (source) {
            case SOURCE_BROADCAST: // incoming call
                avChatData = (AVChatData) bundle.getSerializable(EXTRA_CALL_CONFIG);
                if (avChatData != null) {
                    callType = avChatData.getChatType().getValue();
                    registerCallObserver(true);
                    uiHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onInComingCalling(avChatData);
                        }
                    }, 200);
                }
                break;
            case SOURCE_INTERNAL: // outgoing call
                remoteAccount = bundle.getString(EXTRA_REMOTE_ACCOUNT);
                callType = bundle.getInt(EXTRA_CALL_TYPE, -1);
                final AVChatType chatType = callType == AVChatType.VIDEO.getValue()
                        ? AVChatType.VIDEO
                        : AVChatType.AUDIO;
                registerCallObserver(true);
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        call(remoteAccount, chatType);
                    }
                }, 200);
                break;
            default:
                notifyCallStateChanged(callingState);
                break;
        }
    }

    public void registerCallObserver(boolean register) {
        AVChatManager.getInstance().observeAVChatState(callStateObserver, register);
        AVChatManager.getInstance().observeCalleeAckNotification(callAckObserver, register);
        AVChatManager.getInstance().observeControlNotification(callControlObserver, register);
        AVChatManager.getInstance().observeHangUpNotification(callHangupObserver, register);
        AVChatManager.getInstance().observeOnlineAckNotification(onlineAckObserver, register);
        CallTimeoutObserver.INSTANCE.observeTimeoutNotification(timeoutObserver, register, isIncomingCall);
        CallPhoneStateObserver.INSTANCE.observeAutoHangUpForLocalPhone(autoHangUpForLocalPhoneObserver, register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
}


    private AVChatStateObserver callStateObserver = new CallChatStateObserver() {

        @Override
        public void onJoinedChannel(int code, String audioFile, String videoFile, int elapsed) {
            if (code == 200) {
            } else if (code == 101) { // 连接超时
                closeSessions(CallExitCode.PEER_NO_RESPONSE);
            } else if (code == 401) { // 验证失败
                closeSessions(CallExitCode.CONFIG_ERROR);
            } else if (code == 417) { // 无效的channelId
                closeSessions(CallExitCode.INVALIDE_CHANNELID);
            } else { // 连接服务器错误，直接退出
                closeSessions(CallExitCode.CONFIG_ERROR);
            }
        }

        @Override
        public void onUserJoined(String account) {
            initLargeRenderer(account);
        }

        @Override
        public void onUserLeave(String account, int event) {
            hangUp();
        }

        @Override
        public void onCallEstablished() {
            CallHelper.this.onCallEstablished();
        }
    };

    private void initLargeRenderer(String account) {
        if (flLargeContainer == null || TextUtils.isEmpty(account)) {
            return;
        }
        largeAccount = account;
        AVChatManager chatManager = AVChatManager.getInstance();
        if (largeRenderer == null) {
            largeRenderer = new AVChatSurfaceViewRenderer(context);
        }
        if (TextUtils.equals(account, CallAccountHelper.INSTANCE.getAccount())) {
            chatManager.setupLocalVideoRender(
                    null,
                    false,
                    AVChatVideoScalingType.SCALE_ASPECT_BALANCED
            );
            chatManager.setupLocalVideoRender(
                    largeRenderer,
                    false,
                    AVChatVideoScalingType.SCALE_ASPECT_BALANCED
            );
        } else {
            chatManager.setupRemoteVideoRender(
                    remoteAccount,
                    null,
                    false,
                    AVChatVideoScalingType.SCALE_ASPECT_BALANCED
            );
            chatManager.setupRemoteVideoRender(
                    remoteAccount,
                    largeRenderer,
                    false,
                    AVChatVideoScalingType.SCALE_ASPECT_BALANCED
            );
        }

        ViewParent parent = largeRenderer.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(largeRenderer);
        }
        flLargeContainer.removeAllViews();
        flLargeContainer.addView(largeRenderer);
        largeRenderer.setZOrderMediaOverlay(false);
        flLargeContainer.setVisibility(View.VISIBLE);
//        largeCover.setVisibility(View.GONE);
    }

    private void initSmallRenderer(String account) {
        if (flSmallContainer == null || TextUtils.isEmpty(account)) {
            return;
        }
        smallAccount = account;
        if (smallRenderer == null) {
            smallRenderer = new AVChatSurfaceViewRenderer(context);
        }
        AVChatManager chatManager = AVChatManager.getInstance();
        if (TextUtils.equals(account, CallAccountHelper.INSTANCE.getAccount())) {
            chatManager.setupLocalVideoRender(
                    null,
                    false,
                    AVChatVideoScalingType.SCALE_ASPECT_BALANCED
            );
            chatManager.setupLocalVideoRender(
                    smallRenderer,
                    false,
                    AVChatVideoScalingType.SCALE_ASPECT_BALANCED
            );
        } else {
            chatManager.setupRemoteVideoRender(
                    remoteAccount,
                    null,
                    false,
                    AVChatVideoScalingType.SCALE_ASPECT_BALANCED
            );
            chatManager.setupRemoteVideoRender(
                    remoteAccount,
                    smallRenderer,
                    false,
                    AVChatVideoScalingType.SCALE_ASPECT_BALANCED
            );
        }

        ViewParent parent = smallRenderer.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(smallRenderer);
        }
        flSmallContainer.removeAllViews();
        flSmallContainer.addView(smallRenderer);
        smallRenderer.setZOrderMediaOverlay(true);
        flSmallContainer.setVisibility(View.VISIBLE);
//        smallCover.setVisibility(View.GONE);
    }

    /**
     * 注册/注销对方接听挂断状态（音视频模式切换通知）
     */
    private Observer<AVChatCalleeAckEvent> callAckObserver = new Observer<AVChatCalleeAckEvent>() {
        @Override
        public void onEvent(AVChatCalleeAckEvent ackInfo) {
            AVChatData info = avChatData;
            if (info != null && info.getChatId() == ackInfo.getChatId()) {
                CallSoundPlayer.instance().stop();
                if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_BUSY) {
                    Timber.i("callAckEvent %s", "CALLEE_ACK_BUSY");
                    CallSoundPlayer.instance().play(CallSoundPlayer.RingerType.PEER_BUSY);
                    closeSessions(CallExitCode.PEER_BUSY);
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {
                    Timber.i("callAckEvent %s", "CALLEE_ACK_REJECT");
                    closeSessions(CallExitCode.REJECT);
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_AGREE) {
                    Timber.i("callAckEvent %s", "CALLEE_ACK_AGREE");
                    mCallEstablished.set(true);
                }
            }
        }
    };

    /**
     * 注册/注销网络通话控制消息（音视频模式切换通知）
     */
    private Observer<AVChatControlEvent> callControlObserver = new Observer<AVChatControlEvent>() {
        @Override
        public void onEvent(AVChatControlEvent notification) {
            if (AVChatManager.getInstance().getCurrentChatId() != notification.getChatId()) {
                return;
            }
            switch (notification.getControlCommand()) {
//            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO:
//                avChatUI.incomingAudioToVideo();
//                break;
//            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_AGREE:
//                onAudioToVideo();
//                break;
//            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_REJECT:
//                avChatUI.onCallStateChange(CallStateEnum.AUDIO);
//                Toast.makeText(AVChatActivity.this, R.string.avchat_switch_video_reject, Toast.LENGTH_SHORT).show();
//                break;
//            case AVChatControlCommand.SWITCH_VIDEO_TO_AUDIO:
//                onVideoToAudio();
//                break;
                case AVChatControlCommand.NOTIFY_VIDEO_OFF:
                    isRemoteVideoOff = true;
                    notifyCallStateChanged(CallState.VIDEO_OFF);
                    break;
                case AVChatControlCommand.NOTIFY_VIDEO_ON:
                    isRemoteVideoOff = false;
                    notifyCallStateChanged(CallState.VIDEO_ON);
                    break;
                default:
                    Timber.i("对方发来指令值：" + notification.getControlCommand());
                    break;
            }
        }
    };

    /**
     * 注册/注销网络通话对方挂断的通知
     */
    private Observer<AVChatCommonEvent> callHangupObserver = new Observer<AVChatCommonEvent>() {
        @Override
        public void onEvent(AVChatCommonEvent avChatHangUpInfo) {
            AVChatData info = avChatData;
            if (info != null && info.getChatId() == avChatHangUpInfo.getChatId()) {
                CallSoundPlayer.instance().stop();
                closeSessions(CallExitCode.HANGUP);
            }
        }
    };

    /**
     * 注册/注销同时在线的其他端对主叫方的响应
     */
    private Observer<AVChatOnlineAckEvent> onlineAckObserver = new Observer<AVChatOnlineAckEvent>() {
        @Override
        public void onEvent(AVChatOnlineAckEvent ackInfo) {
            AVChatData info = avChatData;
            if (info != null && info.getChatId() == ackInfo.getChatId()) {
                CallSoundPlayer.instance().stop();
                String client = null;
                switch (ackInfo.getClientType()) {
                    case ClientType.Web:
                        client = "Web";
                        break;
                    case ClientType.Windows:
                        client = "Windows";
                        break;
                    case ClientType.Android:
                        client = "Android";
                        break;
                    case ClientType.iOS:
                        client = "iOS";
                        break;
                    case ClientType.MAC:
                        client = "Mac";
                        break;
                    default:
                        break;
                }
                if (client != null) {
                    String option = ackInfo.getEvent() == AVChatEventType.CALLEE_ONLINE_CLIENT_ACK_AGREE ? "接听！" : "拒绝！";
                    ToastUtils.showShort("通话已在" + client + "端被" + option);
                }
                closeSessions(-1);
            }
        }
    };

    private Observer<Integer> timeoutObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer integer) {
            CallSoundPlayer.instance().stop();
            hangUp();
        }
    };

    private Observer<Integer> autoHangUpForLocalPhoneObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer integer) {
            CallSoundPlayer.instance().stop();
            closeSessions(CallExitCode.PEER_BUSY);
        }
    };

    private Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                CallSoundPlayer.instance().stop();
                closeSessions(-1);
            }
        }
    };

    private boolean destroyRTC;

    private void closeRtc(CallState state) {
        if (destroyRTC) {
            return;
        }
        if (state == CallState.OUTGOING_VIDEO_CALLING || state == CallState.VIDEO) {
            AVChatManager.getInstance().stopVideoPreview();
            AVChatManager.getInstance().disableVideo();
        }
        AVChatManager.getInstance().disableRtc();
        destroyRTC = true;
        CallSoundPlayer.instance().stop();
    }

    private volatile boolean closing;

    public void closeSessions(int exitCode) {
        if (closing) {
            return;
        }
        closing = true;
        Timber.i("closeSession: code=%s", CallExitCode.getExitString(exitCode));
        CallSoundPlayer.instance().stop();
        registerCallObserver(false);
        setLargeContainer(null);
        setSmallContainer(null);
        mCallEstablished.set(false);
        stopTimer();
        mCallTimeCallback = null;
        showQuitToast(exitCode);
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mOnCloseSessionListener != null) {
                    mOnCloseSessionListener.onCloseSession();
                    mOnCloseSessionListener = null;
                }
                closing = false;
                mChatting = false;
                avChatData = null;
            }
        }, 1000);
        if (destroyRTC) {
            destroyRTC = false;
            return;
        }
        if (callingState != null && callingState.getValue() >= CallState.OUTGOING_VIDEO_CALLING.getValue() || callingState == CallState.VIDEO) {
            AVChatManager.getInstance().stopVideoPreview();
            AVChatManager.getInstance().disableVideo();
        }
        AVChatManager.getInstance().disableRtc();
    }

    /**
     * 给出结束的提醒
     *
     * @param code
     */
    public void showQuitToast(int code) {
        switch (code) {
            case CallExitCode.NET_CHANGE: // 网络切换
            case CallExitCode.NET_ERROR: // 网络异常
            case CallExitCode.CONFIG_ERROR: // 服务器返回数据错误
                ToastUtils.showShort(R.string.call_net_error_then_quit);
                break;
            case CallExitCode.PEER_HANGUP:
            case CallExitCode.HANGUP:
                if (mCallEstablished.get()) {
                    ToastUtils.showShort(R.string.call_call_finish);
                }
                break;
            case CallExitCode.PEER_BUSY:
                ToastUtils.showShort(R.string.call_peer_busy);
                break;
            case CallExitCode.PROTOCOL_INCOMPATIBLE_PEER_LOWER:
                ToastUtils.showShort(R.string.call_peer_protocol_low_version);
                break;
            case CallExitCode.PROTOCOL_INCOMPATIBLE_SELF_LOWER:
                ToastUtils.showShort(R.string.call_local_protocol_low_version);
                break;
            case CallExitCode.INVALIDE_CHANNELID:
                ToastUtils.showShort(R.string.call_invalid_channel_id);
                break;
            case CallExitCode.LOCAL_CALL_BUSY:
                ToastUtils.showShort(R.string.call_local_call_busy);
                break;
            default:
                break;
        }
    }

    public void onCallEstablished() {
        CallTimeoutObserver.INSTANCE.observeTimeoutNotification(
                timeoutObserver, false, isIncomingCall);
        startTimer();
        if (callType == AVChatType.VIDEO.getValue()) {
            initSmallRenderer(CallAccountHelper.INSTANCE.getAccount());
            notifyCallStateChanged(CallState.VIDEO);
        } else {
            notifyCallStateChanged(CallState.AUDIO);
        }
    }

    private int mSeconds = 0;

    private void startTimer() {
        mSeconds = 0;
        uiHandler.post(refreshCallTime);
    }

    private void stopTimer() {
        uiHandler.removeCallbacks(refreshCallTime);
    }

    private final Runnable refreshCallTime = new Runnable() {
        @Override
        public void run() {
            uiHandler.postDelayed(refreshCallTime, 1000);
            if (mCallTimeCallback != null) {
                mCallTimeCallback.onCallTime(mSeconds);
            }
            mSeconds++;
        }
    };

    public static String formatCallTime(int seconds) {
        final int hh = seconds / 60 / 60;
        final int mm = seconds / 60 % 60;
        final int ss = seconds % 60;
        return (hh > 9 ? "" + hh : "0" + hh) +
                (mm > 9 ? ":" + mm : ":0" + mm) +
                (ss > 9 ? ":" + ss : ":0" + ss);
    }

    public interface CallTimeCallback {
        void onCallTime(int seconds);
    }

    private CallTimeCallback mCallTimeCallback;

    public void setCallTimeCallback(CallTimeCallback callTimeCallback) {
        mCallTimeCallback = callTimeCallback;
    }

    public void call(String remoteAccount, final AVChatType chatType) {
        CallSoundPlayer.instance().play(CallSoundPlayer.RingerType.CONNECTING);
        this.remoteAccount = remoteAccount;
        AVChatManager chatManager = AVChatManager.getInstance();
        closing = false;
        chatManager.enableRtc();
        if (parameters == null) {
            parameters = CallParameters.INSTANCE.provideParameter();
        }
        chatManager.setParameters(parameters);
//        chatManager.setParameter(AVChatParameters.KEY_VIDEO_FRAME_FILTER, true);
        if (videoCapturer == null) {
            videoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
        }
        chatManager.setupVideoCapturer(videoCapturer);
        if (chatType == AVChatType.VIDEO) {
            chatManager.enableVideo();
            chatManager.startVideoPreview();
        }

        if (chatType == AVChatType.AUDIO) {
            notifyCallStateChanged(CallState.OUTGOING_AUDIO_CALLING);
        } else {
            notifyCallStateChanged(CallState.OUTGOING_VIDEO_CALLING);
        }
        AVChatNotifyOption notifyOption = new AVChatNotifyOption();
        notifyOption.extendMessage = "extra_data";
        Timber.i("call2: remoteAccount=%s chatType=%s notifyOption=%s", remoteAccount, chatType, notifyOption);
        chatManager.call2(remoteAccount, chatType, notifyOption, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData data) {
                Timber.i("call2 -> onSuccess: data=%s", data);
                avChatData = data;
                // checkPermission @link Manifest.permission.CAMERA
                initLargeRenderer(CallAccountHelper.INSTANCE.getAccount());
                notifyCallStateChanged(CallState.CONNECT_SUCCESS);
            }

            @Override
            public void onFailed(int code) {
                Timber.w("call2 -> onFailed: code=%s", code);
                if (code == ResponseCode.RES_FORBIDDEN) {
                    ToastUtils.showShort(R.string.call_no_permission);
                } else {
                    ToastUtils.showShort(R.string.call_call_failed);
                }
                closeRtc(chatType == AVChatType.VIDEO ? CallState.VIDEO : CallState.AUDIO);
//                notifyCallStateChanged(CallState.CONNECT_FAILED);
                closeSessions(-1);
            }

            @Override
            public void onException(Throwable exception) {
                Timber.w(exception, "call2 -> onException: ");
                ToastUtils.showShort(R.string.call_call_failed);
                closeRtc(chatType == AVChatType.VIDEO ? CallState.VIDEO : CallState.AUDIO);
//                notifyCallStateChanged(CallState.CONNECT_FAILED);
                closeSessions(-1);
            }
        });

    }

    /**
     * 来电
     */
    public void onInComingCalling(AVChatData avChatData) {
        this.avChatData = avChatData;
        remoteAccount = avChatData.getAccount();
        CallSoundPlayer.instance().play(CallSoundPlayer.RingerType.RING);
        if (avChatData.getChatType() == AVChatType.AUDIO) {
            notifyCallStateChanged(CallState.INCOMING_AUDIO_CALLING);
        } else {
            notifyCallStateChanged(CallState.INCOMING_VIDEO_CALLING);
        }
    }

    public void refuse() {
        switch (callingState) {
            case INCOMING_AUDIO_CALLING:
            case AUDIO_CONNECTING:
                rejectInComingCall();
                break;
            case INCOMING_AUDIO_TO_VIDEO:
//                rejectAudioToVideo();
                break;
            case INCOMING_VIDEO_CALLING:
            case VIDEO_CONNECTING: // 连接中点击拒绝
                rejectInComingCall();
                break;
            default:
                break;
        }
    }

    private void rejectInComingCall() {
        CallSoundPlayer.instance().stop();
        notifyCallStateChanged(CallState.INCOMING_VIDEO_REFUSING);
        long chatId = avChatData.getChatId();
        Timber.i("hangUp2: chatId=%s", chatId);
        AVChatManager.getInstance().hangUp2(chatId, new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Timber.i("hangUp2 -> onSuccess:");
                closeSessions(-1);
            }

            @Override
            public void onFailed(int code) {
                Timber.w("hangUp2 -> onFailed: code=%s", code);
                closeSessions(-1);
            }

            @Override
            public void onException(Throwable exception) {
                Timber.w(exception, "hangUp2 -> onException: ");
                closeSessions(-1);
            }
        });
    }

    public void receive() {
        switch (callingState) {
            case INCOMING_AUDIO_CALLING:
                receive(AVChatType.AUDIO);
                break;
            case AUDIO_CONNECTING: // 连接中，继续点击开启 无反应
                break;
            case INCOMING_VIDEO_CALLING:
                receive(AVChatType.VIDEO);
                break;
            case VIDEO_CONNECTING: // 连接中，继续点击开启 无反应
                break;
            case INCOMING_AUDIO_TO_VIDEO:
//                receiveAudioToVideo();
            default:
                break;
        }
    }

    private AVChatParameters parameters;

    private void receive(final AVChatType chatType) {
        closing = false;
        CallSoundPlayer.instance().stop();
        AVChatManager chatManager = AVChatManager.getInstance();
        chatManager.enableRtc();
        if (parameters == null) {
            parameters = CallParameters.INSTANCE.provideParameter();
        }
        chatManager.setParameters(parameters);
//        chatManager.setParameter(AVChatParameters.KEY_VIDEO_FRAME_FILTER, true);
        if (videoCapturer == null) {
            videoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
        }
        chatManager.setupVideoCapturer(videoCapturer);
        if (chatType == AVChatType.VIDEO) {
            chatManager.enableVideo();
            chatManager.startVideoPreview();
        }
        long chatId = avChatData.getChatId();
        Timber.i("accept2: chatId = %s", chatId);
        if (chatType == AVChatType.VIDEO) {
            notifyCallStateChanged(CallState.VIDEO_CONNECTING);
        } else {
            notifyCallStateChanged(CallState.AUDIO_CONNECTING);
        }
        chatManager.accept2(chatId, new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Timber.i("accept2 -> onSuccess:");
                mCallEstablished.set(true);
                notifyCallStateChanged(CallState.INCOMING_VIDEO_RECEIVE_SUCCESS);
            }

            @Override
            public void onFailed(int code) {
                Timber.w("accept2 -> onFailed: code = %s", code);
                if (code == -1) {
                    ToastUtils.showShort("本地音视频启动失败");
                } else {
                    ToastUtils.showShort("建立连接失败");
                }
                handleAcceptFailed(chatType == AVChatType.VIDEO ?
                        CallState.VIDEO_CONNECTING : CallState.AUDIO);
            }

            @Override
            public void onException(Throwable exception) {
                Timber.w(exception, "accept2 -> onException: ");
                handleAcceptFailed(chatType == AVChatType.VIDEO ?
                        CallState.VIDEO_CONNECTING : CallState.AUDIO);
            }
        });
    }

    private void handleAcceptFailed(CallState state) {
        if (state == CallState.VIDEO_CONNECTING) {
            AVChatManager.getInstance().stopVideoPreview();
            AVChatManager.getInstance().disableVideo();
        }
        hangUp(CallExitCode.CANCEL);
    }

    public void hangUp() {
        if (mCallEstablished.get()) {
            hangUp(CallExitCode.HANGUP);
        } else {
            hangUp(CallExitCode.CANCEL);
        }
    }

    public void hangUp(final int code) {
        if (destroyRTC) {
            return;
        }
        if (avChatData != null && (code == CallExitCode.HANGUP
                || code == CallExitCode.PEER_NO_RESPONSE
                || code == CallExitCode.REJECT
                || code == CallExitCode.CANCEL)) {
            long chatId = avChatData.getChatId();
            Timber.i("hangUp2: chatId=%s", chatId);
            AVChatManager.getInstance().hangUp2(chatId, new AVChatCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Timber.i("hangUp2 -> onSuccess: ");
                    closeSessions(code);
                }

                @Override
                public void onFailed(int errorCode) {
                    Timber.w("hangUp2 -> onFailed: errorCode=%s", errorCode);
                    closeSessions(code);
                }

                @Override
                public void onException(Throwable exception) {
                    Timber.w(exception, "hangUp2 -> onException");
                    closeSessions(code);
                }
            });
        }
    }

    public void switchCamera() {
        if (videoCapturer != null) {
            videoCapturer.switchCamera();
        }
    }

    private boolean needRestoreLocalVideo = false;
    private boolean needRestoreLocalAudio = false;

    //恢复视频和语音发送
    public void resume() {
        if (needRestoreLocalVideo) {
            AVChatManager.getInstance().muteLocalVideo(false);
            needRestoreLocalVideo = false;
        }

        if (needRestoreLocalAudio) {
            AVChatManager.getInstance().muteLocalAudio(false);
            needRestoreLocalAudio = false;
        }

    }

    //关闭视频和语音发送.
    public void pause() {
        if (!AVChatManager.getInstance().isLocalVideoMuted()) {
            AVChatManager.getInstance().muteLocalVideo(true);
            needRestoreLocalVideo = true;
        }

        if (!AVChatManager.getInstance().isLocalAudioMuted()) {
            AVChatManager.getInstance().muteLocalAudio(true);
            needRestoreLocalAudio = true;
        }
    }


    private CallFloatViewHelper callFloatViewHelper;

    public void enterFloatWindow() {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callFloatViewHelper == null) {
                    callFloatViewHelper = new CallFloatViewHelper(context);
                }
                callFloatViewHelper.setFullScreenOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitFloatWindow();
                        CallHelper.enterFullScreen(context);
                    }
                });
                callFloatViewHelper.setCloseOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hangUp();
                    }
                });
                callFloatViewHelper.show();
            }
        }, 300);
    }

    public void exitFloatWindow() {
        if (callFloatViewHelper != null) {
            callFloatViewHelper.dismiss();
            callFloatViewHelper = null;
        }
    }

    public void dispatchIncomingCallFromBroadCast(Context context, AVChatData avChatData) {
        this.avChatData = avChatData;
        isIncomingCall = true;
        CallHelper.incomingCall(context, avChatData, CallHelper.SOURCE_BROADCAST);
    }

}
