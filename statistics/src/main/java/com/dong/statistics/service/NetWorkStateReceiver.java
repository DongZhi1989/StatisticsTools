package com.dong.statistics.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author <dr_dong>
 *         Time : 2017/12/18 16:43
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            UploadPolicy.setNowWeakNetFlag(!wifiNetworkInfo.isConnected());
        } else {
            //API大于23时使用下面的方式进行网络监听
            NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                UploadPolicy.setNowWeakNetFlag(false);
            } else {
                UploadPolicy.setNowWeakNetFlag(true);
            }
        }
    }
}
