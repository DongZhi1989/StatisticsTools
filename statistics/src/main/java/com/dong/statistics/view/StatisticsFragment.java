package com.dong.statistics.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dong.statistics.data.StatisticsConstant;
import com.dong.statistics.data.StatisticsInfo;
import com.dong.statistics.utils.LogUtils;
import com.dong.statistics.utils.StatisticsReport;

/**
 * @author <dr_dong>
 *         Time : 2017/12/7 10:43
 *         Fragment 信息统计
 */
public class StatisticsFragment extends Fragment implements StatisticsContext {

    public boolean isCreated = false;
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
        isCreated = true;
        //用于时间记录，此处记录是因为viewpage的第一个fragment首次进入时isVisibleToUser为false无法记录
        statisticsTime = System.currentTimeMillis();
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
    }

    @Override
    public void onPause() {
        LogUtils.d(this.getClass().getSimpleName(), "====onPause");
        super.onPause();
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
        /** dialogFragment 页面显示统计，包括显示、隐藏 */
        if (!isCreated) {
            return;
        }
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        if (isVisibleToUser) {
            statisticsInfo.setE_t(StatisticsConstant.E_T_PAGE_VIEW);
            statisticsTime = System.currentTimeMillis();
        } else {
            statisticsInfo.setE_t(StatisticsConstant.E_T_PAGE_LEAVE);
            statisticsInfo.setDur(System.currentTimeMillis() - statisticsTime);
        }
        statisticsInfo.setE_p(this.getClass().getSimpleName());
        if (getFragmentManager() != null && getFragmentManager().getFragments() != null
                && !getFragmentManager().getFragments().isEmpty()) {
            for (int i = 0; i < getFragmentManager().getFragments().size(); i++) {
                if (this == getFragmentManager().getFragments().get(i)) {
                    statisticsInfo.setE_p(statisticsInfo.getE_p() + "/" + i);
                }
            }
        }
        StatisticsReport.reportCollectInfo(statisticsInfo);
        if (isVisibleToUser) {
            if (statisticsHelper == null) {
                statisticsHelper = new StatisticsHelper(this, mRootView);
            }
        }
    }


}
