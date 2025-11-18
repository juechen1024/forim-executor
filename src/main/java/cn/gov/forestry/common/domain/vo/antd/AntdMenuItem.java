package cn.gov.forestry.common.domain.vo.antd;

import lombok.Data;

import java.util.List;

/**
 * AntdMenuItemNode
 * <p>
 *     https://ant.design/components/menu-cn#itemtype
 * @author gongdear 2023/10/17
 * @return
 */
@Data
public class AntdMenuItem {
    // 展示错误状态样式
    private String danger;
    // 是否禁用
    private Boolean disabled = Boolean.FALSE;
    // 菜单图标
    private String icon;
    // item 的唯一标志
    private String key;
    // 菜单项标题
    private String label;
    // 设置收缩时展示的悬浮标题
    private String title;
    // 定义类型为 group 时，会作为分组处理:  type: 'group', // Must have
    private String type;
    // 子节点
    private List<AntdMenuItem> children;
}
