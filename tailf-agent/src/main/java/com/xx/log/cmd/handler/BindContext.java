package com.xx.log.cmd.handler;

import com.xx.log.cmd.CMDType;
import com.xx.log.cmd.type.BaseCmd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Component
public class BindContext {

    @Autowired
    private CmdHandler[] cmdHandlers;

    public CmdHandler getByCmd(BaseCmd baseCmd) {
        for (CmdHandler cmdHandler : cmdHandlers) {
            CMDType cmdType = AnnotationUtils.findAnnotation(cmdHandler.getClass(), CMDType.class);
            if (cmdType != null && cmdType.cmd().equals(baseCmd.getClass())) {
                return cmdHandler;
            }
        }
        return null;
    }
}
