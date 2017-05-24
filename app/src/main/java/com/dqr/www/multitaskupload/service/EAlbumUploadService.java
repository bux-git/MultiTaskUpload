package com.dqr.www.multitaskupload.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dqr.www.multitaskupload.bean.ProgressBean;
import com.dqr.www.multitaskupload.bean.UploadTaskBean;
import com.dqr.www.multitaskupload.database.EAlbumDB;

import java.io.Serializable;
import java.util.List;

/**
 * Description：照片上传服务
 * Author：LiuYM
 * Date： 2017-05-24 15:34
 */

public class EAlbumUploadService extends Service {
    private static final String TAG = "EAlbumUploadService";
    public static List<ProgressBean> sTaskBeen;//上传任务集合
    private EAlbumDB mEAlbumDB;//数据库操作
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //获取SQL操作实例
        mEAlbumDB = EAlbumDB.getInstance(this);
        //获取所有上传任务数据
        sTaskBeen= mEAlbumDB.getUploadTaskBean();
        initUploadTaskQueue();
    }

    /**
     * 添加单个任务到上传队列
     * @param uploadTaskBean
     */
    public static void startAddUploadTask(Context context,ProgressBean uploadTaskBean){
        Intent intent = new Intent(context,EAlbumUploadService.class);
        intent.putExtra("addSingleTask", uploadTaskBean);
        context.startService(intent);
    }

    /**
     * 批量添加任务到上传队列
     * @param context
     * @param uploadTaskBeans
     */
    public static void startAddUploadTask(Context context,List<ProgressBean> uploadTaskBeans){
        Intent intent = new Intent(context,EAlbumUploadService.class);
        intent.putExtra("addTasks", (Serializable) uploadTaskBeans);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UploadTaskBean taskBean = (UploadTaskBean) intent.getSerializableExtra("addSingleTask");
        if(taskBean!=null){
            addUploadTaskToSqlAndQueue(taskBean);

        }else{
            List<ProgressBean> uploadTaskBeans = (List<ProgressBean>) intent.getSerializableExtra("addTasks");
            if(uploadTaskBeans!=null) {
                for (int i = 0; i < uploadTaskBeans.size(); i++) {
                    ProgressBean bean = uploadTaskBeans.get(i);
                    addUploadTaskToSqlAndQueue((UploadTaskBean) bean);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化上传请求队列
     */
    private void initUploadTaskQueue(){
        Log.d(TAG, "initUploadTaskQueue: ");
        for(int i=0;i<sTaskBeen.size();i++){
            addSingleUploadTask((UploadTaskBean) sTaskBeen.get(i));
        }
    }

    /**
     * 添加一个任务 到请求队列
     * @param uploadTaskBean
     */
    private void addSingleUploadTask(UploadTaskBean uploadTaskBean){
        Log.d(TAG, "addSingleUploadTask: ");
    }

    /**
     * 添加一个任务到数据库 并添加请求操作
     * @param uploadTaskBean
     */
    private void addUploadTaskToSqlAndQueue(UploadTaskBean uploadTaskBean){

        int id=(int) mEAlbumDB.saveUploadTask(uploadTaskBean);
        if(id>0){
            uploadTaskBean.setId(id);
            sTaskBeen.add(uploadTaskBean);
            addSingleUploadTask(uploadTaskBean);
            return;
        }
        Log.d(TAG, "addUploadTaskToSql: 添加数据库失败:"+uploadTaskBean.getFilePath());


    }
}
