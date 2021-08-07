package com.xx.log.ssh2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ssh2Result implements java.io.Serializable {
    private boolean success;
    private String msg;

    public static Ssh2Result success() {
        return new Ssh2Result(true, "部署完毕");
    }
}
