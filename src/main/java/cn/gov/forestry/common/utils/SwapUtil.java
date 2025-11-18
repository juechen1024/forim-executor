package cn.gov.forestry.common.utils;

import java.util.List;

public class SwapUtil {
    //冒泡排序法（升序）
    public static List<Long> ascLongListSwap(List<Long> longList){
        for (int i = 0; i < longList.size()-1; i++) {//外层for循环控制循环次数
            for(int j = 0; j < longList.size()-1-i; j++){//内层for循环依次比较相邻2个order值
                if(longList.get(j) > longList.get(j+1)){//按升序规则，前者要小于后者，如果前者大于后者，则交换顺序
                    Long temp1 = longList.get(j);
                    Long temp2 = longList.get(j+1);
                    longList.set(j,temp2);
                    longList.set(j+1,temp1);
                }
            }
        }
        return longList;
    }
    //冒泡排序法（降序）
    public static List<Long> descLongListSwap(List<Long> longList){
        for (int i = 0; i < longList.size()-1; i++) {//外层for循环控制循环次数
            for(int j = 0; j < longList.size()-1-i; j++){//内层for循环依次比较相邻2个order值
                if(longList.get(j) < longList.get(j+1)){//按降序规则，前者要大于后者，如果前者小于后者，则交换顺序
                    Long temp1 = longList.get(j);
                    Long temp2 = longList.get(j+1);
                    longList.set(j,temp2);
                    longList.set(j+1,temp1);
                }
            }
        }
        return longList;
    }
}
