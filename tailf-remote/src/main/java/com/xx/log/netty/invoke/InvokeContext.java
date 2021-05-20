package com.xx.log.netty.invoke;

import com.alibaba.fastjson.JSONObject;
import com.xx.log.common.pojo.message.Message;
import com.xx.log.common.util.ConstanceUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class InvokeContext {

    static final Map<Long/*threadId*/, InvokeResult> threads = new ConcurrentHashMap<>();

    public static <T> T invoke(ChannelHandlerContext context, Message param, Class<T> resultClass) {

        InvokeResult<T> result = new InvokeResult<T>();
        result.setResultMark(resultClass);
        Long threadId = Thread.currentThread().getId();
        threads.put(threadId, result);
        try {
            param.setThreadId(threadId);
            context.writeAndFlush(JSONObject.toJSONString(param) + ConstanceUtil.DECODER_DELIMITER);
            boolean res = result.getCountDownLatch().await(5, TimeUnit.SECONDS);
            if (!res) {
                throw new RuntimeException("timeout:" + param);
            }
        } catch (InterruptedException e) {
            log.error("com.xx.log.netty.invoke.InvokeContext.invoke", e);
        }
        if (result.getData() == null) {
            throw new RuntimeException("invoke result is null:" + param);
        }
        return result.getData();
    }

    public static InvokeResult getInvokeResult(Long threadId) {
        return threads.get(threadId);
    }
}
