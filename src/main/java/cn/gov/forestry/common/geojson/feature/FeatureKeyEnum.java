package cn.gov.forestry.common.geojson.feature;

import cn.gov.forestry.common.domain.bo.SystemBuildInFieldEnum;
import lombok.Getter;

public enum FeatureKeyEnum {
    ID(SystemBuildInFieldEnum.ID.getFieldName()),
    TYPE("type"),
    GEOMETRY("geometry"),
    PROPERTIES("properties"),
    BBOX("bbox");

    @Getter
    private String key;

    FeatureKeyEnum(String key) {
        this.key = key;
    }

    public static Boolean isFeatureKey(String key) {
        for (FeatureKeyEnum featureKeyEnum : FeatureKeyEnum.values()) {
            if (featureKeyEnum.getKey().equals(key)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    // 返回空间字段的key除了id
    public static Boolean isFeatureKeyExceptId(String key) {
        for (FeatureKeyEnum featureKeyEnum : FeatureKeyEnum.values()) {
            if (featureKeyEnum.getKey().equals(key)) {
                if (ID.getKey().equals(key)) {
                    return Boolean.FALSE;
                } else {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
}
