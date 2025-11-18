package cn.gov.forestry.common.utils;

import java.nio.charset.StandardCharsets;

/**
 * 类名称：PasswordUtil<br>
 * 类描述：<br>
 * 创建时间：2021年12月07日<br>
 *
 * @author gongdear
 * @version 1.0.0
 */
public class PasswordUtil {
    public static String makePassword(String password, String seed){
        return DigestUtils.md5DigestAsHex((password + seed).getBytes(StandardCharsets.UTF_8));
    }
}
