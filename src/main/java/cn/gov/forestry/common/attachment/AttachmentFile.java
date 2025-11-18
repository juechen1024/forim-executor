package cn.gov.forestry.common.attachment;

import lombok.Data;

/**
 * 类名称：AttachmentDTO<br>
 * 类描述：适配了antd procomponents 里upload的结构<br>
 * 创建时间：2021年12月11日<br>
 *
 * @author gongdian
 * @version 1.0.0
 */
@Data
public class AttachmentFile {
    private String uid;
    private String etag;
    private String name;
    private String extName;
    private String originName;
    private String url;
    private String thumbUrl;
    private String absoluteUrl;
}
