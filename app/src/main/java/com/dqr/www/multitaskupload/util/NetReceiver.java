package com.dqr.www.multitaskupload.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.dqr.www.multitaskupload.service.EAlbumUploadService;

/**
 * Description：
 * Author：LiuYM
 * Date： 2017-05-25 14:14
 */

public class NetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            boolean isConnected = NetUtils.isNetworkConnected(context);
            System.out.println("网络状态：" + isConnected);
            System.out.println("wifi状态：" + NetUtils.isWifiConnected(context));
            System.out.println("移动网络状态：" + NetUtils.isMobileConnected(context));
            System.out.println("网络连接类型：" + NetUtils.getConnectedType(context));


            if (isConnected) {//有网络时 开启照片上传服务
                EAlbumUploadService.startUploadTask(context);
            }

        }
    }
}
