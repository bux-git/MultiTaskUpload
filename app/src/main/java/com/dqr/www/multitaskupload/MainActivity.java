package com.dqr.www.multitaskupload;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dqr.www.multitaskupload.bean.ProgressBean;
import com.dqr.www.multitaskupload.bean.UploadTaskBean;
import com.dqr.www.multitaskupload.database.EAlbumDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int COUNT = 10000;
    private static final int DW = 1024;
    private List<ProgressBean> mList;

    private EAlbumDB mEAlbumDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();

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

    public void getData() {
        mList = new ArrayList<>();
        for (int i = COUNT; i >= 0; i--) {
            ProgressBean p = new ProgressBean();
            p.setTitle("上传到《相册" + i + "》");
            p.setDesc("排队中...");

            p.setUploadedSize(1);
            p.setTotalSize(i % 6 * COUNT * DW);

            p.setImgPath("http://test.dqr2015.cn:8888/uploadFiles/7852/small_94d70ea99e564649808a479f96dec671.jpg");
            mList.add(p);
        }

    }

    public void updateData() {

       new Timer().schedule(new TimerTask() {
           @Override
           public void run() {
               for (int i = 0; i < mList.size(); i++) {
                   ProgressBean p = mList.get(i);
                   //测试数据
                   p.setUploadedSize(p.getUploadedSize() + 100 * DW);
                   if (p.getUploadedSize() > p.getTotalSize()) {
                       mList.remove(i);
                   }
               }
           }
       },100,1000);

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_see:
                ProgressManager.getInstance().setList(mList);
                EAlbumUploadProgressActivity.start(MainActivity.this);
                updateData();
                break;
            case R.id.btn_add:
                UploadTaskBean taskBean = new UploadTaskBean(11l,0l,"","",11l,"","",1,"1");
                mEAlbumDB.saveUploadTask(taskBean);
                break;
            case R.id.btn_delete:
                //mAlbumDBHelper.getWritableDatabase();
                break;
            case R.id.btn_query:
                List<UploadTaskBean> list = mEAlbumDB.getUploadTaskBean();
                Log.d(TAG, "onClick: "+list.size());
                break;
        }
    }
}
