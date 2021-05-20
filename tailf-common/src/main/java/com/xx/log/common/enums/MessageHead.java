package com.xx.log.common.enums;

public enum MessageHead implements java.io.Serializable {
    //client->server
    REGISTER, HEART_BEAT,

    //server->client
    IN_ACTIVE,DIR,

    //RESPONSE
    RESPONSE
}
