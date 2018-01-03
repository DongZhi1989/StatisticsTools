package com.dong.statistics.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * @author <dr_dong>
 * @time 2017/3/31 12:00
 */
public class SysUtils {

    public static final String TAG = SysUtils.class.getSimpleName();

    public static boolean isNetworkConnected(@NonNull Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
        }
        return false;
    }

    public static boolean isWifiConnected(@NonNull Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mWiFiNetworkInfo != null) {
            if (mWiFiNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return mWiFiNetworkInfo.isAvailable() && mWiFiNetworkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * 获取手机ip地址
     *
     * @return
     */
    public static String getIpAddressString() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取系统名称
     *
     * @return
     */
    public static String getOSName() {
        return "android_" + Build.BRAND;
    }

    /**
     * 获取系统版本
     *
     * @return
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备厂商
     *
     * @return
     */
    public static String getSysManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备型号
     *
     * @return
     */
    public static String getSysModel() {
        return Build.MODEL;
    }

    private static Location getLocation(Context context) {
        //获取地理位置管理器
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        String locationProvider;
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            return null;
        }
        //获取Location
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return locationManager.getLastKnownLocation(locationProvider);
    }

    /**
     * 获取维度
     *
     * @return
     */
    public static String getLat(Context context) {
        Location location = getLocation(context);
        if (location != null) {
            //不为空,显示地理位置经纬度
            return location.getLatitude() + "";
        } else {
            return "unknown";
        }
    }

    /**
     * 获取经度
     *
     * @return
     */
    public static String getLon(Context context) {
        Location location = getLocation(context);
        if (location != null) {
            //不为空,显示地理位置经纬度
            return location.getLongitude() + "";
        } else {
            return "unknown";
        }
    }

    /**
     * 获取网络状态
     *
     * @return
     */
    public static String getNetType(Context context) {
        String netType = "";
        NetworkInfo networkInfo = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String strSubTypeName = networkInfo.getSubtypeName();
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = "4G";
                        break;
                    default:
                        if ("TD-SCDMA".equalsIgnoreCase(strSubTypeName)
                                || "WCDMA".equalsIgnoreCase(strSubTypeName)
                                || "CDMA2000".equalsIgnoreCase(strSubTypeName)) {
                            netType = "3G";
                        } else {
                            netType = strSubTypeName;
                        }
                        break;
                }
            }
        }

        return netType;
    }

}
