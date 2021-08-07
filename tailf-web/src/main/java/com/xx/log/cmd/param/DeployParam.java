package com.xx.log.cmd.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeployParam extends BaseParam {

    // 部署的uuid 任务ID
    private String uuid;

    private String userName;

    private String ip;

    private String pw;

    private String remoteDir;
}
