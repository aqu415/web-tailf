## tailf

一个类似linux tail的web版本日志查看工具

提交在github(https://github.com/aqu415/tailf)

再同步到码云(https://gitee.com/aqu415/tailf)

### 背景
> 前一段时间由于项目原因，需要经常上服务器看日志；由于没有公共的页面查看与下载，就需要频繁的登录服务器，然后执行一堆命令，最后才能看到日志；
> 这个过程太痛苦，网上搜了一圈没有合适的工具，于是自己写了一个简单的日志实时查看的工具，取名叫 Web-tailf

### 原理
> Websocket + FileAlterationListenerAdaptor

### gitee地址
https://gitee.com/aqu415/tailf

### 本地调试
+ 本地调试如果需要监听多个目录可以通过以下配置，多个目录间以 ; 分隔
+ 然后执行 com.xx.log.LogApplication.main 方法即可本地启动;

注：配置文件里的监听目录配置是默认配置，可被启动参数覆盖(如：IDE配置启动参数，或者打成jar包后用 jar 命令启动后的参数)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210320100926895.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FxdTQxNQ==,size_16,color_FFFFFF,t_70)

### 打包
依赖spring-boot打包插件，打成可执行jar包

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210312111021171.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FxdTQxNQ==,size_16,color_FFFFFF,t_70)

### 服务器上jar包启动

支持监听多个目录
```
linux后台运行:
nohup java -jar tailf-web-1.0-xxx.jar "/usr/logs/tomcat1" "/usr/logs/tomcat2" &

windows窗口启动
java -jar tailf-web-1.0-xxx.jar "/usr/logs/tomcat1" "/usr/logs/tomcat2"
```

### 效果
#### Extjs风格
+  访问地址：http://localhost:10086/

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210419145343483.gif)

#### 文件下载
在树节点上右键即可弹出下载菜单

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210420114237971.gif)
