package com.xx.log.common.pojo.message;

import com.xx.log.common.enums.MessageHead;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterMsg extends Message implements java.io.Serializable {

    @Override
    protected void initMessageHead() {
        this.setMessageHead(MessageHead.REGISTER);
    }

    private String host;
    private String path;
}
