package com.xx.log.netty.invoke;

import com.xx.log.common.pojo.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.CountDownLatch;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvokeResult<T> {

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    private Class resultMark;

    private T data;

    public T getData(){
        return data;
    }
}
