package com.dqr.www.multitaskupload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.dqr.www.multitaskupload.adapter.EAlbumProgressAdapter;
import com.dqr.www.multitaskupload.bean.ProgressBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Description：上传详情显示页面
 * Author：LiuYM
 * Date： 2017-05-23 15:20
 */

public class EAlbumUploadProgressActivity extends AppCompatActivity {

    private static final int NOTIFYDATA_CHANGED = 0x01;

    private Timer timer;// 记时器
    private ChangeListViewTask task;// 进度轮询任务
    private boolean isRefresh=true;//是否刷新 滑动时不刷新


    private RecyclerView mRecyclerView;
    private EAlbumProgressAdapter mAdapter;
    private List<ProgressBean> mList;


    public static void start(Context context){
        Intent intent=new Intent(context,EAlbumUploadProgressActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ealbum_upload_progress_list_activity);

        mList =ProgressManager.getInstance().getList();

        if(mList==null) mList = new ArrayList<>();
        mAdapter = new EAlbumProgressAdapter(mList);


        mRecyclerView = (RecyclerView) findViewById(R.id.rl_content);
        //取消刷新动画
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
         mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isRefresh = true;
                } else {
                    isRefresh = false;
                }
            }
        });

    }

    @Override
    public void onResume() {
        startRefresh();
        super.onResume();
    }

    @Override
    public void onPause() {
        stopRefresh();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // 定时刷新上传任务显示数据
    private class ChangeListViewTask extends TimerTask {
        @Override
        public void run() {
            if (isRefresh) {
                mHandler.obtainMessage(NOTIFYDATA_CHANGED).sendToTarget();

            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NOTIFYDATA_CHANGED:
                    if (mList.size() == 0) {
                        finish();
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * 开始刷新
     */
    private void startRefresh() {
        if (timer == null) {
            timer = new Timer();// 初始化计时器
        }
        if (task == null) {
            task = new ChangeListViewTask();
            timer.schedule(task, 100, 1000);
        }
    }

    /**
     * 停止刷新
     */
    private void stopRefresh() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}
