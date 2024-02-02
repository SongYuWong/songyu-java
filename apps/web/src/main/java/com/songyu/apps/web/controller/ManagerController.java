package com.songyu.apps.web.controller;

import com.mybatisflex.core.paginate.Page;
import com.songyu.components.api.*;
import com.songyu.components.springboot.mvc.utils.RequestParamChecker;
import com.songyu.domains.auth.aggregate.ResourcePage;
import com.songyu.domains.auth.aggregate.RolePage;
import com.songyu.domains.auth.aggregate.UserPage;
import com.songyu.domains.auth.entity.Resource;
import com.songyu.domains.auth.entity.User;
import com.songyu.domains.auth.entity.Role;
import com.songyu.domains.auth.service.ResourceManagerService;
import com.songyu.domains.auth.service.RoleManagerService;
import com.songyu.domains.auth.service.UserManagerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 管理前端控制器
 * </p>
 *
 * @author songYu
 * @since 2024/1/7 23:23
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {

    @javax.annotation.Resource
    ApiSecureManager apiSecureManager;

    @javax.annotation.Resource
    private UserManagerService userManagerService;

    @javax.annotation.Resource
    private RoleManagerService roleManagerService;

    @javax.annotation.Resource
    private ResourceManagerService resourceManagerService;

    @PostMapping("/newUser")
    @ApiDoc(name = "新增用户", desc = "新增用户", requestType = User.class, responseType = User.class)
    public SecureResponse<User> newUser(@RequestBody SecureRequest secureRequest) {
        User user = secureRequest.parseRequest(apiSecureManager, User.class);
        RequestParamChecker.notNull(user, "缺少用户信息。");
        user = userManagerService.newUser(user);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, user),
                secureRequest.getKey()
        );
    }

    @PostMapping("/archiveUser")
    @ApiDoc(name = "归档用户", desc = "归档用户", requestType = User.class, responseType = Void.class)
    public SecureResponse<Void> archiveUser(@RequestBody SecureRequest secureRequest) {
        User user = secureRequest.parseRequest(apiSecureManager, User.class);
        RequestParamChecker.notNull(user, "缺少用户信息。");
        userManagerService.archiveUser(user);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, null),
                secureRequest.getKey()
        );
    }

    @PostMapping("/editUser")
    @ApiDoc(name = "编辑用户", desc = "编辑用户", requestType = User.class, responseType = User.class)
    public SecureResponse<User> editUser(@RequestBody SecureRequest secureRequest) {
        User user = secureRequest.parseRequest(apiSecureManager, User.class);
        RequestParamChecker.notNull(user, "缺少用户信息。");
        user = userManagerService.editUser(user);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, user),
                secureRequest.getKey()
        );
    }

    @PostMapping("/userPage")
    @ApiDoc(name = "用户分页", desc = "用户分页", requestType = UserPage.class)
    public SecureResponse<Page<UserPage>> userPage(@RequestBody SecureRequest secureRequest) {
        UserPage userPage = secureRequest.parseRequest(apiSecureManager, UserPage.class);
        RequestParamChecker.notNull(userPage, "缺少用户分页信息。");
        Page<UserPage> userPagePage = userManagerService.userPage(userPage);
        userPagePage.getRecords().forEach(User::decryptInfo);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, userPagePage),
                secureRequest.getKey()
        );
    }

    @PostMapping("/newRole")
    @ApiDoc(name = "新增角色", desc = "新增角色", requestType = Role.class, responseType = Role.class)
    public SecureResponse<Role> newRole(@RequestBody SecureRequest secureRequest) {
        Role role = secureRequest.parseRequest(apiSecureManager, Role.class);
        RequestParamChecker.notNull(role, "缺少角色信息。");
        role = roleManagerService.newRole(role);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, role),
                secureRequest.getKey()
        );
    }

    @PostMapping("/archiveRole")
    @ApiDoc(name = "归档角色", desc = "归档角色", requestType = Role.class, responseType = Void.class)
    public SecureResponse<Void> archiveRole(@RequestBody SecureRequest secureRequest) {
        Role role = secureRequest.parseRequest(apiSecureManager, Role.class);
        RequestParamChecker.notNull(role, "缺少角色信息。");
        roleManagerService.archiveRole(role);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, null),
                secureRequest.getKey()
        );
    }

    @PostMapping("/editRole")
    @ApiDoc(name = "编辑角色", desc = "编辑角色", requestType = Role.class, responseType = Role.class)
    public SecureResponse<Role> editRole(@RequestBody SecureRequest secureRequest) {
        Role role = secureRequest.parseRequest(apiSecureManager, Role.class);
        RequestParamChecker.notNull(role, "缺少角色信息。");
        role = roleManagerService.editRole(role);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, role),
                secureRequest.getKey()
        );
    }

    @PostMapping("/rolePage")
    @ApiDoc(name = "角色分页", desc = "角色分页", requestType = RolePage.class)
    public SecureResponse<Page<RolePage>> rolePage(@RequestBody SecureRequest secureRequest) {
        RolePage rolePage = secureRequest.parseRequest(apiSecureManager, RolePage.class);
        RequestParamChecker.notNull(rolePage, "缺少角色分页信息。");
        Page<RolePage> rolePagePage = roleManagerService.rolePage(rolePage);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, rolePagePage),
                secureRequest.getKey()
        );
    }

    @PostMapping("/newResource")
    @ApiDoc(name = "新增资源", desc = "新增资源", requestType = Resource.class, responseType = Resource.class)
    public SecureResponse<Resource> newResource(@RequestBody SecureRequest secureRequest) {
        Resource resource = secureRequest.parseRequest(apiSecureManager, Resource.class);
        RequestParamChecker.notNull(resource, "缺少资源信息。");
        resource = resourceManagerService.newResource(resource);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, resource),
                secureRequest.getKey()
        );
    }

    @PostMapping("/archiveResource")
    @ApiDoc(name = "归档资源", desc = "归档资源", requestType = Resource.class, responseType = Void.class)
    public SecureResponse<Void> archiveResource(@RequestBody SecureRequest secureRequest) {
        Resource resource = secureRequest.parseRequest(apiSecureManager, Resource.class);
        RequestParamChecker.notNull(resource, "缺少资源信息。");
        resourceManagerService.archiveResource(resource);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, null),
                secureRequest.getKey()
        );
    }

    @PostMapping("/editResource")
    @ApiDoc(name = "编辑资源", desc = "编辑资源", requestType = Resource.class, responseType = Resource.class)
    public SecureResponse<Resource> editResource(@RequestBody SecureRequest secureRequest) {
        Resource resource = secureRequest.parseRequest(apiSecureManager, Resource.class);
        RequestParamChecker.notNull(resource, "缺少资源信息。");
        resource = resourceManagerService.editResource(resource);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, resource),
                secureRequest.getKey()
        );
    }

    @PostMapping("/resourcePage")
    @ApiDoc(name = "资源分页", desc = "资源分页", requestType = ResourcePage.class)
    public SecureResponse<Page<ResourcePage>> resourcePage(@RequestBody SecureRequest secureRequest) {
        ResourcePage resourcePage = secureRequest.parseRequest(apiSecureManager, ResourcePage.class);
        RequestParamChecker.notNull(resourcePage, "缺少资源分页信息。");
        Page<ResourcePage> resourcePagePage = resourceManagerService.resourcePage(resourcePage);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, resourcePagePage),
                secureRequest.getKey()
        );
    }

}
