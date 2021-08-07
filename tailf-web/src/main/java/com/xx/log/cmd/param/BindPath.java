package com.xx.log.cmd.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.websocket.Session;

@Data
@EqualsAndHashCode(callSuper = false)
public class BindPath extends BaseParam {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 绑定的路径
     */
    private String pathKey;
    /**
     * 模糊搜索的内容
     */
    private String searchKey;

}