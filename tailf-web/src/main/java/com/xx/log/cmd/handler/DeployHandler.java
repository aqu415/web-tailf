package com.xx.log.cmd.handler;

import com.alibaba.fastjson.JSONObject;
import com.xx.log.cmd.Cmd;
import com.xx.log.cmd.param.BaseParam;
import com.xx.log.cmd.param.DeployParam;
import com.xx.log.common.util.ThreadPoolUtil;
import com.xx.log.properties.LogProperties;
import com.xx.log.session.SessionContext;
import com.xx.log.ssh2.Ssh2Result;
import com.xx.log.ssh2.Ssh2Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.Session;

@Cmd(param = DeployParam.class, cmd = "deploy")
@Service
@Slf4j
public class DeployHandler implements CmdHandler {

    @Resource
    private LogProperties logProperties;

    @Override
    public void handleMessage(BaseParam baseParam) {
        log.info("部署开始,{}", baseParam);
        final DeployParam deployParam = (DeployParam) baseParam;
        ThreadPoolUtil.submit(() -> {
                    Session session = baseParam.getSession();
                    try {
                        SessionContext.sendMsg(session, "部署开始");
                        //  获得安装包位置，上传文件
                        String localFilePath = this.logProperties.getAttachmentFilepath() + this.logProperties.getAttachmentFileName();
                        Ssh2Result result = Ssh2Utils.upLoad(deployParam.getRemoteDir(), localFilePath, deployParam.getUserName(), deployParam.getPw(), deployParam.getIp());
                        log.debug("文件上传结果：{}", result);
                        SessionContext.sendMsg(session, JSONObject.toJSONString(result));

                        // 执行命令行启动应用

                    } catch (Exception e) {
                        log.error("部署异常", e);
                        SessionContext.sendMsg(session, e.getLocalizedMessage());
                    }
                }
        );
    }
}