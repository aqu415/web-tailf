package com.xx.log.cmd;

import com.xx.log.cmd.handler.CmdHandler;
import com.xx.log.cmd.param.BaseParam;
import com.xx.log.common.util.MixUtil;
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
     * @param baseParam baseParam
     * @return CmdHandler
     */
    public CmdHandler getByCmd(BaseParam baseParam) {
        for (CmdHandler cmdHandler : cmdHandlers) {
            CMD cmd = AnnotationUtils.findAnnotation(cmdHandler.getClass(), CMD.class);
            if (cmd != null && cmd.cmd().equals(baseParam.getCmd())) {
                return cmdHandler;
            }
        }
        return null;
    }

    /**
     * 将字符串转换为对象参数
     *
     * @param handler handler
     * @param message message
     * @return BaseParam
     */
    public BaseParam buildParam(CmdHandler handler, String message) {
        CMD cmd = AnnotationUtils.findAnnotation(handler.getClass(), CMD.class);
        return MixUtil.jsonStr2Obj(message, cmd.param());
    }
}
