package com.xx.log.endpoint;

import java.net.InetSocketAddress;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.xx.log.cmd.handler.BindContext;
import com.xx.log.cmd.handler.CmdHandler;
import com.xx.log.cmd.type.BindPathCmd;
import com.xx.log.session.SessionContext;
import com.xx.log.util.SessionUtil;
import com.xx.log.util.SpringUtil;

@ServerEndpoint(value = "/websocket01")
@Component
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
        System.out.println("Open");
    }

    /**
     * 获得host,端口
     * @param session
     * @return
     */
    private String getHost(Session session) {
        InetSocketAddress fieldInstance = (InetSocketAddress) SessionUtil.getFieldInstance(session.getAsyncRemote(), "base#socketWrapper#socket#sc#remoteAddress");
        return fieldInstance.toString();
    }


    /**
     * 连接关闭调用的方法
     *
     * @throws Exception
     */
    @OnClose
    public void onClose(Session session) throws Exception {
        System.out.println("Close");
        String host = this.getHost(session);
        com.xx.log.session.SessionContext.unRegister(host);
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
            System.out.println("接收到数据" + message);
        } else {
            return;
        }
        String host = this.getHost(session);
        if (host != null) {
        	// 欢迎信息
        	session.getBasicRemote().sendText("hello this is server;");
        	
        	// bind
            if (message.contains("bind")) {
                BindPathCmd cmd = JSONObject.parseObject(message, BindPathCmd.class);
                cmd.setHost(host);
                cmd.setSession(session);
                CmdHandler handler = SpringUtil.getBean(BindContext.class).getByCmd(cmd);
                handler.handleCMD(cmd);
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
