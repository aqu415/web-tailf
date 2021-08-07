package com.xx.log.controller;


import com.alibaba.fastjson.JSON;
import com.xx.log.common.pojo.ExtPath;
import com.xx.log.common.pojo.message.DirMsg;
import com.xx.log.common.pojo.result.DirResult;
import com.xx.log.common.util.ConstanceUtil;
import com.xx.log.common.util.HashUtil;
import com.xx.log.netty.invoke.InvokeContext;
import com.xx.log.properties.LogProperties;
import com.xx.log.session.Global;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * @author chenyang
 * @date 2020.10.26 11:04
 */
@RestController
@Slf4j
public class RestDataController {

    // logProperties
    @Resource
    private LogProperties logProperties;

    /**
     * 获得子目录、文件
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/extPath")
    public String getExtChildren(HttpServletRequest request) {

        //{treeList:[{text:'总公司1',id:'123',children:[{text:'部门1',id:'234',leaf:true}]},{text:'总公司2',id:'258'}]}

        List<ExtPath> collection = null;
        String node = request.getParameter("node");

        // 校验是否根节点
        if ("root".equals(node)) {
            // 获得所有监听的路径
            final List<ExtPath> col = new ArrayList<>();
            Global.getRegisterInfo().forEach((k, v) -> {
                String[] split = v.split(";");
                for (String path : split) {
                    String txt = k + ConstanceUtil.RIGHT_ARROW + path;
                    col.add(ExtPath.builder().text(txt).leaf("false")
                            .id(txt) // id在展开节点时会通过node参数带回
                            .build());
                }
            });
            collection = col;
        } else {
            // 非根节点
            collection = this.getExtPath(node);
        }

        // 排序
        collection.sort(Comparator.reverseOrder());

        // 组装返回值
        Map<String, List<ExtPath>> map = new HashMap<>();
        map.put("treeList", collection);
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
        String[] tag = pathStr.split(ConstanceUtil.RIGHT_ARROW);

        // 检查是否有slave连接
        ChannelHandlerContext channelContext = Global.getChannelContext(tag[0]);
        if (channelContext != null) {
            DirMsg req = DirMsg.builder().path(tag[1]).build();
            DirResult result = InvokeContext.invoke(channelContext, req, DirResult.class);
            String body = result.getBody();
            String[] item = body.split(ConstanceUtil.SEMICOLON);
            for (String s : item) {

                // com.xx.log.message.handler.client.DirCmdHandler.handleMessage
                // path|name|boolean(dir)
                String[] attrs = s.split(ConstanceUtil.VERTICAL_DELIMITER);
                String h_id = "tag[0] + ConstanceUtil.RIGHT_ARROW + attrs[0]";
                ExtPath path = ExtPath.builder()
                        .id(h_id) // id在展开节点时会通过node参数带回
                        .text(attrs[1])
                        .absPath(attrs[0])
                        .idMD5(HashUtil.getMD5String(h_id))
                        .leaf(attrs[2])
                        .build();
                list.add(path);
            }
        } else {
            File file = new File(tag[1]);
            if (file.exists() && file.isDirectory()) {
                File[] listFiles = file.listFiles();
                for (File f : listFiles) {
                    String h_id = tag[0] + ConstanceUtil.RIGHT_ARROW + f.getAbsolutePath().replace("\\", "/");
                    ExtPath path = ExtPath.builder()
                            .id(h_id) // id在展开节点时会通过node参数带回
                            .text(f.getName())
                            .absPath(f.getAbsolutePath().replace("\\", "/"))
                            .idMD5(HashUtil.getMD5String(h_id))
                            .leaf(String.valueOf(!f.isDirectory()))
                            .build();
                    list.add(path);
                }
            }
        }
        return list;
    }


    /**
     * 获得deploykey
     *
     * @return String
     */
    @RequestMapping(method = RequestMethod.GET, value = "/deployKey")
    public String deployKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
