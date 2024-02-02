package com.songyu.domains.auth.aggregate;

import com.alibaba.fastjson.JSON;
import com.songyu.domains.auth.entity.Resource;
import com.songyu.domains.auth.entity.ResourceRouter;
import com.songyu.domains.auth.entity.User;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 认证客户端
 * </p>
 *
 * @author songYu
 * @since 2023/11/2 11:24
 */
@Data
public class AuthClient {

    /**
     * 认证客户端用户信息
     */
    private User user;

    /**
     * 客户端唯一编码
     */
    private String clientId;

    /**
     * 客户端组件树
     */
    private ComponentTree components;

    /**
     * 客户端路由信息
     */
    private RouterTree routers;

    public static AuthClient initWithUser(User user) {
        AuthClient authClient = new AuthClient();
        authClient.user = JSON.parseObject(JSON.toJSONBytes(user), User.class);
        return authClient;
    }

    public void addComponentCodes(Resource resource) {
        if (components == null) {
            ComponentTree componentTree = new ComponentTree();
            componentTree.setComponentCode("ROOT");
            componentTree.setTempMap(new HashMap<>());
            components = componentTree;
        }
        components.addComponent(resource);
    }

    public void addResourceRouter(Resource resource, Map<String, ResourceRouter> resourceRouterMap) {
        if (routers == null) {
            RouterTree routerTree = new RouterTree();
            routerTree.setName("ROOT");
            routerTree.setTempMap(new HashMap<>());
            routers = routerTree;
        }
        routers.addRouter(resource, resourceRouterMap);
    }

    public void clearTokenExcepted() {
        routers.setTempMap(null);
        components.setTempMap(null);
        this.setClientId(null);
        this.user.clearTokenExcepted();
    }

}
