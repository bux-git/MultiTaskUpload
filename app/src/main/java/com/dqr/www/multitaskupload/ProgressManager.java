package com.dqr.www.multitaskupload;

import com.dqr.www.multitaskupload.bean.ProgressBean;

import java.util.List;

/**
 * Description：进度数据管理单例
 * Author：LiuYM
 * Date： 2017-05-23 20:07
 */

public class ProgressManager {

    private static ProgressManager mProgressManager;
    private List<ProgressBean> mList;

    private ProgressManager() {

    }

    public static ProgressManager getInstance() {
        if (mProgressManager == null) {
            mProgressManager = new ProgressManager();
        }
        return mProgressManager;
    }

    public static ProgressManager getmProgressManager() {
        return mProgressManager;
    }

    public static void setmProgressManager(ProgressManager mProgressManager) {
        ProgressManager.mProgressManager = mProgressManager;
    }

    public List<ProgressBean> getList() {
        return mList;
    }

    public void setList(List<ProgressBean> list) {
        mList = list;
    }

    public static void clearManagerData(){
        mProgressManager=null;
    }
}
