package cn.gov.forestry.common.domain.vo.antd;

import lombok.Data;

import java.util.List;

/**
 * AntdTreeDataNode
 * <p>
 *     https://ant.design/components/tree-cn#treenode-props
 * @author gongdear 2025/05/20
 * @return
 */
@Data
public class AntdTreeDataNode {
    private Boolean checkable;       // 当树为 checkable 时，设置独立节点是否展示 Checkbox
    private Boolean disableCheckbox; // 禁掉 checkbox
    private Boolean disabled;        // 禁掉响应
    private String icon;             // 自定义图标（这里简化为字符串）
    private Boolean isLeaf;          // 设置为叶子节点 (设置了 loadData 时有效)
    private String key;              // 节点唯一标识符
    private Boolean selectable;      // 设置节点是否可被选中
    private String title;            // 标题

    private List<AntdTreeDataNode> children;
}
