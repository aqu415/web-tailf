package com.xx.log.cmd.handler;

import com.xx.log.cmd.Cmd;
import com.xx.log.cmd.param.BaseParam;
import com.xx.log.cmd.param.BindPath;
import com.xx.log.common.util.FileUtil;
import com.xx.log.monitor.local.MonitorContext;
import com.xx.log.properties.LogProperties;
import com.xx.log.session.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Cmd(param = BindPath.class)
@Service
@Slf4j
public class BindHandler implements CmdHandler {

    // logProperties
    @Resource
    private LogProperties logProperties;

    /**
     * 处理订阅目录请求
     *
     * @param baseParam 目录参数
     */
    @Override
    public void handleCMD(BaseParam baseParam) {
        BindPath bindPath = (BindPath) baseParam;

        File file = new File(bindPath.getPathKey());

        // 注册客户端、文件绝对路径、搜索关键字之间的关系
        SessionContext.subscribe(bindPath.getClientHost(), file.getAbsolutePath(), bindPath.getSearchKey());

        //监听目录(幂等)，注册监听处理逻辑
        MonitorContext.register(bindPath.getPathKey(), SessionContext::sendMsg);

        try {
            // 获得文件最后n行内容
            String lastContent = FileUtil.readLastRows(bindPath.getPathKey(), logProperties.getDefaultShowLineNum(),
                    bindPath.getSearchKey());
            bindPath.getSession().getBasicRemote().sendText(lastContent);
            // 记录内容读取偏移量
            MonitorContext.setOffset(file.getAbsolutePath(), file.length());
        } catch (IOException e) {
            log.error("handleCMD BindPath exception:", e);
        }
    }
}