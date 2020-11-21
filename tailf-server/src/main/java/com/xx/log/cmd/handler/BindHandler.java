package com.xx.log.cmd.handler;

import com.xx.log.cmd.CMDType;
import com.xx.log.cmd.type.BaseCmd;
import com.xx.log.cmd.type.BindPathCmd;
import com.xx.log.moniter.MonitorContext;
import com.xx.log.properties.LogProperties;
import com.xx.log.session.SessionContext;
import com.xx.log.util.ContentUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@CMDType(cmd = BindPathCmd.class)
@Service
public class BindHandler implements CmdHandler {

    @Resource
    private LogProperties logProperties;

    @Override
    public void handleCMD(BaseCmd baseCmd) {
        BindPathCmd bindPathCmd = (BindPathCmd) baseCmd;

        File file = new File(bindPathCmd.getPathKey());
        
        // 注册host-路径关系
        SessionContext.subscribe(bindPathCmd.getHost(), file.getAbsolutePath());

        //监听目录(幂等)
        MonitorContext.register(bindPathCmd.getPathKey());
        
        try {
        	// 获得文件最后几行内容
			String lastContent = ContentUtil.readLastRows(bindPathCmd.getPathKey(), logProperties.getDefaultShowLineNum());
			bindPathCmd.getSession().getBasicRemote().sendText(lastContent);
			// 记录内容读取偏移量
			MonitorContext.setOffset(file.getAbsolutePath(), file.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}