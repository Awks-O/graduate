package cn.core.beans;

import lombok.Data;

import java.util.List;

/**
 * 数节点，测试前台控件使用
 *
 * @author 肖文杰 https://github.com/xwjie/
 */
@Data
public class TreeNode {

    private long id;

    private String name;

    private String icon = "edit";

    private List<TreeNode> subnodes;

    public TreeNode(long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

}
