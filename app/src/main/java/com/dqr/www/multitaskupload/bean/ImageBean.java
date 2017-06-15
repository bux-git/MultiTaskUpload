package com.dqr.www.multitaskupload.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description：
 * Author：LiuYM
 * Date： 2017-05-26 17:57
 */

public class ImageBean implements Parcelable {

    /**
     * id : 8000
     * userId : 7852
     * img : http://test.dqr2015.cn:8888/uploadFiles/201706/7852/97deaae975354cfb9c2cfdb924b73a8f.jpg
     * fileName : IMG_20170607_173632_BURST30.jpg
     * smallimg : http://test.dqr2015.cn:8888/uploadFiles/201706/7852/small_97deaae975354cfb9c2cfdb924b73a8f.jpg
     * type : 1
     * hashMd5 : 998ba3c4db7f35486fe7459b240cfcae
     * fileTime : 1496784996000
     * fileAddr : 湖南 长沙
     * fileSize : 2518483
     * fileAttribute : {"lng":28,"lat":113}
     * status : 1
     * source : 1
     * createdAt : 1496920685000
     * updatedAt : 1496920685000
     * upImg : null
     * img_edit : /uploadFiles/201706/7852/97deaae975354cfb9c2cfdb924b73a8f.jpg
     * smallimg_edit : /uploadFiles/201706/7852/small_97deaae975354cfb9c2cfdb924b73a8f.jpg
     */

    private int id;
    private int userId;
    private String img;
    private String fileName;
    private String smallimg;
    private int type;
    private String hashMd5;
    private long fileTime;
    private String fileAddr;
    private int fileSize;
    private String fileAttribute;
    private int status;
    private int source;
    private long createdAt;
    private long updatedAt;
    private String upImg;
    private String img_edit;
    private String smallimg_edit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSmallimg() {
        return smallimg;
    }

    public void setSmallimg(String smallimg) {
        this.smallimg = smallimg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHashMd5() {
        return hashMd5;
    }

    public void setHashMd5(String hashMd5) {
        this.hashMd5 = hashMd5;
    }

    public long getFileTime() {
        return fileTime;
    }

    public void setFileTime(long fileTime) {
        this.fileTime = fileTime;
    }

    public String getFileAddr() {
        return fileAddr;
    }

    public void setFileAddr(String fileAddr) {
        this.fileAddr = fileAddr;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileAttribute() {
        return fileAttribute;
    }

    public void setFileAttribute(String fileAttribute) {
        this.fileAttribute = fileAttribute;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpImg() {
        return upImg;
    }

    public void setUpImg(String upImg) {
        this.upImg = upImg;
    }

    public String getImg_edit() {
        return img_edit;
    }

    public void setImg_edit(String img_edit) {
        this.img_edit = img_edit;
    }

    public String getSmallimg_edit() {
        return smallimg_edit;
    }

    public void setSmallimg_edit(String smallimg_edit) {
        this.smallimg_edit = smallimg_edit;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.userId);
        dest.writeString(this.img);
        dest.writeString(this.fileName);
        dest.writeString(this.smallimg);
        dest.writeInt(this.type);
        dest.writeString(this.hashMd5);
        dest.writeLong(this.fileTime);
        dest.writeString(this.fileAddr);
        dest.writeInt(this.fileSize);
        dest.writeString(this.fileAttribute);
        dest.writeInt(this.status);
        dest.writeInt(this.source);
        dest.writeLong(this.createdAt);
        dest.writeLong(this.updatedAt);
        dest.writeString(this.upImg);
        dest.writeString(this.img_edit);
        dest.writeString(this.smallimg_edit);
    }

    public ImageBean() {
    }

    protected ImageBean(Parcel in) {
        this.id = in.readInt();
        this.userId = in.readInt();
        this.img = in.readString();
        this.fileName = in.readString();
        this.smallimg = in.readString();
        this.type = in.readInt();
        this.hashMd5 = in.readString();
        this.fileTime = in.readLong();
        this.fileAddr = in.readString();
        this.fileSize = in.readInt();
        this.fileAttribute = in.readString();
        this.status = in.readInt();
        this.source = in.readInt();
        this.createdAt = in.readLong();
        this.updatedAt = in.readLong();
        this.upImg =in.readString();
        this.img_edit = in.readString();
        this.smallimg_edit = in.readString();
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
