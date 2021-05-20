package com.xx.log.message.handler.client;

import com.xx.log.common.enums.MessageHead;
import com.xx.log.common.pojo.message.DirMsg;
import com.xx.log.common.pojo.message.Message;
import com.xx.log.common.pojo.result.DirResult;
import com.xx.log.common.util.ConstanceUtil;
import com.xx.log.message.handler.BusinessHandler;
import com.xx.log.netty.RemotingUtil;
import com.xx.log.netty.handler.Msg;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 获得子目录列表的处理器
 */
@Msg(head = MessageHead.DIR, body = DirMsg.class)
@Slf4j
@Component
public class DirCmdHandler implements BusinessHandler {

    @Override
    public void handleMessage(ChannelHandlerContext ctx, Message message) {
        log.debug("DirCmdHandler.handleMessage:" + message);
        DirMsg dirMsg = (DirMsg) message;
        String path = dirMsg.getPath();

        // 获得目录下列表
        StringBuilder info = new StringBuilder();
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File f : listFiles) {
                    //path|name|boolean(DIR)
                    info.append(f.getAbsolutePath().replace("\\", "/")).append(ConstanceUtil.VERTICAL_DELIMITER)
                            .append(f.getName()).append(ConstanceUtil.VERTICAL_DELIMITER)
                            .append(!f.isDirectory()).append(ConstanceUtil.SEMICOLON);
                }
            }
        }
        // 返回值
        DirResult result = DirResult.builder().body(info.toString()).build();
        result.setThreadId(dirMsg.getThreadId());
        RemotingUtil.writeAndFlushMessage(ctx, result);
    }
}
