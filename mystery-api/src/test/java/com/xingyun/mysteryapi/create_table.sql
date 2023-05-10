create table user_account
(
    id               bigint auto_increment
        primary key,
    token_amount     decimal(18, 2) default 0.00                 not null comment '余额',
    wait_claim_token decimal(18, 2) default 0.00                 null comment '待领取的币',
    permanent_power  decimal(18)    default 0                    null comment '永久算力',
    current_power    decimal(18)    default 0                    null comment '当前生效算力',
    wallet_address   varchar(50)    default ''                   null comment '钱包地址',
    invite_address   varchar(50)    default ''                   null comment '邀请钱包地址',
    mining_flag      bit            default b'0'                 null comment '是否开启挖矿',
    create_time      datetime(6)    default CURRENT_TIMESTAMP(6) null comment '创建时间',
    update_time      datetime(6)    default CURRENT_TIMESTAMP(6) null on update CURRENT_TIMESTAMP(6) comment '修改时间'
)
    comment '用户账户';

create table block_miner_record
(
    id            bigint auto_increment
        primary key,
    block_number  bigint         default 0                    not null comment '区块数',
    miner_address varchar(50)    default ''                   null comment '矿工地址',
    reward        decimal(18, 2) default 0.00                 null comment '出块奖励',
    create_time   datetime(6)    default CURRENT_TIMESTAMP(6) null comment '创建时间'
)
    comment '挖矿记录';

create table team
(
    id          bigint auto_increment
        primary key,
    leader      varchar(50)                                 not null comment '队长',
    team_name   varchar(50)    default ''                   null comment '队名',
    member_num  bigint         default 0                    null comment '队员数量',
    total_power decimal(20, 2) default 0.00                 null comment '团队总算力',
    create_time datetime(6)    default CURRENT_TIMESTAMP(6) null comment '创建时间',
    update_time datetime(6)    default CURRENT_TIMESTAMP(6) null on update CURRENT_TIMESTAMP(6) comment '修改时间'
)
    comment '团队';