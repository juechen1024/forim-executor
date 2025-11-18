package cn.gov.forestry.common.utils;

import java.util.Date;

public class CountUtil {
    public static Integer getTwoDateDifferenceDay(Date startTime ,Date endTime){
        //一天有86400秒
        Long day = (endTime.getTime()-startTime.getTime())/86400000;
        return Math.toIntExact(day);
    }

    //num为需要处理的数据，bit为几位小数，需要保留小数位才有必要使用
    public static Double getDecimalsRoundByBit(Double num,int bit){
        int median=1;
        for (int i=1;i<=bit;i++){
            median*=10;
        }
        return Double.valueOf(Math.round(num*median))/median;
    }
}
