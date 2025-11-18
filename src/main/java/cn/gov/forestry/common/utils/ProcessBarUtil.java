package cn.gov.forestry.common.utils;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * 类名称：ProcessBarUtil<br>
 * 类描述：<br>
 * 创建时间：2024年07月09日<br>
 *
 * @author gongdear
 * @version 1.0.0
 */
public abstract class ProcessBarUtil<T> implements Iterable<T> {

    /**
     * 遍历的数据
     */
    private final Iterable<T> data;

    /**
     * 结束数量
     */
    private final Integer startNum;

    /**
     * 当前的进度
     */
    private Integer current;

    /**
     * 结束数量
     */
    private final Integer endNum;

    /**
     * 进度条当前位置
     */
    private Integer processCurrentNum = 0;

    /**
     * 进度条总数
     */
    private final Integer processTotalNum = 100;

    public ProcessBarUtil(Iterable<T> data, Integer startNum, Integer current, Integer endNum) {
        this.data = data;
        this.startNum = startNum;
        this.current = current;
        this.endNum = endNum;
    }

    /**
     * 添加进度
     *
     * @param num 本次进度数量
     */
    public void add(Integer num) {
        this.current += num;
        this.processCurrentNum = (int) (100.0 * current / endNum);
        updateProcessBar(processCurrentNum, processTotalNum);
    }

    /**
     * 更新进度条
     *
     * @param current 当前进度
     */
    public void update(Integer current) {
        this.current = current;
        this.processCurrentNum = (int) (100.0 * current / endNum);
        updateProcessBar(processCurrentNum, processTotalNum);
    }

    protected abstract void updateProcessBar(Integer processCurrentNum, Integer processTotalNum);

    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        data.forEach(t -> {
            action.accept(t);
            add(1);
        });
    }
}
