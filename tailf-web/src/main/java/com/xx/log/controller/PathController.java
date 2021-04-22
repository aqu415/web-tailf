package com.xx.log.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xx.log.pojo.ExtPath;
import com.xx.log.pojo.Path;
import com.xx.log.properties.LogProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenyang
 * @date 2020.10.26 11:04
 */
@RestController
public class PathController {

    @Resource
    private LogProperties logProperties;

    /**
     * 获得子目录、文件
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/path")
    public String getChildren(HttpServletRequest request) {

        String key = request.getParameter("key");
        if (logProperties.getPath().containsKey(key)) {
            key = logProperties.getPath().get(key);
        }

        ArrayList<Path> list = this.getZtreePath(key);
        list.sort(Comparator.reverseOrder());
        return JSONObject.toJSONString(list);
    }

    /**
     * 获得子目录或者文件
     *
     * @param pathStr
     * @return
     */
    private ArrayList<Path> getZtreePath(String pathStr) {
        ArrayList<Path> list = new ArrayList<Path>();
        File file = new File(pathStr);
        if (file.exists() && file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (File f : listFiles) {
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
     * 获得子目录、文件
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/extPath")
    public String getExtChildren(HttpServletRequest request) {

        //{treeList:[{text:'总公司1',id:'123',children:[{text:'部门1',id:'234',leaf:true}]},{text:'总公司2',id:'258'}]}

        List<ExtPath> collect = null;
        String node = request.getParameter("node");
        // 校验是否根节点
        if ("root".equals(node)) {
            HashMap<String, String> all = logProperties.getPath();
            Set<String> rootKeys = all.keySet();
            collect = rootKeys.stream().map(key -> ExtPath.builder()
                    .text(key)
                    .id(all.get(key))
                    .absPath(all.get(key))
                    .build()).collect(Collectors.toList());
        } else {
            // 非根节点
            collect = this.getExtPath(node);
        }

        // 排序
        collect.sort(Comparator.reverseOrder());

        // 组装返回值
        Map<String, List<ExtPath>> map = new HashMap<>();
        map.put("treeList", collect);
        return JSON.toJSONString(map);
    }

    /**
     * extjs框架获得子目录或者文件
     *
     * @param pathStr
     * @return
     */
    private ArrayList<ExtPath> getExtPath(String pathStr) {
        ArrayList<ExtPath> list = new ArrayList<>();
        File file = new File(pathStr);
        if (file.exists() && file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (File f : listFiles) {
                ExtPath path = ExtPath.builder()
                        .id(f.getAbsolutePath().replace("\\", "/"))
                        .text(f.getName())
                        .absPath(f.getAbsolutePath().replace("\\", "/"))
                        .flag(UUID.randomUUID().toString().replace("-", ""))
                        .leaf(String.valueOf(!f.isDirectory()))
                        .build();
                list.add(path);
            }
        }
        return list;
    }
}
