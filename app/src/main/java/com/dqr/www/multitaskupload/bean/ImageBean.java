package com.dqr.www.multitaskupload.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description：
 * Author：LiuYM
 * Date： 2017-05-26 17:57
 */

public class ImageBean implements Parcelable{


    /**
     * createdAt : 上传时间
     * fileAddr : 文件穿创建地址
     * fileAttribute : 文件属性
     * fileName : 文件名称
     * fileSize : 文件大小
     * fileTime : 文件创建时间
     * hashMd5 : 文件MD5
     * id : 文件ID
     * img : 文件路径
     * smallimg : 缩略图路径
     * source : 来源
     * status : 状态1正常0删除
     * type : 文件类型
     * updatedAt : 修改时间
     * userId : 用户ID
     */

    private String createdAt;
    private String fileAddr;
    private String fileAttribute;
    private String fileName;
    private String fileSize;
    private String fileTime;
    private String hashMd5;
    private String id;
    private String img;
    private String smallimg;
    private String source;
    private String status;
    private String type;
    private String updatedAt;
    private String userId;

    public ImageBean() {
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFileAddr() {
        return fileAddr;
    }

    public void setFileAddr(String fileAddr) {
        this.fileAddr = fileAddr;
    }

    public String getFileAttribute() {
        return fileAttribute;
    }

    public void setFileAttribute(String fileAttribute) {
        this.fileAttribute = fileAttribute;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public String getHashMd5() {
        return hashMd5;
    }

    public void setHashMd5(String hashMd5) {
        this.hashMd5 = hashMd5;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSmallimg() {
        return smallimg;
    }

    public void setSmallimg(String smallimg) {
        this.smallimg = smallimg;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.createdAt);
        dest.writeString(this.fileAddr);
        dest.writeString(this.fileAttribute);
        dest.writeString(this.fileName);
        dest.writeString(this.fileSize);
        dest.writeString(this.fileTime);
        dest.writeString(this.hashMd5);
        dest.writeString(this.id);
        dest.writeString(this.img);
        dest.writeString(this.smallimg);
        dest.writeString(this.source);
        dest.writeString(this.status);
        dest.writeString(this.type);
        dest.writeString(this.updatedAt);
        dest.writeString(this.userId);
    }



    protected ImageBean(Parcel in) {
        this.createdAt = in.readString();
        this.fileAddr = in.readString();
        this.fileAttribute = in.readString();
        this.fileName = in.readString();
        this.fileSize = in.readString();
        this.fileTime = in.readString();
        this.hashMd5 = in.readString();
        this.id = in.readString();
        this.img = in.readString();
        this.smallimg = in.readString();
        this.source = in.readString();
        this.status = in.readString();
        this.type = in.readString();
        this.updatedAt = in.readString();
        this.userId = in.readString();
    }

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel source) {
            return new ImageBean(source);
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };
}
