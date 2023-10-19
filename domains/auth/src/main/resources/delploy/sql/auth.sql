# 认证数据库
CREATE SCHEMA IF NOT EXISTS `auth` COLLATE utf8mb4_general_ci;
USE `auth`;

-- user: table
CREATE TABLE IF NOT EXISTS `auth`.`user`
(
    `user_code`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户编码',
    `user_name`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名称',
    `user_desc`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户简介',
    `user_email`    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户邮箱',
    `user_tel`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户电话号码',
    `user_password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码',
    `created_at`    timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
    `updated_at`    timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`user_code`),
    UNIQUE KEY `user_email_uindex` (`user_email`) COMMENT '用户邮箱索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户信息表';

-- attribute: table
CREATE TABLE IF NOT EXISTS `auth`.`attribute`
(
    `attribute_code`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限属性唯一号编码',
    `attribute_name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限属性名称',
    `attribute_alias`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '权限属性别名',
    `attribute_desc`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '权限属性描述',
    `parent_attribute_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '父级权限属性唯一号编码',
    `created_at`            timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`            timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`attribute_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='权限属性';

-- resource_label: table
CREATE TABLE IF NOT EXISTS `auth`.`resource_label`
(
    `resource_label_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源标签编码',
    `resource_label_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源标签名称',
    `resource_label_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '资源标签描述',
    `created_at`          timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`          timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`resource_label_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='资源标签';

-- user_client: table
CREATE TABLE IF NOT EXISTS `auth`.`user_client`
(
    `user_client_code`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户客户端编码唯一号',
    `user_client_token`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '用户客户端认证令牌',
    `user_client_key`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '用户客户端密钥',
    `user_client_fingerprint` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '用户客户端指纹',
    `user_code`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户唯一号编码',
    `created_at`              timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`              timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`user_client_code`),
    KEY `user_client_user_user_code_fk` (`user_code`),
    CONSTRAINT `user_client_user_user_code_fk` FOREIGN KEY (`user_code`) REFERENCES `user` (`user_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户客户端表';

-- user_attribute: table
CREATE TABLE IF NOT EXISTS `auth`.`user_attribute`
(
    `user_code`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户编码',
    `attribute_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限属性编码',
    PRIMARY KEY (`user_code`, `attribute_code`),
    KEY `user_attribute_attribute_attribute_code_fk` (`attribute_code`),
    CONSTRAINT `user_attribute_attribute_attribute_code_fk` FOREIGN KEY (`attribute_code`) REFERENCES `attribute` (`attribute_code`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `user_attribute_user_user_code_fk` FOREIGN KEY (`user_code`) REFERENCES `user` (`user_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户权限属性';

-- attribute_resource_label: table
CREATE TABLE IF NOT EXISTS `auth`.`attribute_resource_label`
(
    `attribute_code`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限属性唯一号编码',
    `resource_label_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源标签唯一号编码',
    PRIMARY KEY (`resource_label_code`, `attribute_code`),
    KEY `attribute_resource_label_attribute_attribute_code_fk` (`attribute_code`),
    CONSTRAINT `attribute_resource_label_attribute_attribute_code_fk` FOREIGN KEY (`attribute_code`) REFERENCES `attribute` (`attribute_code`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `attribute_resource_label_resource_label_resource_label_code_fk` FOREIGN KEY (`resource_label_code`) REFERENCES `resource_label` (`resource_label_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='权限属性的资源标签';

