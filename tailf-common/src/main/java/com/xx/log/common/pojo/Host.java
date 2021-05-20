package com.xx.log.common.pojo;

import lombok.Data;

@Data
public class Host implements java.io.Serializable{

    private static final long serialVersionUID = -6058279064468672210L;


    String ip;

    String port;
}
