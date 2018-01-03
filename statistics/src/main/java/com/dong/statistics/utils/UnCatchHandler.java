package com.dong.statistics.utils;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.dong.statistics.data.StatisticsConstant;
import com.dong.statistics.data.StatisticsInfo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author <dr_dong>
 *         Time : 2017/12/18 10:34
 */
public class UnCatchHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = UnCatchHandler.class.getSimpleName();
    private static UnCatchHandler instance;
    Context context;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private UnCatchHandler(Context context) {
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.context = context;
    }

    public static void getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new UnCatchHandler(context);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (ex != null) {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(context, "很抱歉,程序出现异常,即将退出.",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }.start();
            catchExceptionAndSave(ex);
            StatisticsReport.saveInfo();
        }
        if (mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    private void catchExceptionAndSave(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        statisticsInfo.setE_t(StatisticsConstant.E_T_EXCEPTION);
        statisticsInfo.setE_d(sb.toString().replace("\n", "-->")
                .replace("\t", ""));
        StatisticsReport.reportCollectInfo(statisticsInfo);
    }

}
