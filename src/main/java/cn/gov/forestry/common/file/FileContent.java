package cn.gov.forestry.common.file;

import lombok.Data;

@Data
public class FileContent {
    private String systemId;
    private String name;
    private String originalFilename;
    private String contentType;
    private String contentLength;
    private Long size;
    private String eTag;
    private byte[] bytes;
    private Boolean success;
    private String message;
    private String uid;
    // 给innerResource专用的字段,用来返回外部看到的绝对路径,其实是assets里的相对于基础/forim的相对路径(不包含路径名)
    private String resourcePath;

    public FileContent() {}

    public FileContent(String name, byte[] bytes) {
        this.name = name;
        this.bytes = bytes;
    }

}
