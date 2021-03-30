package com.jimu.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Ljh on 2021/3/25.
 * Description:
 */
public class NetUtil {
    private static final String TAG = "NetUtil";
    /**
     * 获取外网的 IP（要访问 Url，要放到后台线程里处理）
     */
    public static String getNetIp() {
        URL infoUrl;
        InputStream inStream = null;
        String ipLine = "";
        HttpURLConnection httpConnection = null;
        try {
            infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
            URLConnection connection = infoUrl.openConnection();
            httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, StandardCharsets.UTF_8));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    stringBuilder.append(line).append("\n");
                Pattern pattern = Pattern
                        .compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                Matcher matcher = pattern.matcher(stringBuilder.toString());
                if (matcher.find()) {
                    ipLine = matcher.group();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inStream != null)
                    inStream.close();
                if (httpConnection != null) httpConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ipLine;
    }

    public static boolean pingNet() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            try {
                throw new Exception("pingNet should not use in MainThread!!!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String result = null;
        String[] ipArray = new String[]{
                "www.meizu.com",
                "www.baidu.com",
                "www.qq.com"
        };
        for (int i = 0; i < 3; i++) {
            try {
                Process p = Runtime.getRuntime().exec("ping -c 3 -w 5 " + ipArray[i]);// ping 网址 3 次
                // ping的状态
                int status = p.waitFor();
                if (status == 0) {
                    result = "success";
                    return true;
                } else {
                    result = "failed";
                    Thread.sleep(1000);
                }
            } catch (IOException e) {
                result = "IOException";
            } catch (InterruptedException e) {
                result = "InterruptedException";
            } finally {
                Log.e(TAG, "pingNet: " + ipArray[i] + ":result = " + result);
            }
        }
        return false;
    }

    /**
     * 获取 Ip 地址
     */
    public static String getIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
            if (wifiManager == null) return null;
            DhcpInfo di = wifiManager.getDhcpInfo();
            return intToIp(di.ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * int ip 地址格式化
     */
    public static String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }



    public static int getWifiSignalStrength(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();// 当前 WLAN 连接信息
        if (wifiInfo != null && wifiInfo.getIpAddress() != 0)
            return WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 4);
        else
            return 0;
    }


    /**
     * 网络是否已连接
     */
    public static boolean isNetworkConnect(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
            return networkinfo != null && networkinfo.isConnected() && networkinfo.isAvailable();
        }
        return false;
    }

    //static String getSSid() {
    //    WifiManager wifiManager = (WifiManager) FridgeApplication.instance.getApplicationContext().getSystemService(WIFI_SERVICE);
    //    if (wifiManager == null) return null;
    //    WifiInfo info = wifiManager.getConnectionInfo();
    //    return info != null ? info.getSSID() : null;
    //}

    /**
     * 获取 mac 地址
     */
    public static String getMac() {
        String mac = "";
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    mac = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return mac;
    }
}

