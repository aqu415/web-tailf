package com.xx.log.controller;


import com.xx.log.session.SessionContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author chenyang
 * @date 2020.10.26 11:04
 */
@RestController
public class AdminController {

    /**
     * broadcast
     *
     * @return
     */
    @RequestMapping("/send")
    public String send(String message) throws Exception {
        SessionContext.sendMsg(message);
        return "success " + new Date();
    }
}
