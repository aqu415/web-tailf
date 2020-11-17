package com.xx.log.controller;

import com.xx.log.properties.LogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DefaultController {

    @Autowired
    private LogProperties logProperties;

    /**
     * 跳转首页
     *
     * @return
     */
    @RequestMapping("/main")
    public String main(Model model) {
        String keys = String.join(";", logProperties.getPath().keySet());
        model.addAttribute("path", keys);
        return "main";
    }

    @RequestMapping("/index")
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("pathKey", request.getParameter("pathKey"));
        return "index";
    }
}
