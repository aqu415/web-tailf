package com.xx.log.controller;

import com.xx.log.properties.LogProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Controller
public class ViewController {

    /**
     * logProperties
     */
    @Resource
    private LogProperties logProperties;

    /**
     * 跳转ext主页
     *
     * @param model
     * @return
     */
    @GetMapping(path = {"/", "/e"})
    public String ext(Model model) {
        return "ext";
    }

    /***
     * 跳转日志展示页
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/d")
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("pathKey", request.getParameter("pathKey"));
        return "detail";
    }

    /**
     * 文件下载
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/dl")
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getParameter("path");
        File file = new File(path);
        response.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(file.getName(), "utf-8") + "\"");
        InputStream in = new FileInputStream(file);
        int len = 0;
        byte[] buffer = new byte[1024];
        OutputStream out = response.getOutputStream();

        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.flush();
        out.close();
    }
}
