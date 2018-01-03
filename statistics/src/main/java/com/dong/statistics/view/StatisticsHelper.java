package com.dong.statistics.view;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dong.statistics.data.StatisticsConstant;
import com.dong.statistics.data.StatisticsInfo;
import com.dong.statistics.utils.ReflectUtils;
import com.dong.statistics.utils.ResourceUtils;
import com.dong.statistics.utils.StatisticsReport;
import com.dong.statistics.utils.StatisticsUtils;
import com.dong.statistics.utils.preferences.PreferencesUtils;

import java.util.List;

/**
 * @author <dr_dong>
 *         Time : 2017/12/1 16:11
 *         将页面的元素解析过滤，对点击事件添加统计
 */
public class StatisticsHelper {

    private static final String TAG = StatisticsHelper.class.getSimpleName();
    private final String mOnCheckedChangeListener = "mOnCheckedChangeListener";
    private final String mOnClickListener = "mOnClickListener";
    private final String mOnItemClickListener = "mOnItemClickListener";
    private final String mListenerInfo = "mListenerInfo";
    public StatisticsContext context;

    public StatisticsHelper(StatisticsContext context, View view) {
        this.context = context;
        init(view);
    }

    private void init(View view) {
        //获取当前Activity所有的子View
        List<View> allChildViews = StatisticsUtils.getAllChildViews(view);
        for (final View v : allChildViews) {
            bindingListener(context, v);
        }
    }

