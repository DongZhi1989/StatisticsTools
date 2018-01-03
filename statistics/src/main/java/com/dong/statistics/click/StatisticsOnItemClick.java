package com.dong.statistics.click;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dong.statistics.data.StatisticsConstant;
import com.dong.statistics.data.StatisticsInfo;
import com.dong.statistics.utils.ResourceUtils;
import com.dong.statistics.utils.StatisticsReport;
import com.dong.statistics.utils.StatisticsUtils;
import com.dong.statistics.utils.preferences.PreferencesUtils;
import com.dong.statistics.view.StatisticsContext;

/**
 * @author <dr_dong>
 *         Time : 2017/12/4 17:39
 *         针对 item 的点击事件处理
 */
public abstract class StatisticsOnItemClick implements AdapterView.OnItemClickListener {

    private StatisticsContext context;

    public StatisticsOnItemClick(StatisticsContext context) {
        this.context = context;
    }

    public abstract void itemClick(AdapterView<?> parent, View view, int position, long id);

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            String name;
            if (view == null) {
                name = StatisticsConstant.SV_UNKNOWN;
            } else {
                name = ResourceUtils.getResourceNameById(Class.forName(StatisticsUtils.getInstance().getRClassName()), parent.getId());
            }
            StatisticsInfo statisticsInfo = new StatisticsInfo();
            statisticsInfo.setE_t(StatisticsConstant.E_T_ELEMENT_CLICK);
            //点击添加
            statisticsInfo.setS_x(PreferencesUtils.get(StatisticsConstant.SK_S_X, 0));
            statisticsInfo.setS_y(PreferencesUtils.get(StatisticsConstant.SK_S_Y, 0));
            if (context != null) {
                if (position == -1) {
                    statisticsInfo.setE_p(context.getClass().getSimpleName() + "/" + name);
                } else {
                    statisticsInfo.setE_p(context.getClass().getSimpleName() + "/" + name + "/" + position);
                }
            }
            if (view instanceof LinearLayout || view instanceof RelativeLayout || view instanceof FrameLayout) {
                StringBuilder txt = new StringBuilder();
                for (View vv : StatisticsUtils.getAllChildViews(view)) {
                    if (vv instanceof TextView && !TextUtils.isEmpty(((TextView) vv).getText())) {
                        txt = txt.append(((TextView) vv).getText()).append(">");
                    }
                }
                String txtStr = txt.toString();
                if (txtStr.length() > 0) {
                    txtStr = txtStr.substring(0, txtStr.length() - 1);
                }
                if (!TextUtils.isEmpty(txtStr)) {
                    statisticsInfo.setE_p(statisticsInfo.getE_p() + "[" + txtStr.replace(" ", "") + "]");
                }
            }
            if (view != null && view.getTag() != null && view.getTag() instanceof String) {
                statisticsInfo.setE_d((String) view.getTag());
            }
            StatisticsReport.reportCollectInfo(statisticsInfo);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            itemClick(parent, view, position, id);
        }

    }
}
