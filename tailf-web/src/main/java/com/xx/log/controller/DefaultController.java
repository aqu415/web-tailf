package com.xx.log.controller;

import com.xx.log.properties.LogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class DefaultController {

    @Autowired
    private LogProperties logProperties;


    /**
     * 文件下载
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("dl")
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getParameter("path");
        File file = new File(path);
        response.setHeader("content-disposition", "attachment;filename=" + file.getName());
        InputStream in = new FileInputStream(file);
        int len = 0;
        byte[] buffer = new byte[1024];
        OutputStream out = response.getOutputStream();

        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        in.close();
    }

    /**
     * 跳转到管理界面
     *
     * @param model
     * @return
     */
    @RequestMapping("/manage")
    public String manage(Model model) {
        // 查询所有有心跳的服务端
        String keys = String.join(";", logProperties.getPath().keySet());
        model.addAttribute("path", keys);
        return "main";
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