    private boolean bindingListener(StatisticsContext context, View view) {
        if (view instanceof CompoundButton) {
            //CompoundButton 获取 OnCheckedChangeListener
            OnCheckedChangeListener onCheckedChangeListener =
                    (OnCheckedChangeListener) getListener(view, mOnCheckedChangeListener);
            if (onCheckedChangeListener != null) {
                try {
                    hookOnCheckedChangeListener(context, (CompoundButton) view, onCheckedChangeListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        } else if (view instanceof AdapterView) {
            //利用反射获取 AdapterView 中设置的 mOnItemClickListener
            OnItemClickListener onItemClickListener =
                    (OnItemClickListener) getListener(view, mOnItemClickListener);
            if (onItemClickListener != null) {
                try {
                    hookOnItemClickListener(context, (AdapterView) view, onItemClickListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        } else {
            //利用反射获取view中设置的listener
            View.OnClickListener onClickListener = (View.OnClickListener) getListener(view, mOnClickListener);
            if (onClickListener != null) {
                try {
                    hookOnClickListener(context, view, onClickListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }
    }

    /**
     * 添加统计代码
     *
     * @param view
     * @param onCheckedChangeListener
     */
    private void hookOnCheckedChangeListener(final StatisticsContext context,
                                             final CompoundButton view,
                                             final OnCheckedChangeListener onCheckedChangeListener) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (view.getId() == -1) {
                    return;
                }
                OnCheckedChangeListener onCheckedChangeListenerHooked = new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            addStatistics(null, view, -1, context);
                        }
                        onCheckedChangeListener.onCheckedChanged(view, isChecked);

                    }
                };
                setListener(view, mOnCheckedChangeListener, onCheckedChangeListenerHooked);
            }
        });
    }


    /**
     * 添加统计代码
     *
     * @param view
     * @param onClickListener
     */
    private void hookOnClickListener(final StatisticsContext context,
                                     final View view,
                                     final View.OnClickListener onClickListener) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (view.getId() == -1) {
                    return;
                }
                View.OnClickListener onClickListenerHooked = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        addStatistics(null, view, -1, context);
                        onClickListener.onClick(view);
                    }
                };
                setListener(view, mOnClickListener, onClickListenerHooked);
            }
        });
    }

    /**
     * 添加统计代码
     *
     * @param view
     * @param onItemClickListener
     */
    private void hookOnItemClickListener(final StatisticsContext context,
                                         final AdapterView view,
                                         final OnItemClickListener onItemClickListener) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (view.getId() == -1) {
                    return;
                }
                OnItemClickListener onItemClickListenerHooked = new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        addStatistics(parent, view, position, context);
                        onItemClickListener.onItemClick(parent, view, position, id);
                    }
                };
                setListener(view, mOnItemClickListener, onItemClickListenerHooked);
            }
        });
    }

    /**
     * @param parent
     * @param view
     * @param position 除了 AdapterView 传-1
     * @param context
     */
    private void addStatistics(AdapterView<?> parent, View view, int position, StatisticsContext context) {
        try {
            String name;
            if (parent == null && view == null) {
                name = StatisticsConstant.SV_UNKNOWN;
            } else if (parent == null) {
                name = ResourceUtils.getResourceNameById(Class.forName(StatisticsUtils.getInstance().getRClassName()), view.getId());
            } else if (view == null) {
                name = ResourceUtils.getResourceNameById(Class.forName(StatisticsUtils.getInstance().getRClassName()), parent.getId());
            } else {
                name = ResourceUtils.getResourceNameById(Class.forName(StatisticsUtils.getInstance().getRClassName()), parent.getId()) + "/" +
                        ResourceUtils.getResourceNameById(Class.forName(StatisticsUtils.getInstance().getRClassName()), view.getId());
            }
            StatisticsInfo statisticsInfo = new StatisticsInfo();
            statisticsInfo.setE_t(StatisticsConstant.E_T_ELEMENT_CLICK);
            statisticsInfo.setS_x(PreferencesUtils.get(StatisticsConstant.SK_S_X, 0));
            statisticsInfo.setS_y(PreferencesUtils.get(StatisticsConstant.SK_S_Y, 0));
            if (position == -1) {
                statisticsInfo.setE_p(context.getClass().getSimpleName() + "/" + name);
            } else {
                statisticsInfo.setE_p(context.getClass().getSimpleName() + "/" + name + "/" + position);
            }
            if (view instanceof TextView && !TextUtils.isEmpty(((TextView) view).getText())) {
                statisticsInfo.setE_p(statisticsInfo.getE_p() + "[" + ((TextView) view).getText().toString().replace
                        (" ", "") + "]");
            } else if (view instanceof LinearLayout || view instanceof RelativeLayout || view instanceof FrameLayout) {
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
        }
    }

    private void setListener(View view, String listenerName, Object value) {
        setListener(view, getClassByListenerName(listenerName), listenerName, value);
    }

    private void setListener(View view, Class<?> targetClass, String fieldName, Object value) {
        int level = countLevelFromViewToFather(view, targetClass);
        if (-1 == level) {
            return;
        }

        try {
            if (!(view instanceof AdapterView)
                    && !(view instanceof CompoundButton)
                    && Build.VERSION.SDK_INT > 14) {
                Object mListenerInfo = ReflectUtils.getField(view, targetClass.getName(), this.mListenerInfo);
                if (null == mListenerInfo) {
                    return;
                }
                ReflectUtils.setField(mListenerInfo, null, fieldName, value);
            } else {
                ReflectUtils.setField(view, targetClass.getName(), fieldName, value);
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getListener(View view, String fieldName) {
        return getListener(view, getClassByListenerName(fieldName), fieldName);
    }

    /**
     * 获取Listener
     *
     * @param view
     * @param targetClass
     * @param fieldName
     * @return
     */
    private Object getListener(View view, Class<?> targetClass, String fieldName) {
        //获取层级
        int level = countLevelFromViewToFather(view, targetClass);
        if (-1 == level) {
            return null;
        }

        try {
            //处理API14以上的情况，View中的listener封装下ListenerInfo类中
            if (!(view instanceof AdapterView)
                    && !(view instanceof CompoundButton)
                    && Build.VERSION.SDK_INT > 14) {
                Object mListenerInfo = ReflectUtils.getField(view, targetClass.getName(), this.mListenerInfo);
                return null == mListenerInfo ? null : ReflectUtils.getField(mListenerInfo, null, fieldName);
            } else {
                Object o = ReflectUtils.getField(view, targetClass.getName(), fieldName);
                return o;

            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // eat it
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param listenerName
     * @return
     */
    private Class<?> getClassByListenerName(String listenerName) {
        Class<?> viewClass = null;
        if (mOnItemClickListener.equals(listenerName)
                || "mOnItemLongClickListener".equals(listenerName)) {
            viewClass = AdapterView.class;
        } else if ("mOnScrollListener".equals(listenerName)) {
            viewClass = AbsListView.class;
        } else if ("mOnChildClickListener".equals(listenerName)
                || "mOnGroupClickListener".equals(listenerName)) {
            viewClass = ExpandableListView.class;
        } else if (mOnCheckedChangeListener.equals(listenerName)) {
            viewClass = CompoundButton.class;
        } else {
            viewClass = View.class;
        }
        return viewClass;
    }

    /**
     * 获取子类在父类的层级
     *
     * @param view
     * @param father
     * @return
     */
    private int countLevelFromViewToFather(View view, Class<?> father) {
        if (null == view) {
            return -1;
        }
        int level = 0;
        Class<?> originalClass = view.getClass();
        while (true) {
            if (originalClass.equals(Object.class)) {
                return -1;
            } else if (originalClass.equals(father)) {
                return level;
            } else {
                level++;
                originalClass = originalClass.getSuperclass();
            }
        }
    }

}