package cn.gov.forestry.common.attachment;

import lombok.Data;

/**
 * 类名称：AttachmentDTO<br>
 * 类描述：<br>
 * 创建时间：2021年12月11日<br>
 *
 * @author gongdian
 * @version 1.0.0
 */
@Data
public class AttachmentUploadFile {
    private String uid;
    private Object lastModified;
    private String lastModifiedDate;
    private String name;
    private Object size;
    private String type;
    private Object percent;
    private Object originFileObj;
    private String status;
    private AttachmentFile response;
}
