package cn.gov.forestry.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * 类名称：TokenUtil<br>
 * 类描述：<br>
 * 创建时间：2022年01月10日<br>
 *
 * @author gongdear
 * @version 1.0.0
 */
public class TokenUtil {
    public static String makeToken(String password, Integer length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        sb.append(password);
        return DigestUtils.md5DigestAsHex((sb.toString()).getBytes(StandardCharsets.UTF_8));
    }
}
