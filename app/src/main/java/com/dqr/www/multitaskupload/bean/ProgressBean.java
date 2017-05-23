package com.dqr.www.multitaskupload.bean;

import java.io.Serializable;

/**
 * Description：进度详情
 * Author：LiuYM
 * Date： 2017-05-23 15:26
 */

public class ProgressBean implements Serializable{

    public static final int MAX_PROGRESS=1000;

    /**
     * 显示名称 如 上传照片到《默默相册》
     */
    private String title;
    /**
     * 排队说明 如  排队中...
     */
    private String desc;
    /**
     * 已经上传的大小单位byte
     */
    private int uploadedSize;
    /**
     * 总大小byte
     */
    private int totalSize;

    /**
     * 缩略图地址
     */
    private String imgPath;

    private boolean isSuccess;//是否上传成功
    private boolean  isFail;//是否上传失败


    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public boolean isFail() {
        return isFail;
    }

    public void setFail(boolean fail) {
        isFail = fail;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getUploadedSize() {
        return uploadedSize;
    }

    public void setUploadedSize(int uploadedSize) {
        this.uploadedSize = uploadedSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    /**
     * 获取进度int值
     *
     * @return
     */
    public int getProgress() {
        if (uploadedSize > 0 && totalSize > 0) {
            return (int) ((uploadedSize*1.0 / totalSize)*MAX_PROGRESS);
        } else {
            return 0;
        }
    }


    /**
     * 根据size获取响应单位
     *
     * @param size
     * @return
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1fGB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0fMB" : "%.1fMB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0fKB" : "%.1fKB", f);
        } else
            return String.format("%dB", size);
    }
}
