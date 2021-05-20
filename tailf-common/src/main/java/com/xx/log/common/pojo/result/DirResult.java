package com.xx.log.common.pojo.result;

import com.xx.log.common.enums.MessageHead;
import com.xx.log.common.pojo.message.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirResult extends Message {

    @Override
    protected void initMessageHead() {
        this.setMessageHead(MessageHead.RESPONSE);
    }

    private String body;
}
