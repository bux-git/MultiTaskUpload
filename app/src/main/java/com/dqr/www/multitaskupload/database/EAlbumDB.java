package com.dqr.www.multitaskupload.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dqr.www.multitaskupload.Constant;
import com.dqr.www.multitaskupload.bean.ProgressBean;
import com.dqr.www.multitaskupload.bean.UploadTaskBean;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
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
    public static final int MAX_SIZE=300;
    public static final int VERSION = 1;
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
        Cursor cursor = db.query(UPLOAD_TASK_TABLE, null,"userId=?",new String[]{Constant.userId+""}, null, null, " id asc limit 0,"+MAX_SIZE);
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
     * @param id
     * @param startPos
     */
    public void updateTaskStartPosById(int id,long startPos){
        ContentValues values = new ContentValues();
        values.put("startPos",startPos);
        db.update(UPLOAD_TASK_TABLE,values," id = ? ",new String[]{id + ""});
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
        Cursor cursor = db.query(UPLOAD_TASK_TABLE, null, "md5=? and userId=?", new String[]{md5,Constant.userId+""}, null, null, " id asc");

        if (cursor.moveToFirst()) {
            Log.d(TAG, "saveUploadTask: 已经存在相同任务" + cursor.getString(cursor.getColumnIndex("filePath")));
            cursor.close();
            return true;
        }
        cursor.close();
        return false;

    }
}
