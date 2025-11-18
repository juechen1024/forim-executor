package cn.gov.forestry.common.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 类名称：ConsoleProcessBarUtil<br>
 * 类描述：<br>
 * 创建时间：2024年07月09日<br>
 *
 * @author gongdear
 * @version 1.0.0
 */
public class ConsoleProcessBarUtil<T> extends ProcessBarUtil<T> {

    private final OutputStream outputStream;

    /**
     * 未完成的
     */
    private static final char incomplete = '░';

    /**
     * 完成的
     */
    private static final char complete = '█';


    private ConsoleProcessBarUtil(Iterable<T> data, Integer startNum, Integer current, Integer endNum) {
        super(data, startNum, current, endNum);
        outputStream = System.out;
    }

    public static <T> ProcessBarUtil<T> init(Iterable<T> data) {
        return new ConsoleProcessBarUtil<>(data, 0, 0, size(data));
    }

    public static ProcessBarUtil<?> init(Integer startNum, Integer endNum) {
        return new ConsoleProcessBarUtil<>(null, startNum, startNum, endNum);
    }

    @Override
    protected void updateProcessBar(Integer processCurrentNum, Integer processTotalNum) {
        String processBar = generateProcessBar(processCurrentNum, processTotalNum);
        try {
            outputStream.write("\r".getBytes());
            outputStream.write(processBar.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("an error occurred while printing the progress bar");
        }
    }

    private String generateProcessBar(Integer processCurrentNum, Integer processTotalNum) {
        StringBuilder sb = new StringBuilder("[");
        Stream.generate(() -> complete)
                .limit(processCurrentNum)
                .forEach(sb::append);
        IntStream.range(processCurrentNum, processTotalNum)
                .mapToObj(i -> incomplete)
                .forEach(sb::append);
        sb.append("] ");
        if (processCurrentNum >= processTotalNum) {
            sb.append("complete\n");
        } else {
            sb.append(processCurrentNum).append("%");
        }
        return sb.toString();
    }
    /**
     * 返回 Iterable 对象的元素数量
     *
     * @param iterable Iterable对象
     * @return Iterable对象的元素数量
     * @since 5.5.0
     */
    public static int size(Iterable<?> iterable) {
        if (null == iterable) {
            return 0;
        }

        if (iterable instanceof Collection<?>) {
            return ((Collection<?>) iterable).size();
        } else {
            return size(iterable.iterator());
        }
    }
    /**
     * 返回 Iterator 对象的元素数量
     *
     * @param iterator Iterator对象
     * @return Iterator对象的元素数量
     * @since 5.5.0
     */
    public static int size(Iterator<?> iterator) {
        int size = 0;
        if (iterator != null) {
            while (iterator.hasNext()) {
                iterator.next();
                size++;
            }
        }
        return size;
    }

}
