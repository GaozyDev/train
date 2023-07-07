create database train_member default character set utf8mb4 collate utf8mb4_unicode_ci;

drop table if exists `member`;

create table `member` (
    `id` bigint not null comment 'id',
    `mobile` varchar(11) comment '手机号',
    primary key (`id`),
    unique key `mobile_unique` (`mobile`)
) engine = innodb default charset = utf8mb4 comment = '会员';

