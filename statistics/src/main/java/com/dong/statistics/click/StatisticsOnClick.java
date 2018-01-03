package com.dong.statistics.click;

import android.text.TextUtils;
import android.view.View;
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
 *         Time : 2017/12/1 11:48
 *         针对普通点击事件处理
 */
public abstract class StatisticsOnClick implements View.OnClickListener {

    private StatisticsContext context;
    private int position;

    public StatisticsOnClick(StatisticsContext context) {
        this.context = context;
        this.position = -1;
    }

    public StatisticsOnClick(StatisticsContext obj, int position) {
        this.context = obj;
        this.position = position;
    }

    public abstract void click(View v);

    @Override
    public void onClick(View v) {
        try {
            String name = ResourceUtils.getResourceNameById(Class.forName(StatisticsUtils.getInstance().getRClassName()), v.getId());
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
            if (v instanceof TextView && !TextUtils.isEmpty(((TextView) v).getText())) {
                statisticsInfo.setE_p(statisticsInfo.getE_p() + "[" + ((TextView) v).getText().toString().replace(" ", "") + "]");
            } else if (v instanceof LinearLayout || v instanceof RelativeLayout || v instanceof FrameLayout) {
                StringBuilder txt = new StringBuilder();
                for (View vv : StatisticsUtils.getAllChildViews(v)) {
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
            if (v.getTag() != null && v.getTag() instanceof String) {
                statisticsInfo.setE_d((String) v.getTag());
            }
            StatisticsReport.reportCollectInfo(statisticsInfo);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            //test
            click(v);
        }
    }
}
