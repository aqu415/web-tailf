package com.xx.log.websocket;

import com.xx.log.cmd.BindContext;
import com.xx.log.cmd.handler.CmdHandler;
import com.xx.log.cmd.param.BaseParam;
import com.xx.log.common.util.MixUtil;
import com.xx.log.session.SessionContext;
import com.xx.log.util.SessionUtil;
import com.xx.log.util.SpringUtil;
import io.undertow.websockets.jsr.UndertowSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.net.InetSocketAddress;

@ServerEndpoint(value = "/websocket01")
@Component
@Slf4j
public class DefaultEndpoin {

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数
     * @throws Exception
     */
    @OnOpen
    public void onOpen(Session session) throws Exception {
        String host = this.getHost(session);
        if (host != null) {
            SessionContext.register(host, session);
        }
        log.info("Open:" + host);
    }

    /**
     * 获得host,端口等(会话唯一标记)
     *
     * @param session
     * @return
     */
    private String getHost(Session session) {
        if (session instanceof UndertowSession) {
            UndertowSession undertowSession = (UndertowSession) session;
            return undertowSession.getId();
        } else {
            InetSocketAddress fieldInstance = (InetSocketAddress) SessionUtil.getFieldInstance(session.getAsyncRemote(), "base#socketWrapper#socket#sc#remoteAddress");
            return fieldInstance.toString();
        }
    }


    /**
     * 连接关闭调用的方法
     *
     * @throws Exception
     */
    @OnClose
    public void onClose(Session session) throws Exception {
        String host = this.getHost(session);
        com.xx.log.session.SessionContext.unRegister(host);
        log.info("close:" + host);
    }

    /**
     * 收到消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     * @throws Exception
     */
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        if (message != null) {
            log.info("接收到数据:" + message);
            String clientHost = this.getHost(session);
            if (clientHost != null) {
                BaseParam cmd = MixUtil.jsonStr2Obj(message, BaseParam.class);
                BindContext bindContext = SpringUtil.getBean(BindContext.class);
                CmdHandler handler = bindContext.getByCmd(cmd);
                BaseParam param = bindContext.buildParam(handler, message);
                param.setSession(session);
                param.setClientHost(clientHost);
                if (handler != null) {
                    handler.handleMessage(param);
                } else {
                    log.error("未找到消息处理器");
                }
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        if (session.isOpen()) {
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        error.printStackTrace();
    }
}
