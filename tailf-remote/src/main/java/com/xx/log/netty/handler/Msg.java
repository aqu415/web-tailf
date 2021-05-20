package com.xx.log.netty.handler;

import com.xx.log.common.enums.MessageHead;
import com.xx.log.common.pojo.message.Message;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Msg {
    // head
    MessageHead head();

    // body
    Class<? extends Message> body() default Message.class;
}
