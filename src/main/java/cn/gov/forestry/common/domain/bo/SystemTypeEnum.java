package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum SystemTypeEnum {
    WEB("1010", "Web"),
    APP_ANDROID("1020", "Android"),
    APP_IOS("1030", "iOS"),
    DESKTOP_WINDOWS("1040", "Windows Desktop"),
    DESKTOP_MAC("1050", "Mac Desktop"),
    DESKTOP_LINUX("1060", "Linux Desktop"),
    SERVER("1070", "Server"),
    EMBEDDED("1080", "Embedded System"),
    IOT("1090", "IoT (Internet of Things)"),
    API("1100", "API Service"),
    CLI("1110", "Command Line Interface"),
    MOBILE_PWA("1120", "Progressive Web App (Mobile)"),
    MOBILE_HYBRID("1130", "Hybrid Mobile App"),
    GAME_CONSOLE("1140", "Game Console"),
    CLOUD("1150", "Cloud Platform"),
    MICROSERVICE("1160", "Microservice"),
    MONOLITHIC("1170", "Monolithic Application"),
    PLUGIN("1180", "Plugin/Extension"),
    WEARABLE("1190", "Wearable Device"),
    VR_AR("1200", "Virtual Reality / Augmented Reality"),
    ROBOTICS("1210", "Robotics"),
    AUTOMATION("1220", "Automation System"),
    BLOCKCHAIN("1230", "Blockchain Network");

    @Getter
    private final String code;

    @Getter
    private final String name;

    SystemTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据 code 获取对应的枚举值
     *
     * @param code 系统类型的编码
     * @return 对应的枚举值，如果未找到则返回 null
     */
    public static SystemTypeEnum fromCode(String code) {
        for (SystemTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据 name 获取对应的枚举值
     *
     * @param name 系统类型的名称
     * @return 对应的枚举值，如果未找到则返回 null
     */
    public static SystemTypeEnum fromName(String name) {
        for (SystemTypeEnum type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}