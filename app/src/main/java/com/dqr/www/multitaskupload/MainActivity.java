package com.dqr.www.multitaskupload;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dqr.www.multitaskupload.bean.ProgressBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int COUNT = 1000;
    private static final int DW = 1024;

    private TextView mTvSee;

    private List<ProgressBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();

        mTvSee = (TextView) findViewById(R.id.tv_see);
        mTvSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressManager.getInstance().setList(mList);
                EAlbumUploadProgressActivity.start(MainActivity.this);
                updateData();
            }
        });
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


}
