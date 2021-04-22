package com.xx.log.controller;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.xx.log.pojo.Path;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.xx.log.properties.LogProperties;
import com.xx.log.session.SessionContext;

/**
 * @author chenyang
 * @date 2020.10.26 11:04
 */
@RestController
public class MockController {

    /**
     * mockSend
     *
     * @return
     */
    @RequestMapping("/mockSend")
    public String mockSend() throws Exception {
        // SessionContext.sendMsg("hello world-" + System.currentTimeMillis());

        FileInputStream fis = new FileInputStream("d:/u1.sql");
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        String line = null;
        while ((line = br.readLine()) != null) {
            SessionContext.sendMsg(line);
        }
        isr.close();
        fis.close();
        return "success " + new Date();
    }

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
