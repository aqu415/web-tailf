package com.xx.log.controller;

import com.xx.log.pojo.Slave;
import com.xx.log.properties.LogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DefaultController {

    @Autowired
    private LogProperties logProperties;


    /**
     * 跳转到管理界面
     * @param model
     * @return
     */
    @RequestMapping("/manage")
    public String manage(Model model) {
        // 查询所有有心跳的服务端
        String keys = String.join(";", logProperties.getPath().keySet());
        model.addAttribute("path", keys);
        return "manage";
    }

    /**
     * 跳转首页
     *
     * @param model
     * @return
     */
    @RequestMapping("/main")
    public String main(Model model) {
        String keys = String.join(";", logProperties.getPath().keySet());
        model.addAttribute("path", keys);
        return "main";
    }

    /***
     * 跳转日志页
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("pathKey", request.getParameter("pathKey"));
        return "index";
    }
}
