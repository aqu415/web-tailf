package com.xx.log.common.pojo.message;

import com.xx.log.common.enums.MessageHead;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息对象
 */
@Data
public abstract class Message extends Markable implements java.io.Serializable {

    public Message(){
        this.initMessageHead();
    }

    protected abstract void initMessageHead();
}
