package com.dqr.www.multitaskupload.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dqr.www.multitaskupload.Constant;
import com.dqr.www.multitaskupload.base.BaseModel;
import com.dqr.www.multitaskupload.base.ProgressListener;
import com.dqr.www.multitaskupload.base.ProgressRequestBody;
import com.dqr.www.multitaskupload.base.UploadBaseCallBack;
import com.dqr.www.multitaskupload.bean.ImageBean;
import com.dqr.www.multitaskupload.bean.ProgressBean;
import com.dqr.www.multitaskupload.bean.UploadTaskBean;
import com.dqr.www.multitaskupload.database.EAlbumDB;
import com.dqr.www.multitaskupload.util.NetUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Description：照片上传服务
 * Author：LiuYM
 * Date： 2017-05-24 15:34
 */

public class EAlbumUploadService extends Service {
    private static final String TAG = "EAlbumUploadService";


    private static final int MESSAGE_NOTIFY_ADD_UPLOAD = 0x11;

    private boolean isStop;//服务是否已经停止
    private EAlbumDB mEAlbumDB;//数据库操作

    private static final int MAX_UPLOAD_SIZE = 10;//同时最大上传队列
    private static final int MAX_SIZE = 1024 * 1024;//分块大小默认1M

    public static List<ProgressBean> sTaskBeen = new ArrayList<>();//上传任务集合
    private List<ProgressBean> waitUploadQueue;//待上传集合
    private List<ProgressBean> uploadQueue;//正在上传集合

    private OkHttpClient mClient;
    private String url = "http://test.dqr2015.cn:8888/EarthMan/newAlbums/bpuploadPhoto/110/token";

