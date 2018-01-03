package com.dong.statistics.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dong.statistics.data.InfoQueue;
import com.dong.statistics.data.StatisticsInfo;
import com.dong.statistics.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <dr_dong>
 *         Time : 2017/12/13 16:52
 */
public class UploadService extends Service {

    private static final String TAG = UploadService.class.getSimpleName();

    private Handler handler;
    private Runnable runnableInterval;
    private Runnable runnableBatch;
    private Runnable runnableRealTime;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(TAG, "===onStartCommand===");
        handler = new Handler();
        if (UploadPolicy.getUploadPolicyType() == UploadPolicy.UPLOAD_POLICY_REAL_TIME) {
            realTimeUpload();
        } else if (UploadPolicy.getUploadPolicyType() == UploadPolicy.UPLOAD_POLICY_WHILE_INITIALIZE) {
            batchUpload();
        } else {
            while (!InfoQueue.getInstance().getQueueBackups().isEmpty()) {
                LogUtils.d(TAG, "===备份还原");
                InfoQueue.getInstance().add(InfoQueue.getInstance().getBackups());
            }
            if (UploadPolicy.getInstance().getIntervalTime() != 0) {
                intervalUpload();
            } else {
                batchUpload();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 时时上报
     */
    private void realTimeUpload() {
        runnableRealTime = new Runnable() {
            @Override
            public void run() {
                StatisticsInfo info = InfoQueue.getInstance().get(true);
                if (info != null) {
                    LogUtils.i(TAG, "===" + info.toString());
                }
                handler.post(runnableRealTime);
            }
        };
        handler.post(runnableRealTime);
    }

    /**
     * 定时上报空控制
     */
    private void intervalUpload() {
        runnableInterval = new Runnable() {
            @Override
            public void run() {
                batchUpload();
                handler.postDelayed(this, UploadPolicy.getInstance().getIntervalTime() * 60000L);
            }
        };
        handler.postDelayed(runnableInterval, UploadPolicy.getInstance().getIntervalTime() * 60000L);
    }

    /**
     * 定量上报控制
     */
    private void batchUpload() {
        runnableBatch = new Runnable() {
            @Override
            public void run() {
                if (upload()) {
                    handler.post(this);
                }
            }
        };
        handler.post(runnableBatch);
    }

    /**
     * @return true 数据没有上报完，继续；false 上报完毕
     */
    private boolean upload() {
        int batchSize = UploadPolicy.getInstance().getBatchSize();
        List<StatisticsInfo> infoList = new ArrayList<>();
        if (UploadPolicy.getUploadPolicyType() == UploadPolicy.UPLOAD_POLICY_WHILE_INITIALIZE) {
            if (InfoQueue.getInstance().getQueueBackups().isEmpty()) {
                return false;
            }
            while (!InfoQueue.getInstance().getQueueBackups().isEmpty() && batchSize > 0) {
                infoList.add(InfoQueue.getInstance().getBackups());
                batchSize--;
            }
        } else {
            if (batchSize > InfoQueue.getInstance().getQueue().size()) {
                return false;
            }
            while (!InfoQueue.getInstance().getQueue().isEmpty() && batchSize > 0) {
                infoList.add(InfoQueue.getInstance().get(true));
                batchSize--;
            }
        }
        //网络上报
        LogUtils.d(TAG, "网络上报条数===" + infoList.size());
        for (int i = 0; i < infoList.size(); i++) {
            LogUtils.i(TAG, "网络上报===" + i + "===" + infoList.get(i).toString());
        }
        return true;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "===onDestroy===");
        if (runnableInterval != null) {
            handler.removeCallbacks(runnableInterval);
        }
        if (runnableBatch != null) {
            handler.removeCallbacks(runnableBatch);
        }
        if (runnableRealTime != null) {
            handler.removeCallbacks(runnableRealTime);
        }
        super.onDestroy();
    }
}
