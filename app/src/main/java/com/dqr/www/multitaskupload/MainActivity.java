package com.dqr.www.multitaskupload;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dqr.www.multitaskupload.bean.ProgressBean;
import com.dqr.www.multitaskupload.database.EAlbumDBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int COUNT = 10000;
    private static final int DW = 1024;

    private Button mBtnSee;
    private Button mBtnCreate;
    private List<ProgressBean> mList;


    private EAlbumDBHelper mAlbumDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();

        mBtnSee = (Button) findViewById(R.id.btn_see);
        mBtnCreate = (Button) findViewById(R.id.btn_create);
        mBtnSee.setOnClickListener(this);
        mBtnCreate.setOnClickListener(this);

        mAlbumDBHelper = new EAlbumDBHelper(this,"EAlbumDB.db",null,1);

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
            case R.id.btn_create:
                mAlbumDBHelper.getWritableDatabase();
                break;
        }
    }
}
