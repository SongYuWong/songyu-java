package com.songyu.domains.auth.aggregate;

import com.songyu.commonutils.CommonStringUtils;
import com.songyu.domains.auth.entity.Resource;
import lombok.Data;

import java.util.LinkedList;
import java.util.Map;

/**
 * <p>
 * 前端组件树
 * </p>
 *
 * @author songYu
 * @since 2024/1/6 22:08
 */
@Data
public class ComponentTree {

    /**
     * 组件编码
     */
    private String componentCode;

    /**
     * 子组件集合
     */
    private LinkedList<ComponentTree> children = new LinkedList<>();

    /**
     * 临时map
     */
    private Map<String, ComponentTree> tempMap;

    private static ComponentTree initComponent(Resource resource){
        ComponentTree componentTree = new ComponentTree();
        componentTree.initWithResource(resource);
        return componentTree;
    }

    private void initWithResource(Resource resource) {
        this.setComponentCode(resource.getResourceCode());
        this.setChildren(new LinkedList<>());
    }

    public void addComponent(Resource resource) {
        ComponentTree componentTree = tempMap.getOrDefault(resource.getResourceCode(),
                ComponentTree.initComponent(resource));
        tempMap.put(resource.getResourceCode(), componentTree);
        if (componentTree.isInited()){
            componentTree.initWithResource(resource);
        }
        if (resource.getParentResourceCode() != null) {
            ComponentTree parentComponentTree = tempMap.getOrDefault(resource.getParentResourceCode(), new ComponentTree());
            parentComponentTree.addChild(componentTree);
            tempMap.put(resource.getParentResourceCode(), parentComponentTree);
        } else {
            children.addLast(componentTree);
        }
    }

    private void addChild(ComponentTree componentTree) {
        this.children.addLast(componentTree);
    }

    private boolean isInited() {
        return CommonStringUtils.isNotBlank(this.componentCode);
    }
}
