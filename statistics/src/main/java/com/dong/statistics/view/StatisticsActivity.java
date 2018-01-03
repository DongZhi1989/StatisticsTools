package com.dong.statistics.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.dong.statistics.data.StatisticsConstant;
import com.dong.statistics.data.StatisticsInfo;
import com.dong.statistics.utils.LogUtils;
import com.dong.statistics.utils.StatisticsReport;
import com.dong.statistics.utils.StatisticsUtils;
import com.dong.statistics.utils.preferences.PreferencesUtils;

/**
 * @author <dr_dong>
 *         Time : 2017/12/7 10:50
 *         Activity 信息统计
 */
public class StatisticsActivity extends AppCompatActivity implements StatisticsContext {

    private StatisticsHelper statisticsHelper;
    private long statisticsTime;
    private boolean isMoveMotion = false;
    private boolean appClose = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        LogUtils.d(this.getClass().getSimpleName(), "====onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d(this.getClass().getSimpleName(), "====onStart");
        if (statisticsHelper == null) {
            View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
            statisticsHelper = new StatisticsHelper(this, view);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(this.getClass().getSimpleName(), "====onResume");
        StatisticsUtils.activityNames.toName = this.getClass().getSimpleName();
        statisticsTime = System.currentTimeMillis();
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        statisticsInfo.setE_t(StatisticsConstant.E_T_PAGE_VIEW);
        statisticsInfo.setE_p(this.getClass().getSimpleName());
        StatisticsReport.reportCollectInfo(statisticsInfo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d(this.getClass().getSimpleName(), "====onPause");
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        statisticsInfo.setE_t(StatisticsConstant.E_T_PAGE_LEAVE);
        statisticsInfo.setE_p(this.getClass().getSimpleName());
        statisticsInfo.setDur(System.currentTimeMillis() - statisticsTime);
        StatisticsReport.reportCollectInfo(statisticsInfo);
        StatisticsUtils.activityNames.fromName = this.getClass().getSimpleName();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(this.getClass().getSimpleName(), "====onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d(this.getClass().getSimpleName(), "====onDestroy");
        if (appClose) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMoveMotion = false;
                break;
            case MotionEvent.ACTION_MOVE:
                isMoveMotion = true;
                break;
            case MotionEvent.ACTION_UP:
                if (!isMoveMotion) {
                    PreferencesUtils.put(StatisticsConstant.SK_S_X, (int) ev.getRawX());
                    PreferencesUtils.put(StatisticsConstant.SK_S_Y, (int) ev.getRawY());
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void finish() {
        if (isTaskRoot()) {
            StatisticsReport.saveInfo();
            appClose = true;
        }
        super.finish();
    }

}