    // private String url = "http://192.168.0.74:8080/EarthMan/newAlbums/bpuploadPhoto/110/token";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /*if(!Constant.IS_CONNECTED){
            stopSelf();//无网络不允许上传操作
        }*/
        //获取SQL操作实例
        mEAlbumDB = EAlbumDB.getInstance(this);
        isStop = false;
        //初始化上传相关数据集合
        sTaskBeen.clear();
        sTaskBeen.addAll(mEAlbumDB.getUploadTaskBean());
        waitUploadQueue = new ArrayList<>();
        waitUploadQueue.addAll(sTaskBeen);

        uploadQueue = new ArrayList<>();


        initUploadTaskQueue();

        //初始化网络请求相关参数
        initOKHttp();
    }


    /**
     * 启动上传任务 如果数据库中有保存上传任务 则开始上传
     *
     * @param context
     */
    public static void startUploadTask(Context context) {
        Intent intent = new Intent(context, EAlbumUploadService.class);
        context.startService(intent);
    }

    /**
     * 添加单个任务到上传队列
     *
     * @param uploadTaskBean
     */
    public static void startAddUploadTask(Context context, ProgressBean uploadTaskBean) {
        Intent intent = new Intent(context, EAlbumUploadService.class);
        intent.putExtra("addSingleTask", uploadTaskBean);
        context.startService(intent);
    }

    /**
     * 批量添加任务到上传队列
     *
     * @param context
     * @param uploadTaskBeans
     */
    public static void startAddUploadTask(Context context, List<ProgressBean> uploadTaskBeans) {
        Intent intent = new Intent(context, EAlbumUploadService.class);
        intent.putExtra("addTasks", (Serializable) uploadTaskBeans);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //当总集合中有数据 待上传没数据时  说明总集合中是失败数据
        // 将失败数据添加到 待上传集合末尾中 继续上传
        boolean isHasFailData = false;
        List<ProgressBean> failList = null;
        if (sTaskBeen.size() > 0 && waitUploadQueue.size() == 0) {
            isHasFailData = true;
            failList = new ArrayList<>();
            failList.addAll(sTaskBeen);
        }

        UploadTaskBean taskBean = (UploadTaskBean) intent.getSerializableExtra("addSingleTask");
        List<ProgressBean> uploadTaskBeans = (List<ProgressBean>) intent.getSerializableExtra("addTasks");
        if (taskBean != null) {
            addUploadTaskToSqlAndQueue(taskBean);
        } else if (uploadTaskBeans != null) {

            for (int i = 0; i < uploadTaskBeans.size(); i++) {
                ProgressBean bean = uploadTaskBeans.get(i);
                addUploadTaskToSqlAndQueue((UploadTaskBean) bean);
                Log.d(TAG, "service run: " + i);
            }

        }
        if (isHasFailData) {//将失败数据添加到队列末尾
            waitUploadQueue.addAll(failList);
        }

        return super.onStartCommand(intent, flags, startId);
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //添加请求
            if (msg.what == MESSAGE_NOTIFY_ADD_UPLOAD) {
                if (waitUploadQueue.size() > 0) {
                    if (uploadQueue.size() < MAX_UPLOAD_SIZE) {
                        Log.d(TAG, "handleMessage: waitUploadQueue:" + waitUploadQueue.size() + "   uploadQueue:" + uploadQueue.size());
                        UploadTaskBean taskBean = (UploadTaskBean) waitUploadQueue.get(0);
                        taskBean.setFail(false);
                        taskBean.setDesc("排队中...");
                        uploadQueue.add(taskBean);
                        waitUploadQueue.remove(0);
                        addSingleUploadTask(taskBean);
                    }
                }
            }
        }
    };

    /**
     * 开启待上传 上传  数据 无限循环控制 上传请求队列
     */
    private void initUploadTaskQueue() {
        Log.d(TAG, "initUploadTaskQueue: ");
        //同一时间只允许上传MAX_UPLOAD_SIZE
        //上传队列有数目变动后 往里面添加数据 直到 待上传数据 waitUploadQueue 完全操作完
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    if (!NetUtils.isNetworkConnected(EAlbumUploadService.this)) {//网络断开时  直接取消所有上传操作
                        stopSelf();
                        return;
                    } else {//有网络判断网络类型
                        int netStateType = NetUtils.getConnectedType(EAlbumUploadService.this);
                        if (netStateType == ConnectivityManager.TYPE_MOBILE) {//手机网络
                            if (!Constant.MOBILE_UPLOAD) {//3g/4g不上传
                                //取消正在上传的任务
                                for (int i = 0; i < uploadQueue.size(); i++) {
                                    uploadQueue.get(i).getCall().cancel();
                                }
                                uploadQueue.clear();
                                stopSelf();
                                return;
                            }
                        } else if (netStateType == ConnectivityManager.TYPE_WIFI) {

                        } else {//其他停止服务
                            stopSelf();
                            return;
                        }
                    }
                    mHandler.sendEmptyMessage(MESSAGE_NOTIFY_ADD_UPLOAD);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();


    }


    /**
     * 添加一个任务到 数据库  (总集合 待上传集合 )
     *
     * @param uploadTaskBean
     */
    private void addUploadTaskToSqlAndQueue(UploadTaskBean uploadTaskBean) {
        int id = (int) mEAlbumDB.saveUploadTask(uploadTaskBean);
        if (id > 0) {//写入数据库成功后才进行其他操作
            uploadTaskBean.setId(id);
            sTaskBeen.add(uploadTaskBean);
            waitUploadQueue.add(uploadTaskBean);
            return;
        }
        Log.d(TAG, "addUploadTaskToSql: 添加数据库失败:" + uploadTaskBean.getFilePath());
    }


    /**
     * 初始化网络请求相关参数
     */
    private void initOKHttp() {
        mClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 添加一个任务 到请求队列
     *
     * @param up
     */
    private void addSingleUploadTask(final UploadTaskBean up) {
        final File file = new File(up.getFilePath());

        if (!file.exists()) {//文件不存在
            successCheckRemove(up, null);
            return;
        }
        //表示开始上传
        up.setUploadedSize(1);
        up.setTotalSize(up.getFileSize());

        final MultipartBody.Builder builder = getPartBodyBuilder(up, file);
        builder.addFormDataPart("startPos","0");

        final Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(builder.build());

        //先进行判断文件是否已经存在服务器
        // 存在直接上传成功 不存在或者请求失败 继续上传文件
        mClient.newCall(requestBuilder.build())
                .enqueue(new UploadBaseCallBack<JSONObject>() {
                    @Override
                    protected void onSuccess(final JSONObject result) {
                        if (result != null) {//文件存在 直接上传成功
                            Log.d(TAG, "服务器已经存在 :" + up.getFilePath());

                            up.setUploadedSize(up.getFileSize());
                            up.setTotalSize(up.getFileSize());
                            successCheckRemove(up, result);
                            //延迟发送方便进度察看中 有一段停留时间
                            return;
                        }
                        Log.d(TAG, "服务器不存在 onSuccess: ");
                        uploadFile(up, file);
                    }

                    @Override
                    protected void onFail(String msg) {
                        uploadFile(up, file);
                    }
                });


    }

    @NonNull
    private MultipartBody.Builder getPartBodyBuilder(UploadTaskBean up, File file) {
        final MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("size", String.valueOf(up.getFileSize()))
                .addFormDataPart("name", file.getName())
                .addFormDataPart("md5", up.getMd5())
                .addFormDataPart("fileTime", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(up.getFileTime())))
                .addFormDataPart("fileAddr", up.getFileAddr())
                .addFormDataPart("fileAttribute", up.getFileAttribute())
                .addFormDataPart("source", String.valueOf(up.getSource()))
                .addFormDataPart("type", String.valueOf(up.getType()));
        if (up.getAlbumId() > 0) {
            builder.addFormDataPart("albumId", String.valueOf(up.getAlbumId()));
        }
        return builder;
    }

    /**
     * 上传文件
     *
     * @param up
     */
    private void uploadFile(final UploadTaskBean up, final File file) {
        Log.d(TAG, "uploadFile: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                RandomAccessFile randomAccessFile = null;
                try {
                    int len = -1;//记录读取大小

                    randomAccessFile = new RandomAccessFile(file, "r");
                    randomAccessFile.seek(up.getStartPos());
                    byte[] buffer = new byte[MAX_SIZE];
                    boolean isStopWhile = false;//是否停止while
                    //获取分块
                    while ((len = randomAccessFile.read(buffer)) != -1 && (!isStopWhile)) {

                        MultipartBody.Builder builder = getPartBodyBuilder(up, file);
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), buffer, 0, len);
                        builder.addFormDataPart("file", file.getName(), requestBody)
                                .addFormDataPart("startPos", String.valueOf(up.getStartPos()));


                        final Request request = new Request.Builder()
                                .url(url)
                                .post(new ProgressRequestBody(builder.build(), new ProgressListener() {
                                    @Override
                                    public void progress(long bytesRead, long contentLength, boolean done) {
                                        up.setUploadedSize(up.getUploadedSize() + bytesRead);
                                        Log.d(TAG, "progress: bytesRead:" + bytesRead + "   contentLength:" + contentLength + "  fileSize:" + up.getFileSize() + " done:" + done);
                                    }
                                })).build();


                        Call call = mClient.newCall(request);
                        up.setCall(call);
                        Response response = call.execute();
                        String responseStr = response.body().source().readString(Charset.forName("UTF-8"));
                        Log.d(TAG, "上传文件:path:"+up.getFilePath()+"  code:" + response.code() + "   response:" + responseStr);

                        if (response.code() == 200) {//成功
                            BaseModel<ImageBean> baseModel = JSON.parseObject(responseStr,new TypeReference<BaseModel<ImageBean>>(){});
                            switch (baseModel.getCode()) {
                                case "000004"://重新上传整个文件
                                    isStopWhile = true;
                                    //设置其实位置为0
                                    mEAlbumDB.updateTaskStartPosById(up.getId(), 0);
                                    failCheckRemove(up);
                                    break;
                                case "000001"://重新上传分片
                                    isStopWhile = true;
                                    failCheckRemove(up);
                                    break;
                                case "000000":
                                    if (baseModel.getResult() == null) {//分块上传成功
                                        up.setStartPos(up.getStartPos() + len);
                                        mEAlbumDB.updateTaskStartPosById(up.getId(), up.getStartPos() + len);
                                    } else {//文件上传成功
                                        isStopWhile = true;
                                        successCheckRemove(up, null);
                                    }
                                    break;

                            }

                        } else {//失败
                            failCheckRemove(up);
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();

    }

    /**
     * 上传失败 处理
     *
     * @param up
     */
    private void failCheckRemove(UploadTaskBean up) {
        up.setFail(true);
        //移除上传队列任务 不移除 数据库 和总集合 下次继续上传
        uploadQueue.remove(up);
        //失败移除到末尾
        sTaskBeen.remove(up);
        sTaskBeen.add(up);
        stopService();
    }


    /**
     * 文件上传成功时 处理
     *
     * @param up
     */
    private void successCheckRemove(UploadTaskBean up, JSONObject result) {
        //result !=null
        Log.d(TAG, "onSuccess: " + result);
        //移除任务
        mEAlbumDB.deleteUploadTaskById(up.getId());
        sTaskBeen.remove(up);
        uploadQueue.remove(up);
        stopService();
    }

    /**
     * 停止服务
     */
    private void stopService() {
        if (waitUploadQueue.size() == 0) {
            stopSelf();
            isStop = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
