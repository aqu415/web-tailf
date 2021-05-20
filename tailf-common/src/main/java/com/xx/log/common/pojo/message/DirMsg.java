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
public class DirMsg extends Message {

    @Override
    protected void initMessageHead() {
        this.setMessageHead(MessageHead.DIR);
    }

    private String path;
}
