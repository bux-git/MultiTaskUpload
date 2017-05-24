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
    private Context mContext;
    /**
     * 待上传数据记录表
     */
    public static final String UPLOAD_TASK = "create table upload_task (" +
            "id integer primary key autoincrement," +
            "size integer," +//文件大小
            "startPos integer," +//分块开始位置，从0开始
            "name text," +//	文件名称
            "md5 text," +//	文件MD5值，小写
            "file text," +//分块文件。文件路径
            "fileTime text," +//文件创建时间yyyy-MM-dd HH:mm:ss
            "fileAddr text," +//文件创建地址。 武汉 长沙 中国
            "fileAttribute text," +//	文件其他属性。JSON格式数据
            "source integer," +//0未知来源1安卓2IOS3PC4其他
            "type integer," +//文件类型1图片2视频3音频
            "albumId integer" +//	相册ID
            "albumName text" +//相册名称
            ")";

    public EAlbumDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UPLOAD_TASK);
        Log.d(TAG, "onCreate: upload_task 创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
