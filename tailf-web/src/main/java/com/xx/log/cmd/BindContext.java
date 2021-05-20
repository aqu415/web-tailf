package com.xx.log.cmd;

import com.xx.log.cmd.handler.CmdHandler;
import com.xx.log.cmd.param.BaseParam;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BindContext {

    // cmdHandlers
    @Resource
    private CmdHandler[] cmdHandlers;

    /**
     * getByCmd
     *
     * @param baseParam
     * @return
     */
    public CmdHandler getByCmd(BaseParam baseParam) {
        for (CmdHandler cmdHandler : cmdHandlers) {
            Cmd cmd = AnnotationUtils.findAnnotation(cmdHandler.getClass(), Cmd.class);
            if (cmd != null && cmd.param().equals(baseParam.getClass())) {
                return cmdHandler;
            }
        }
        return null;
    }
}
