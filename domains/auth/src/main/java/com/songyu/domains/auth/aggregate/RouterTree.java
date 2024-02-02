package com.songyu.domains.auth.aggregate;

import com.songyu.commonutils.CommonStringUtils;
import com.songyu.domains.auth.entity.Resource;
import com.songyu.domains.auth.entity.ResourceRouter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;
import java.util.Map;

/**
 * <p>
 * 路由树
 * </p>
 *
 * @author songYu
 * @since 2023/12/7 9:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RouterTree extends ResourceRouter {

    /**
     * 子级路由
     */
    private LinkedList<RouterTree> children = new LinkedList<>();

    /**
     * 临时map
     */
    private Map<String, RouterTree> tempMap;

    /**
     * 初始路由
     *
     * @return 根路由
     */
    public static RouterTree initRouter(ResourceRouter resourceRouter) {
        RouterTree router = new RouterTree();
        router.initWithRouterResource(resourceRouter);
        return router;
    }

    public void addRouter(Resource resource, Map<String, ResourceRouter> resourceRouterMap) {
        ResourceRouter resourceRouter = resourceRouterMap.remove(resource.getResourceCode());
        if (resourceRouter != null) {
            RouterTree routerTree = tempMap.getOrDefault(resource.getResourceCode(),
                    RouterTree.initRouter(resourceRouter));
            tempMap.put(resource.getResourceCode(), routerTree);
            if (!routerTree.isInited()) {
                routerTree.initWithRouterResource(resourceRouter);
            }
            if (resource.getParentResourceCode() != null) {
                RouterTree parentRouterTree = tempMap.getOrDefault(resource.getParentResourceCode(), new RouterTree());
                parentRouterTree.addChild(routerTree);
                tempMap.put(resource.getParentResourceCode(), parentRouterTree);
            } else {
                children.addLast(routerTree);
            }
        }
    }

    private void addChild(RouterTree routerTree) {
        this.children.addLast(routerTree);
    }

    private void initWithRouterResource(ResourceRouter resourceRouter) {
        this.setName(resourceRouter.getName());
        this.setPath(resourceRouter.getPath());
        this.setComponent(resourceRouter.getComponent());
        this.setMeta(resourceRouter.getMeta());
    }

    private boolean isInited() {
        return CommonStringUtils.isNotBlank(this.getPath());
    }

}
