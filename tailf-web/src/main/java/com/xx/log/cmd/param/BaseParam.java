package com.xx.log.cmd.param;

import lombok.Data;

import javax.websocket.Session;

@Data
public class BaseParam implements java.io.Serializable {
    /**
     * cmd
     */
    private String cmd;

    /**
     * 当前webSocket session
     */
    private Session session;

    /**
     * 客户端IP
     */
    private String clientHost;
}
