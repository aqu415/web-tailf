package com.xx.log.common.pojo.message;

import com.xx.log.common.enums.MessageHead;
import lombok.Data;

@Data
public class Markable implements java.io.Serializable {

    private Long threadId;

    private MessageHead messageHead;

}
