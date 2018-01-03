package com.dong.statistics.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dong.statistics.data.StatisticsConstant;
import com.dong.statistics.data.StatisticsInfo;
import com.dong.statistics.utils.LogUtils;
import com.dong.statistics.utils.StatisticsReport;

/**
 * @author <dr_dong>
 *         Time : 2017/12/4 11:17
 *         DialogFragment 信息统计
 */
public class StatisticsDialogFragment extends DialogFragment implements StatisticsContext {

    private StatisticsHelper statisticsHelper;
    private long statisticsTime;
    private View mRootView;

    public void setmRootView(View mRootView) {
        this.mRootView = mRootView;
    }

    @Override
    public void onAttach(Context context) {
        LogUtils.d(this.getClass().getSimpleName(), "====onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.d(this.getClass().getSimpleName(), "====onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.d(this.getClass().getSimpleName(), "====onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        LogUtils.d(this.getClass().getSimpleName(), "====onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        LogUtils.d(this.getClass().getSimpleName(), "====onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        LogUtils.d(this.getClass().getSimpleName(), "====onResume");
        super.onResume();
        if (mRootView == null) {
            return;
        }
        if (statisticsHelper == null) {
            statisticsHelper = new StatisticsHelper(this, mRootView);
        }
        statisticsTime = System.currentTimeMillis();
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        statisticsInfo.setE_p(this.getClass().getSimpleName());
        statisticsInfo.setE_t(StatisticsConstant.E_T_PAGE_VIEW);
        StatisticsReport.reportCollectInfo(statisticsInfo);
    }

    @Override
    public void onPause() {
        LogUtils.d(this.getClass().getSimpleName(), "====onPause");
        super.onPause();
        if (mRootView == null) {
            return;
        }
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        statisticsInfo.setE_p(this.getClass().getSimpleName());
        statisticsInfo.setE_t(StatisticsConstant.E_T_PAGE_LEAVE);
        statisticsInfo.setDur(System.currentTimeMillis() - statisticsTime);
        StatisticsReport.reportCollectInfo(statisticsInfo);
    }

    @Override
    public void onStop() {
        LogUtils.d(this.getClass().getSimpleName(), "====onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        LogUtils.d(this.getClass().getSimpleName(), "====onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtils.d(this.getClass().getSimpleName(), "====onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        LogUtils.d(this.getClass().getSimpleName(), "====onDetach");
        super.onDetach();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtils.d(this.getClass().getSimpleName(), "====setUserVisibleHint===" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }

}
