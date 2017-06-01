# MultiTaskUpload    
## 多任务断点上传    
![image](https://github.com/bux-git/MultiTaskUpload/raw/master/multiType.gif)    
思路记录：   
分为三部分   
#### 1.数据库部分,主要是存储上传任务相关信息
#### 2.上传服务部分，主要是对上传数据进行数据库增删改查操作及上传操作
#### 3.进度显示部分，对上传任务详情进行显示


### 一.数据库

```
 public class EAlbumDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "EAlbumDBHelper";

    public static final String UPLOAD_TASK_TABLE = "upload_task";
    private Context mContext;
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

    public EAlbumDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
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
 ```
 数据库操作类 
  ```
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
 ```
 
### 二.上传服务

    三个数据集 
        未成功上传任务总集合 全局静态
        待上传任务集合 
        正在上传任务集合
    三个控制参数
        最大上传队列数量
        分块大小
        服务是否停止
        
        
    onCreate 
         初始化三个队列集合 OKHttp相关参数
            读取数据库数据初始化上传任务总集合   待上传任务集合  
         开启无限循环 不断将待上传集合中数据 放入 上传任务集合  同时移除出自身
            并将任务添加到请求队列
            
    onStartCommand
        接收外部传入的上传任务参数 
        将任务同时添加到 总集合 待上传集合 和本地数据库
        
### 三.进度显示
    获取服务中总集合 定时刷新进度页面
            