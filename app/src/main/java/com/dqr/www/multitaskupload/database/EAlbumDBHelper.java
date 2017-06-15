package com.dqr.www.multitaskupload.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Description：
 * Author：LiuYM
 * Date： 2017-05-24 10:55
 */

public class EAlbumDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "EAlbumDBHelper";

    public static final String UPLOAD_TASK_TABLE = "upload_task";
    public static final String EALBUM_TABLE_NAME = "ealbum";

    /**
     * 待上传数据记录表
     */
    public static final String UPLOAD_TASK = "create table " + UPLOAD_TASK_TABLE + " (" +
            "id integer primary key autoincrement," +
            "fileSize integer," +//文件大小
            "startPos integer," +//分块开始位置，从0开始
            "md5 text," +//	文件MD5值，小写
            "filePath text," +//分块文件。文件路径
            "fileTime text," +//文件创建时间yyyy-MM-dd HH:mm:ss
            "fileAddr text," +//文件创建地址。 武汉 长沙 中国
            "fileAttribute text," +//	文件其他属性。JSON格式数据
            "source integer," +//0未知来源1安卓2IOS3PC4其他
            "type integer," +//文件类型1图片2视频3音频
            "albumId integer," +//	相册ID
            "albumName text," +//相册名称
            "userId integer" +//userId
            ")";
    /**
     * 上传成功
     */
    public static final String EALBUM_TABLE = "create table " + EALBUM_TABLE_NAME + " (" +
            "sysId integer primary key autoincrement," +
            "id integer," +
            "userId integer," +
            "img text," +
            "fileName text," +

            "smallimg text," +
            "type integer," +
            "hashMd5," +
            "fileTime integer," +
            "fileAddr text," +

            "fileSize text," +
            "fileAttribute text," +
            "status integer," +
            "source integer," +
            "createdAt integer," +

            "updatedAt integer," +
            "upImg text," +
            "img_edit text," +
            "smallimg_edit text" +
            ")";

    public EAlbumDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UPLOAD_TASK);
        db.execSQL(EALBUM_TABLE);
        Log.d(TAG, "onCreate: upload_task 创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(EALBUM_TABLE);
            default:
        }
    }
}
