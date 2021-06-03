create database xblog;

use xblog;

create table `user`(
    `id`       int(11) unsigned zerofill auto_increment,
    `mail`     varchar(64),
    `phone`    varchar(64),
    `name`     varchar(64),
    `password` varchar(64),
    `head_img` varchar(128),
    `regdate`  timestamp,
    primary key(`id`),
    unique(`phone`, `password`),
    unique(`mail`, `password`)
) engine=InnoDB default charset=utf8;

create table `comment`(
    `id`              int(11) unsigned zerofill auto_increment,
    `user_id`         int(11) unsigned zerofill,
    `blog_id`         int(11) unsigned zerofill,
    `parent_id`       int(11) unsigned zerofill,
    `like_num`        int,
    `create_date`     timestamp,
    `content`         varchar(128), 
    `is_long_comment` tinyint(1) comment "是否为超过128个字的长评论，0-否，1-是",
    primary key(`id`),
    index(`blog_id`)
)engine=InnoDB default charset=utf8;

create table `long_comment`(
    `id`              int(11) unsigned zerofill,
    `content`         varchar(1024),
    primary key(`id`)
)engine=InnoDB default charset=utf8;

create table `mark`(
    `id`       int(11) unsigned zerofill auto_increment,
    `value`    varchar(16),
    `describe` varchar(64),
    primary key(`id`)
)engine=InnoDB default charset=utf8;

create table `blog_to_mark`(
    `id`      int(11) unsigned zerofill auto_increment,
    `blog_id` int(11) unsigned zerofill, 
    `mark_id` int(11) unsigned zerofill,
    primary key(`id`)
)engine=InnoDB default charset=utf8;

create table `cite`(
    `id`         int(11) unsigned zerofill auto_increment,
    `app_code`   int(11) unsigned zerofill comment "引用站点/应用的编码",
    `source_url` varchar(128) comment "引用源URL",
    primary key(`id`)
)engine=InnoDB default charset=utf8;

create table `category`(
    `id`          int(11) unsigned zerofill auto_increment,
    `parent_id`   int(11) unsigned zerofill,
    `name`        varchar(32),
    `create_date` timestamp, 
    primary key(`id`)
)engine=InnoDB default charset=utf8;

create table `blog`(
    `id`            int(11) unsigned zerofill auto_increment,
    `category_id`   int(11) unsigned zerofill,
    `cite_id`       int(11) unsigned zerofill,
    `title`         varchar(64),
    `abstract`      varchar(256),
    `cover`         varchar(128),
    `publish_date`  timestamp,
    `modified_date` timestamp,
    `file_name`     varchar(128),
    `visit_num`     int default 0,
    `comment_num`   int default 0,
    `state`         tinyint default 0 comment "0-草稿箱，1-已发布，2-垃圾箱",
    primary key(`id`),
    index(`category_id`),
    fulltext(`title`)
)engine=InnoDB default charset=utf8;

create table `uop_info`(
    `id`       int(11) unsigned zerofill auto_increment comment "user other platform info",
    `app_code` int(11) unsigned zerofill,
    `uop_id`   varchar(64) comment "用户在第三方平台中的id",
    primary key(`id`),
    unique(`uop_id`)
)engine=InnoDB default charset=utf8;

insert into `user` values(null, 'xinhos@aliyun.com', '15735657340', 'xinhos', 'FFFD6ECA040D4B8487D2992CC8915DD0', 'me.png', current_timestamp());
