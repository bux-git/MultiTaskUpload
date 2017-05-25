package com.dqr.www.multitaskupload;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dqr.www.multitaskupload.bean.ProgressBean;
import com.dqr.www.multitaskupload.bean.UploadTaskBean;
import com.dqr.www.multitaskupload.database.EAlbumDB;
import com.dqr.www.multitaskupload.service.EAlbumUploadService;
import com.dqr.www.multitaskupload.util.FileUtils;
import com.dqr.www.multitaskupload.util.NetReceiver;
import com.dqr.www.multitaskupload.util.NetUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int COUNT = 10000;
    private static final int DW = 1024;
    private List<ProgressBean> mList;

    private EAlbumDB mEAlbumDB;
    private NetReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化网络
        Constant.IS_CONNECTED = NetUtils.isNetworkConnected(this);
        Constant.NET_STATE_TYPE=NetUtils.getConnectedType(this);

        mList = new ArrayList<>();
        Button btnSee = (Button) findViewById(R.id.btn_see);
        Button add = (Button) findViewById(R.id.btn_add);
        Button delete = (Button) findViewById(R.id.btn_delete);
        Button query = (Button) findViewById(R.id.btn_query);

        btnSee.setOnClickListener(this);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        query.setOnClickListener(this);

        mEAlbumDB = EAlbumDB.getInstance(this);

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
                    UploadTaskBean up=(UploadTaskBean) list.get(i);
                    Log.d(TAG, "onClick:Id " +up.getId()+
                            "   " +up.getFilePath()+
                            "   " +up.getMd5()+
                            "   " +up.getFileTime()+
                            "   " +up.getFileSize());
                }
                Log.d(TAG, "onClick: "+list.size());
                break;
            case R.id.btn_select:
                getUploadTaskBean();
                break;
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
                    mList.add(taskBean);

                }
                mCursor.close();

               EAlbumUploadService.startAddUploadTask(MainActivity.this, mList);

            }
        }).start();
    }

}
