package com.dqr.www.multitaskupload.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.dqr.www.multitaskupload.Constant;
import com.dqr.www.multitaskupload.bean.ImageBean;
import com.dqr.www.multitaskupload.bean.ProgressBean;
import com.dqr.www.multitaskupload.bean.UploadTaskBean;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.dqr.www.multitaskupload.database.EAlbumDBHelper.EALBUM_TABLE_NAME;
import static com.dqr.www.multitaskupload.database.EAlbumDBHelper.UPLOAD_TASK_TABLE;

/**
 * Description：图片上传信息操作类
 * Author：LiuYM
 * Date： 2017-05-24 14:12
 */

public class EAlbumDB {
    /**
     * 数据库名称
     */
    public static final String db_NAME = "ealbum_db";
    /**
     * 数据库版本
     */
    public static final int MAX_SIZE = 300;
    public static final int VERSION = 2;
    private static EAlbumDB sEAlbumDB;
    private SQLiteDatabase db;

    private EAlbumDB(Context context) {
        EAlbumDBHelper dbHelper = new EAlbumDBHelper(context, db_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }


    public synchronized static EAlbumDB getInstance(Context context) {
        if (sEAlbumDB == null) {
            sEAlbumDB = new EAlbumDB(context);
        }
        return sEAlbumDB;
    }

    /**
     * 存储上传任务
     *
     * @param up
     */
    public long saveUploadTask(UploadTaskBean up) {
        if (up != null) {
            if (isHasSameTaskByMD5(up.getMd5())) {
                return -1;
            }
            ContentValues values = new ContentValues();
            values.put("fileSize", up.getFileSize());
            values.put("startPos", up.getStartPos());
            values.put("md5", up.getMd5());
            values.put("filePath", up.getFilePath());
            values.put("fileTime", up.getFileTime());
            values.put("fileAddr", up.getFileAddr());
            values.put("fileAttribute", up.getFileAttribute());
            values.put("source", up.getSource());
            values.put("type", up.getType());
            values.put("albumId", up.getAlbumId());
            values.put("albumName", up.getAlbumName());
            values.put("userId", Constant.userId);
            long id = db.insert(UPLOAD_TASK_TABLE, null, values);
            Log.d(TAG, "saveUploadTask:success id:" + id);
            return id;
        }
        return -1;
    }

    /**
     * 获取所有上传任务
     *
     * @return
     */
    public List<ProgressBean> getUploadTaskBean() {
        List<ProgressBean> list = new ArrayList<>();
        Cursor cursor = db.query(UPLOAD_TASK_TABLE, null, "userId=?", new String[]{Constant.userId + ""}, null, null, " id asc limit 0," + MAX_SIZE);
        if (cursor.moveToFirst()) {
            do {
                UploadTaskBean up = new UploadTaskBean();
                up.setId(cursor.getInt(cursor.getColumnIndex("id")));
                up.setStartPos(cursor.getInt(cursor.getColumnIndex("startPos")));
                up.setFileSize(cursor.getInt(cursor.getColumnIndex("fileSize")));
                up.setMd5(cursor.getString(cursor.getColumnIndex("md5")));
                up.setFilePath(cursor.getString(cursor.getColumnIndex("filePath")));
                up.setFileTime(cursor.getLong(cursor.getColumnIndex("fileTime")));
                up.setFileAddr(cursor.getString(cursor.getColumnIndex("fileAddr")));
                up.setFileAttribute(cursor.getString(cursor.getColumnIndex("fileAttribute")));
                up.setSource(cursor.getInt(cursor.getColumnIndex("source")));
                up.setType(cursor.getInt(cursor.getColumnIndex("type")));
                up.setAlbumId(cursor.getInt(cursor.getColumnIndex("albumId")));
                up.setAlbumName(cursor.getString(cursor.getColumnIndex("albumName")));
                list.add(up);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 根据ID 修改任务起始位置
     *
     * @param id
     * @param startPos
     */
    public void updateTaskStartPosById(int id, long startPos) {
        ContentValues values = new ContentValues();
        values.put("startPos", startPos);
        db.update(UPLOAD_TASK_TABLE, values, " id = ? ", new String[]{id + ""});
    }

    /**
     * 根据ID删除上传任务
     *
     * @param id
     */
    public void deleteUploadTaskById(int id) {
        db.delete(UPLOAD_TASK_TABLE, "id=?", new String[]{id + ""});
    }

    /**
     * 删除所有上传任务
     */
    public void deleteUploadTaskAll() {
        db.delete(UPLOAD_TASK_TABLE, null, null);
    }

    /**
     * 是否已经存在相同任务 根据图片md5判断
     *
     * @param md5
     * @return
     */
    public boolean isHasSameTaskByMD5(String md5) {
        Cursor cursor = db.query(UPLOAD_TASK_TABLE, null, "md5=? and userId=?", new String[]{md5, Constant.userId + ""}, null, null, " id asc");

        if (cursor.moveToFirst()) {
            Log.d(TAG, "saveUploadTask: 已经存在相同任务" + cursor.getString(cursor.getColumnIndex("filePath")));
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public static final String QUERY_UPLOAD_IMAGE = "insert into " + EALBUM_TABLE_NAME +
            " ( id, userId, img, fileName, smallimg" +
            ", type, hashMd5, fileTime, fileAddr, fileSize" +
            ", fileAttribute, status, source, createdAt, updatedAt" +
            ", upImg, img_edit, smallimg_edit)" +
            " select ?, ?, ?, ?, ?" +
            ", ?, ?, ?, ?, ?" +
            ", ?, ?, ?, ?, ?" +
            ", ?, ?, ? " +
            "where not exists(select 1 from  " + EALBUM_TABLE_NAME + " where userId=? and id=?)";

    /**
     * 存储上传成功图片信息
     *
     * @param img
     */
    public synchronized void saveUploadImage(ImageBean img) {
        db.execSQL(QUERY_UPLOAD_IMAGE, new String[]{
                 String.valueOf(img.getId())
                , String.valueOf(img.getUserId())
                , img.getImg()
                , img.getFileName()
                , img.getSmallimg()

                , String.valueOf(img.getType())
                , img.getHashMd5()
                , String.valueOf(img.getFileTime())
                , img.getFileAddr()
                , String.valueOf(img.getFileSize())

                , img.getFileAttribute()
                , String.valueOf(img.getStatus())
                , String.valueOf(img.getSource())
                , String.valueOf(img.getCreatedAt())
                , String.valueOf(img.getUpdatedAt())

                , img.getUpImg()
                , img.getImg_edit()
                , img.getSmallimg_edit()
                , String.valueOf(Constant.userId)
                , String.valueOf(img.getId())});
    }


    public synchronized void saveUploadImage(List<ImageBean> imgs) {
        int maxSize=3000;//每一次批量插入数量

        int imgSize = imgs.size();

        //一共需要执行多少次
        int fSize = imgSize % maxSize;//取余

        int num;
        if(fSize==0){
            num=imgSize/maxSize;
        }else{// if(fSize>0)
            num=imgSize/maxSize+1;
        }

        for(int i=0;i<num;i++){

            SQLiteStatement stat = db.compileStatement(QUERY_UPLOAD_IMAGE);
            db.beginTransaction();

            int startJ=i*maxSize;//起始值
            int endJ=(i+1)*maxSize;//结束值

            for (int j = startJ; j <endJ ; j++) {

                if(j==imgSize)break;

                ImageBean img = imgs.get(j);

                stat.bindLong(1, img.getId());
                stat.bindLong(2, img.getUserId());
                stat.bindString(3,img.getImg());
                stat.bindString(4, img.getFileName());
                stat.bindString(5, img.getSmallimg());

                stat.bindLong(6,img.getType());
                stat.bindString(7, img.getHashMd5());
                stat.bindLong(8, img.getFileTime());
                stat.bindString(9, img.getFileAddr());
                stat.bindLong(10, img.getFileSize());

                stat.bindString(11, img.getFileAttribute());
                stat.bindLong(12, img.getStatus());
                stat.bindLong(13, img.getSource());
                stat.bindLong(14, img.getCreatedAt());
                stat.bindLong(15, img.getUpdatedAt());

                stat.bindString(16,img.getUpImg());
                stat.bindString(17, img.getImg_edit());
                stat.bindString(18, img.getSmallimg_edit());

                //条件
                stat.bindLong(19, Constant.userId);
                stat.bindLong(20, img.getId());

                stat.executeInsert();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }

    }


    /**
     * 获取所有图片信息
     *
     * @return
     */
    public List<ImageBean> getAllImageBean() {

        List<ImageBean> list = new ArrayList<>();
        Cursor cursor = db.query(EALBUM_TABLE_NAME, null, "userId=?", new String[]{String.valueOf(Constant.userId)}, null, null, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndex("id");
            int userId = cursor.getColumnIndex("userId");
            int img = cursor.getColumnIndex("img");
            int fileName = cursor.getColumnIndex("fileName");
            int smallImg = cursor.getColumnIndex("smallimg");

            int type = cursor.getColumnIndex("type");
            int hashMd5 = cursor.getColumnIndex("hashMd5");
            int fileTime = cursor.getColumnIndex("fileTime");
            int fileAddr = cursor.getColumnIndex("fileAddr");
            int fileSize = cursor.getColumnIndex("fileSize");

            int fileAttribute = cursor.getColumnIndex("fileAttribute");
            int status = cursor.getColumnIndex("status");
            int source = cursor.getColumnIndex("source");
            int createdAt = cursor.getColumnIndex("createdAt");
            int updatedAt = cursor.getColumnIndex("updatedAt");

            int upImg = cursor.getColumnIndex("upImg");
            int img_edit = cursor.getColumnIndex("img_edit");
            int smallimg_edit = cursor.getColumnIndex("smallimg_edit");


            do {
                ImageBean bean = new ImageBean();
                bean.setId(cursor.getInt(id));
                bean.setUserId(cursor.getInt(userId));
                bean.setImg(cursor.getString(img));
                bean.setFileName(cursor.getString(fileName));
                bean.setSmallimg(cursor.getString(smallImg));

                bean.setType(cursor.getInt(type));
                bean.setHashMd5(cursor.getString(hashMd5));
                bean.setFileTime(cursor.getInt(fileTime));
                bean.setFileAddr(cursor.getString(fileAddr));
                bean.setFileSize(cursor.getInt(fileSize));

                bean.setFileAttribute(cursor.getString(fileAttribute));
                bean.setStatus(cursor.getInt(status));
                bean.setSource(cursor.getInt(source));
                bean.setCreatedAt(cursor.getInt(createdAt));
                bean.setUpdatedAt(cursor.getInt(updatedAt));

                bean.setUpImg(cursor.getString(upImg));
                bean.setImg_edit(cursor.getString(img_edit));
                bean.setSmallimg_edit(cursor.getString(smallimg_edit));



                list.add(bean);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    /**
     * 删除所有图片信息
     */
    public void deleteAllImage() {
        db.delete(EALBUM_TABLE_NAME, "userId= ?", new String[]{String.valueOf(Constant.userId)});
    }


}
