package cn.gov.forestry.common.domain.dto.metadata;

import cn.gov.forestry.common.necromantic.dto.paint.NecromanticPaint;
import cn.gov.forestry.common.necromantic.dto.source.NecromanticSource;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class MetadataFieldDTO {
    private String systemId;

    private String systemModule;
    /**
     * 字段序号
     */
    private String id;

    /**
     * 表格序号
     */
    private String tableId;

    /**
     * 字段
     */
    private String fieldName;

    /**
     * 字段名
     */
    private String fieldTitleName;

    /**
     * 字段别名
     */
    private String fieldAlia;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 字段数据类型
     */
    private String fieldDataType;

    /**
     * 字段空间类型
     * */
    private String fieldGeometryType;

    /**
     * 字段长度
     */
    private String fieldDataLength;

    /**
     * 默认值
     */
    private String fieldDefaultValue;

    /**
     * 字段顺序(全局唯一的排序,不可修改)
     */
    private Long fieldOrder;

    /**
     * 字段排序(默认赋值fieldOrder,每个表区分的顺序,可以表范围内排序,影响生成的表格中字段排序)
     */
    private Long fieldSortOrder;

    /**
     * 字段关联的枚举,翻译,表格多选单选等都从枚举中取值
     */
    private String fieldEnumId;

    /**
     * 字段从用户信息中带出的字段key（additionalProperties.xxx）
     */
    private String fieldFromUserProperties;

    /**
     * 是否主键
     */
    private Boolean isPrimaryKey;

    /**
     * 是否可为空
     */
    private Boolean isNullable;

    /**
     * 是否排序字段
     */
    private Boolean isOrderField;

    /**
     * 字段渲染配置
     */
    private NecromanticPaint fieldPaint;

    /**
     * 字段源配置
     */
    private NecromanticSource fieldSource;


    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String createUserName;

    private Map<String, Object> additionalProperties;
}
