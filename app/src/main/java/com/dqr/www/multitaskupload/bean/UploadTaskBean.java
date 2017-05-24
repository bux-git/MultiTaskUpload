package com.dqr.www.multitaskupload.bean;

/**
 * Description：相册上传参数信息类
 * Author：LiuYM
 * Date： 2017-05-24 14:00
 */

public class UploadTaskBean extends ProgressBean {

    private int id;//表中自增ID
    private long fileSize;//文件大小
    private long startPos;//分块开始位置，从0开始
    private String md5;//	文件MD5值，小写
    private String filePath;//分块文件。文件路径
    private Long fileTime;//文件创建时间yyyy-MM-dd HH:mm:ss
    private String fileAddr;//文件创建地址。 武汉 长沙 中国
    private String fileAttribute;//	文件其他属性。JSON格式数据
    private int source=1;//0未知来源1安卓2IOS3PC4其他
    private int type=1;//文件类型1图片2视频3音频
    private int albumId;//	相册ID
    private String albumName;//相册名称


    /**
     * 初始化全部参数
     * @param fileSize
     * @param startPos
     * @param md5
     * @param filePath
     * @param fileTime
     * @param fileAddr
     * @param fileAttribute
     * @param source
     * @param type
     * @param albumId
     * @param albumName
     */
    public UploadTaskBean(long fileSize, long startPos, String md5, String filePath, Long fileTime, String fileAddr, String fileAttribute, int source, int type, int albumId, String albumName) {
        this.fileSize = fileSize;
        this.startPos = startPos;
        this.md5 = md5;
        this.filePath = filePath;
        this.fileTime = fileTime;
        this.fileAddr = fileAddr;
        this.fileAttribute = fileAttribute;
        this.source = source;
        this.type = type;
        this.albumId = albumId;
        this.albumName = albumName;
    }

    /**
     * 默认souce 为1 android
     * 默认 文件类型 type 为1 图片
     * @param fileSize
     * @param startPos
     * @param md5
     * @param filePath
     * @param fileTime
     * @param fileAddr
     * @param fileAttribute
     * @param albumId
     * @param albumName
     */
    public UploadTaskBean(long fileSize, long startPos, String md5, String filePath, Long fileTime, String fileAddr, String fileAttribute, int albumId, String albumName) {
        this.fileSize = fileSize;
        this.startPos = startPos;
        this.md5 = md5;
        this.filePath = filePath;
        this.fileTime = fileTime;
        this.fileAddr = fileAddr;
        this.fileAttribute = fileAttribute;
        this.albumId = albumId;
        this.albumName = albumName;
    }

    public UploadTaskBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileTime() {
        return fileTime;
    }

    public void setFileTime(Long fileTime) {
        this.fileTime = fileTime;
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

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}
