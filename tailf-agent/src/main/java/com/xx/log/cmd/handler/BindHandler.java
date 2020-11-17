package com.xx.log.cmd.handler;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.xx.log.cmd.CMDType;
import com.xx.log.cmd.type.BaseCmd;
import com.xx.log.cmd.type.BindPathCmd;
import com.xx.log.moniter.MonitorContext;
import com.xx.log.session.SessionContext;
import com.xx.log.util.ContentUtil;

@CMDType(cmd = BindPathCmd.class)
@Service
public class BindHandler implements CmdHandler {

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
			String lastContent = ContentUtil.readLastRows(bindPathCmd.getPathKey(), 10);
			bindPathCmd.getSession().getBasicRemote().sendText(lastContent);
			// 记录内容读取偏移量
			MonitorContext.setOffset(file.getAbsolutePath(), file.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}