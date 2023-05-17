package com.xingyun.mysteryapi.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeamVo {

    @ApiModelProperty("我的队名")
    private String myTeamName;

    @ApiModelProperty("我的队伍队长地址")
    private String myTeamLeader;

    @ApiModelProperty("我的队伍总算力")
    private BigDecimal myTeamPower;

    @ApiModelProperty("从我的队伍中获取的算力")
    private BigDecimal powerFromMyTeam;

    @ApiModelProperty("我的队伍成员数")
    private Long myTeamMemberNum;

    @ApiModelProperty("加入的队名")
    private String joinTeamName;

    @ApiModelProperty("加入的队伍总算力")
    private BigDecimal joinTeamPower;

    @ApiModelProperty("从加入的队伍中获取的算力")
    private BigDecimal powerFromJoinTeam;

    @ApiModelProperty("加入的队伍成员数")
    private Long joinTeamMemberNum;

    @ApiModelProperty("加入的队伍队长地址")
    private String joinTeamLeader;
}
