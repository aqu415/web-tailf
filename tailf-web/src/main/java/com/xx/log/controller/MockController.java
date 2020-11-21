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

	@Resource
	private LogProperties logProperties;
	
	/**
	 * 获得子目录、文件
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/path")
	public String getChildren(HttpServletRequest request) {
		
		String key = request.getParameter("key");
		if(logProperties.getPath().containsKey(key)) {
			key = logProperties.getPath().get(key);
		}
		
		ArrayList<Path> list = this.getPath(key);
		list.sort(Comparator.reverseOrder());
		return JSONObject.toJSONString(list);
	}
	
	/**
	 * 获得子目录或者文件
	 * @param pathStr
	 * @return
	 */
	private ArrayList<Path> getPath (String pathStr){
		ArrayList<Path> list = new ArrayList<Path>();
		File file = new File(pathStr);
		if(file.exists() && file.isDirectory()) {
			File[] listFiles = file.listFiles();
			for(File f : listFiles) {
				Path path = Path.builder()
						.name(f.getName())
						.path(f.getAbsolutePath().replace("\\", "/"))
						.isParent(String.valueOf(f.isDirectory()))
						.build();
				list.add(path);
			}
		}
		return list;
	}
	
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
