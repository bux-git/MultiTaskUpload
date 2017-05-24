package com.dqr.www.multitaskupload;

import com.dqr.www.multitaskupload.bean.ProgressBean;

import java.util.List;

/**
 * Description：进度数据管理单例
 * Author：LiuYM
 * Date： 2017-05-23 20:07
 */

public class ProgressManager {

    private static ProgressManager sProgressManager;
    private List<ProgressBean> mList;

    private ProgressManager() {

    }

    public synchronized static ProgressManager getInstance() {
        if (sProgressManager == null) {
            sProgressManager = new ProgressManager();
        }
        return sProgressManager;
    }




    public List<ProgressBean> getList() {
        return mList;
    }

    public void setList(List<ProgressBean> list) {
        mList = list;
    }

    public static void clearManagerData(){
        sProgressManager=null;
    }
}
