package com.dqr.www.multitaskupload;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dqr.www.multitaskupload.bean.ImageBean;
import com.dqr.www.multitaskupload.bean.ProgressBean;
import com.dqr.www.multitaskupload.bean.UploadTaskBean;
import com.dqr.www.multitaskupload.database.EAlbumDB;
import com.dqr.www.multitaskupload.service.EAlbumUploadService;
import com.dqr.www.multitaskupload.util.FileUtils;
import com.dqr.www.multitaskupload.util.NetReceiver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int COUNT = 10000;
    private static final int DW = 1024;
    private static final int REQUEST_CODE = 123;

    private EAlbumDB mEAlbumDB;
    private NetReceiver mReceiver;

    private BroadcastReceiver mUploadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.UPLOAD_SERVICE_ACTION.equals(action)) {//照片上传广播
                Log.d(TAG, "onReceive: ");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnSee = (Button) findViewById(R.id.btn_see);
        Button add = (Button) findViewById(R.id.btn_add);
        Button delete = (Button) findViewById(R.id.btn_delete);
        Button query = (Button) findViewById(R.id.btn_query);

        btnSee.setOnClickListener(this);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        query.setOnClickListener(this);

        mEAlbumDB = EAlbumDB.getInstance(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(mUploadReceiver, new IntentFilter(Constant.UPLOAD_SERVICE_ACTION));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUploadReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_see:
                ProgressManager.getInstance().setList(EAlbumUploadService.sTaskBeen);
                EAlbumUploadProgressActivity.start(MainActivity.this);
                break;
            case R.id.btn_add:

                EAlbumUploadService.startUploadTask(this);
                break;
            case R.id.btn_list:

                break;
            case R.id.btn_delete:
                mEAlbumDB.deleteUploadTaskAll();
                break;
            case R.id.btn_query:
                List<ProgressBean> list = mEAlbumDB.getUploadTaskBean();
                for (int i = 0; i < list.size(); i++) {
                    UploadTaskBean up = (UploadTaskBean) list.get(i);
                    Log.d(TAG, "onClick:Id " + up.getId() +
                            "   " + up.getFilePath() +
                            "   " + up.getMd5() +
                            "   " + up.getFileTime() +
                            "       " + up.getFileSize() +
                            "   getStartPos:" + up.getStartPos()
                    );
                }
                Log.d(TAG, "onClick: " + list.size());
                break;
            case R.id.btn_select:
                int hasWriteContactsPermission = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                    return;
                } else {
                    getUploadTaskBean();
                }
                break;
            case R.id.btn_add_single:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<ImageBean> beanList = getImageList();
                        System.out.println("开始插入数据:" + System.currentTimeMillis());
                        for (int i = 0; i < beanList.size(); i++) {
                            ImageBean bean = beanList.get(i);
                            mEAlbumDB.saveUploadImage(bean);
                        }
                        System.out.println("插入结束:" + System.currentTimeMillis());
                    }
                }).start();

                break;
            case R.id.btn_add_select:
                System.out.println("开始查询数据:" + System.currentTimeMillis());
                List<ImageBean> beans = mEAlbumDB.getAllImageBean();
                System.out.println("查询结束:" + System.currentTimeMillis());
                for (int i = 0; i < beans.size(); i++) {
                    ImageBean bean = beans.get(i);
                    Log.d(TAG, "onClick: " + i + "  Image:" + bean.getId());
                }
                break;
            case R.id.btn_add_multi:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<ImageBean> beanList = getImageList();
                        long start=System.currentTimeMillis();
                        System.out.println("开始插入数据:" +start );
                        mEAlbumDB.saveUploadImage(beanList);
                        long end=System.currentTimeMillis();
                        System.out.println("插入结束:" +end+"   total:"+(end-start)/1000 );
                    }
                }).start();

                break;
            case R.id.btn_add_delete:
                System.out.println("开始删除数据:" + System.currentTimeMillis());
                mEAlbumDB.deleteAllImage();
                System.out.println("删除结束:" + System.currentTimeMillis());
                break;

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意使用write
                getUploadTaskBean();
            } else {
                //用户不同意，自行处理即可
                Toast.makeText(this, "无权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getUploadTaskBean() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = getContentResolver();
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                Log.e(TAG, mCursor.getCount() + "");
                List<ProgressBean> list = new ArrayList<ProgressBean>();
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File file = new File(path);
                    UploadTaskBean taskBean = new UploadTaskBean(file.length()
                            , 0
                            , FileUtils.getFileMD5(file)
                            , path
                            , file.lastModified()
                            , "湖南 长沙"
                            , "{\"lng\":28,\"lat\":113}"
                            , 0
                            , "测试");
                    EAlbumUploadService.startAddUploadTask(MainActivity.this, taskBean);
                }
                mCursor.close();
                Log.d(TAG, "run: ");


            }
        }).start();
    }

    public List<ImageBean> getImageList() {
        List<ImageBean> beanList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            ImageBean bean = new ImageBean();
            bean.setId(i);
            bean.setUserId(Constant.userId);
            bean.setImg("http://test.dqr2015.cn:8888/uploadFiles/201706/7852/97deaae975354cfb9c2cfdb924b73a8f.jpg");
            bean.setFileName("IMG_20170607_173632_BURST30.jpg");
            bean.setSmallimg("http://test.dqr2015.cn:8888/uploadFiles/201706/7852/small_97deaae975354cfb9c2cfdb924b73a8f.jpg");
            bean.setType(1);
            bean.setHashMd5("998ba3c4db7f35486fe7459b240cfcae");
            bean.setFileTime(1496784996000L);
            bean.setFileAddr("湖南 长沙");
            bean.setFileSize(2518483);
            bean.setFileAttribute("{\"lng\":28,\"lat\":113}");
            bean.setStatus(1);
            bean.setSource(1);
            bean.setCreatedAt(1496920685000L);
            bean.setUpdatedAt(1496920685000L);
            bean.setUpImg("");
            bean.setImg_edit("/uploadFiles/201706/7852/97deaae975354cfb9c2cfdb924b73a8f.jpg");
            bean.setSmallimg_edit("/uploadFiles/201706/7852/small_97deaae975354cfb9c2cfdb924b73a8f.jpg");
            beanList.add(bean);
        }
        return beanList;
    }
}
