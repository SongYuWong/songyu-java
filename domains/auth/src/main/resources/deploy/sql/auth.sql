# 认证数据库
CREATE SCHEMA IF NOT EXISTS `auth` COLLATE utf8mb4_general_ci;
USE `auth`;

-- user: table
CREATE TABLE IF NOT EXISTS `auth`.`user`
(
    `user_code`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户编码',
    `user_name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名称',
    `user_desc`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户简介',
    `user_email`         varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户邮箱',
    `user_tel`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户电话号码',
    `user_password`      varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码',
    `user_status_code`   int                                                           NOT NULL COMMENT '用户状态码',
    `user_client_config` json                                                          NOT NULL COMMENT '用户客户端配置',
    `created_at`         timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
    `updated_at`         timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`user_code`),
    UNIQUE KEY `user_index` (`user_email`, `user_name`) COMMENT '用户唯一约束索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户信息表';

insert into auth.user (user_code, user_name, user_desc, user_email, user_tel, user_password, user_status_code,
                       user_client_config)
values ('DN007', 'wVCJqlkEvh+UqyFzzqhI/Q==', '大内密探', 'OVYg4lNk5BXXtS8uif7wIruZCC3JOFVJ+UVuPlsyvng=',
        'IOgiYmIKlTHHgE3VZSq3Rw==', '4afz8ja8LsRa3Bo/Csbf0WxvBvQlbO6BKZY8K6QFMF4=', 2, '{}');

-- role: table
CREATE TABLE IF NOT EXISTS `auth`.`role`
(
    `role_code`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色唯一号编码',
    `role_name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
    `role_alias`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色别名',
    `role_desc`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色描述',
    `parent_role_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '父级角色唯一号编码',
    `created_at`       timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`       timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`role_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='角色';

INSERT INTO auth.role (role_code, role_name, role_alias, role_desc, parent_role_code, created_at, updated_at)
VALUES ('0', '0', '0', '系统管理员', null, '2023-12-05 18:21:43', '2023-12-05 18:21:43');

-- resource: table
CREATE TABLE IF NOT EXISTS `auth`.`resource`
(
    `resource_code`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源编码',
    `parent_resource_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '资源编码',
    `resource_type`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源类型',
    `resource_name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名称',
    `resource_desc`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '资源描述',
    `created_at`           timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`           timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`resource_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='资源';

INSERT INTO auth.resource (resource_code, parent_resource_code, resource_type, resource_name, resource_desc, created_at,
                           updated_at)
VALUES ('dashboard', null, 'ROUTER', '控制台页面路由', '控制台页面路由', '2023-12-05 18:15:15', '2023-12-05 18:15:15');
INSERT INTO auth.resource (resource_code, parent_resource_code, resource_type, resource_name, resource_desc, created_at,
                           updated_at)
VALUES ('manager_resource', 'dashboard', 'COMPONENT', '资源管理', '资源管理', '2024-01-12 13:45:06',
        '2024-01-12 13:45:06');
INSERT INTO auth.resource (resource_code, parent_resource_code, resource_type, resource_name, resource_desc, created_at,
                           updated_at)
VALUES ('manager_role', 'dashboard', 'COMPONENT', '角色管理', '角色管理', '2024-01-12 13:45:06', '2024-01-12 13:45:06');
INSERT INTO auth.resource (resource_code, parent_resource_code, resource_type, resource_name, resource_desc, created_at,
                           updated_at)
VALUES ('manager_user', 'dashboard', 'COMPONENT', '用户管理', '用户管理', '2024-01-12 13:45:06', '2024-01-12 13:45:06');
INSERT INTO auth.resource (resource_code, parent_resource_code, resource_type, resource_name, resource_desc, created_at,
                           updated_at)
VALUES ('sys_config', 'dashboard', 'COMPONENT', '系统配置', '系统配置', '2024-01-12 13:45:06',
        '2024-01-12 13:45:06');


-- api: table
CREATE TABLE IF NOT EXISTS `auth`.`api`
(
    `api_code`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口编码',
    `api_uri`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口地址',
    `api_method`    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口方式 GET POST...',
    `api_name`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口名称',
    `api_desc`      varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '接口描述',
    `request_type`  varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '请求类型',
    `response_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '响应类型',
    `created_at`    timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`    timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`api_code`, `api_uri`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='接口';

insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('activation', '/auth/activation', 'POST', '激活用户', '激活用户账户', 'java.lang.Object',
        'com.songyu.domains.auth.aggregate.UserRegistered');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('newResource', '/manager/newResource', 'POST', '新增资源', '新增资源',
        'com.songyu.domains.auth.entity.Resource', 'com.songyu.domains.auth.entity.Resource');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('refreshAuth', '/auth/refreshAuth', 'POST', '刷新认证', '刷新用户客户端认证信息',
        'com.songyu.domains.auth.entity.UserClientTokenPair', 'com.songyu.domains.auth.entity.User');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('editResource', '/manager/editResource', 'POST', '编辑资源', '编辑资源',
        'com.songyu.domains.auth.entity.Resource', 'com.songyu.domains.auth.entity.Resource');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('login', '/auth/login', 'POST', '登入', '用户登录', 'com.songyu.domains.auth.entity.UserClientTokenPair',
        'com.songyu.domains.auth.aggregate.UserLogin');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('archiveResource', '/manager/archiveResource', 'POST', '归档资源', '归档资源', 'java.lang.Void',
        'com.songyu.domains.auth.entity.Resource');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('logout', '/auth/logout', 'POST', '登出', '退出登录', 'java.lang.Object',
        'com.songyu.domains.auth.entity.UserClientTokenPair');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('loaded', '/sysInfo/loaded', 'POST', '系统资源使用情况', '系统资源使用情况',
        'com.songyu.domains.sysInfo.entity.SysInfoLoad', 'java.lang.Void');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('captcha', '/auth/captcha', 'POST', '验证码', '获取验证码', 'com.songyu.components.captcha.Captcha',
        'com.songyu.domains.auth.aggregate.UserLogin');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('newRole', '/manager/newRole', 'POST', '新增角色', '新增角色', 'com.songyu.domains.auth.entity.Role',
        'com.songyu.domains.auth.entity.Role');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('editUser', '/manager/editUser', 'POST', '编辑用户', '编辑用户', 'com.songyu.domains.auth.entity.User',
        'com.songyu.domains.auth.entity.User');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('signup', '/auth/signup', 'POST', '注册', '用户注册', 'java.lang.Object',
        'com.songyu.domains.auth.entity.User');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('newUser', '/manager/newUser', 'POST', '新增用户', '新增用户', 'com.songyu.domains.auth.entity.User',
        'com.songyu.domains.auth.entity.User');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('userPage', '/manager/userPage', 'POST', '用户分页', '用户分页', 'java.lang.Object',
        'com.songyu.domains.auth.aggregate.UserPage');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('archiveUser', '/manager/archiveUser', 'POST', '归档用户', '归档用户', 'java.lang.Void',
        'com.songyu.domains.auth.entity.User');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('editRole', '/manager/editRole', 'POST', '编辑角色', '编辑角色', 'com.songyu.domains.auth.entity.Role',
        'com.songyu.domains.auth.entity.Role');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('archiveRole', '/manager/archiveRole', 'POST', '归档角色', '归档角色', 'java.lang.Void',
        'com.songyu.domains.auth.entity.Role');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('serverPublicKey', '/auth/serverPublicKey', 'POST', '服务端公钥', '获取服务端公钥', 'java.lang.String',
        'com.songyu.domains.auth.aggregate.AuthClient');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('verifyCaptcha', '/auth/verifyCaptcha', 'POST', '校验验证码', '校验验证码是否正确', 'java.lang.Object',
        'com.songyu.domains.auth.aggregate.AuthClient');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('resourcePage', '/manager/resourcePage', 'POST', '资源分页', '资源分页', 'java.lang.Object',
        'com.songyu.domains.auth.aggregate.ResourcePage');
insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type)
values ('rolePage', '/manager/rolePage', 'POST', '角色分页', '角色分页', 'java.lang.Object',
        'com.songyu.domains.auth.aggregate.RolePage');


-- user_client: table
CREATE TABLE IF NOT EXISTS `auth`.`user_client`
(
    `user_client_code`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户客户端编码唯一号',
    `user_client_refresh_token` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '用户客户端认证刷新令牌',
    `user_code`                 varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户唯一号编码',
    `created_at`                timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`user_client_code`),
    KEY `user_client_user_user_code_fk` (`user_code`),
    CONSTRAINT `user_client_user_user_code_fk` FOREIGN KEY (`user_code`) REFERENCES `user` (`user_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户客户端表';

-- user_role: table
CREATE TABLE IF NOT EXISTS `auth`.`user_role`
(
    `user_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户编码',
    `role_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
    PRIMARY KEY (`user_code`, `role_code`),
    KEY `user_role_role_role_code_fk` (`role_code`),
    CONSTRAINT `user_role_role_role_code_fk` FOREIGN KEY (`role_code`) REFERENCES `role` (`role_code`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `user_role_user_user_code_fk` FOREIGN KEY (`user_code`) REFERENCES `user` (`user_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户角色';

INSERT INTO auth.user_role (user_code, role_code)
VALUES ('DN007', '0');


-- role_resource: table
CREATE TABLE IF NOT EXISTS `auth`.`role_resource`
(
    `role_code`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色唯一号编码',
    `resource_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源唯一号编码',
    PRIMARY KEY (`resource_code`, `role_code`),
    KEY `role_resource_role_role_code_fk` (`role_code`),
    CONSTRAINT `role_resource_role_role_code_fk` FOREIGN KEY (`role_code`) REFERENCES `role` (`role_code`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `role_resource_resource_resource_code_fk` FOREIGN KEY (`resource_code`) REFERENCES `resource` (`resource_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='角色的资源';
INSERT INTO auth.role_resource (role_code, resource_code)
VALUES ('0', 'manager_user');
INSERT INTO auth.role_resource (role_code, resource_code)
VALUES ('0', 'manager_role');
INSERT INTO auth.role_resource (role_code, resource_code)
VALUES ('0', 'manager_resource');
INSERT INTO auth.role_resource (role_code, resource_code)
VALUES ('0', 'sys_config');
INSERT INTO auth.role_resource (role_code, resource_code)
VALUES ('0', 'dashboard');

-- resource_router: table
CREATE TABLE IF NOT EXISTS `auth`.`resource_router`
(
    `resource_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源唯一号编码',
    `path`          varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路由路径',
    `name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路由名称',
    `component`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路由组件',
    `meta`          json                                                          NOT NULL COMMENT '路由元信息',
    PRIMARY KEY (`resource_code`),
    KEY `resource_router_fk` (`resource_code`),
    CONSTRAINT `resource_router_resource_code_fk` FOREIGN KEY (`resource_code`) REFERENCES `resource` (`resource_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='前端路由资源';

INSERT INTO auth.resource_router (resource_code, path, name, component, meta)
VALUES ('dashboard', '/dashboard', 'dashboard', '/dashboard/index', '{
  "title": "仪表盘"
}');


-- resource_api: table
CREATE TABLE IF NOT EXISTS `auth`.`resource_api`
(
    `resource_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源唯一号编码',
    `api_code`      varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口编码',
    PRIMARY KEY (`resource_code`, `api_code`),
    KEY `resource_api_resource_api_code_fk` (`api_code`),
    CONSTRAINT `resource_api_api_api_code_fk` FOREIGN KEY (`api_code`) REFERENCES `api` (`api_code`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `resource_api_resource_resource_code_fk` FOREIGN KEY (`resource_code`) REFERENCES `resource` (`resource_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='资源接口';

INSERT INTO auth.resource_api (resource_code, api_code)
VALUES ('manager_resource', 'archiveResource');
INSERT INTO auth.resource_api (resource_code, api_code)
VALUES ('manager_role', 'archiveRole');
INSERT INTO auth.resource_api (resource_code, api_code)
VALUES ('manager_user', 'archiveUser');
INSERT INTO auth.resource_api (resource_code, api_code)
VALUES ('manager_resource', 'editResource');
INSERT INTO auth.resource_api (resource_code, api_code)
VALUES ('manager_role', 'editRole');
INSERT INTO auth.resource_api (resource_code, api_code)
VALUES ('manager_user', 'editUser');
INSERT INTO auth.resource_api (resource_code, api_code)
VALUES ('dashboard', 'loaded');
INSERT INTO auth.resource_api (resource_code, api_code)
VALUES ('manager_resource', 'newResource');
INSERT INTO auth.resource_api (resource_code, api_code)
VALUES ('manager_role', 'newRole');
INSERT INTO auth.resource_api (resource_code, api_code)
VALUES ('manager_user', 'newUser');
INSERT INTO auth.resource_api (resource_code, api_code)
VALUES ('manager_resource', 'resourcePage');
INSERT INTO auth.resource_api (resource_code, api_code)
VALUES ('manager_role', 'rolePage');
INSERT INTO auth.resource_api (resource_code, api_code)
VALUES ('manager_user', 'userPage');
