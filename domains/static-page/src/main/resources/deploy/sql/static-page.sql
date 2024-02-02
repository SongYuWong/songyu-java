# 静态页数据库
CREATE SCHEMA IF NOT EXISTS `static_page` COLLATE utf8mb4_general_ci;
USE `static_page`;

create table if not exists `static_page`.`article`
(
    `title` var
)