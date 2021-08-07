package com.xx.log.cmd;

import com.xx.log.cmd.param.BaseParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Cmd {
    
    Class<? extends BaseParam> param();

    String cmd();
}