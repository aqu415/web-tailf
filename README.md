## tailf

一个类似linux tail的web版本日志滚动查看工具

码云(https://gitee.com/aqu415/tailf)

github同步(https://github.com/aqu415/tailf)

CSDN(https://blog.csdn.net/Aqu415/article/details/114419320)

### 背景
> 前一段时间由于项目原因，需要经常上服务器看日志；由于没有公共的页面查看与下载，就需要频繁的登录服务器，然后执行一堆命令，最后才能看到日志；
> 这个过程太繁琐痛苦，网上搜了一圈没有合适的工具，于是自己写了一个简单的日志实时查看的工具；

### 原理
1. Websocket + FileAlterationListenerAdaptor：通过监听文件变化，再增量获得变化的内容通过websocket发送给浏览器客户端
2. 多服务器场景下master与slave之间netty连接（异常后重连逻辑未开发）

### gitee地址
https://gitee.com/aqu415/tailf

### 特性
1. 支持界面实时查看、搜索日志功能
2. 支持文件下载
3. 支持master、slave模式，通过master界面直接查看所有服务器日志（省去多个服务器需要记住多个访问链接的问题）   
4. slave自动部署（未开发）
5. 日志智能分析（未开发）

### 本地调试
+ 本地调试如果需要监听多个目录可以通过以下配置，多个目录间以英文 ; 分隔
+ 然后执行 com.xx.log.LogApplication.main 方法即可本地启动;

注：配置文件里的监听目录配置是默认配置，可被启动参数覆盖(如：IDE配置启动参数，或者打成jar包后用 jar 命令启动后的参数)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210520095121238.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FxdTQxNQ==,size_16,color_FFFFFF,t_70)

### 打包
依赖spring-boot打包插件，打成可执行jar包

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210312111021171.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FxdTQxNQ==,size_16,color_FFFFFF,t_70)

### 服务器上jar包启动

支持监听多个目录
```
linux后台运行:
nohup java -jar tailf-web-1.0-xxx.jar "/usr/logs/tomcat1" "/usr/logs/tomcat2" &
or
java -jar tailf-web-1.0-xxx.jar "/usr/logs/tomcat1" "/usr/logs/tomcat2" &

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

### master-slave模式
如果有多台服务器需要进行日志查看，但是又不想记住多个服务器访问地址则可以使用master-slave模式，具体操作如下：

1、master配置
+ 配置当前服务器角色是master
+ 配置master服务器的内网IP（与slave通信会使用）
+ 配置master netty监听端口

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210520091342162.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FxdTQxNQ==,size_16,color_FFFFFF,t_70)

2、slave配置
+ 这三个配置除了角色配置成 slave,其他两项都配置成一样的。如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210520091919488.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FxdTQxNQ==,size_16,color_FFFFFF,t_70)

3、启动服务
+ 首先启动master
  
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210520092236981.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FxdTQxNQ==,size_16,color_FFFFFF,t_70)

+ 再启动slave（另外启动一个IDE）

master控制台打印slave注册信息：
![在这里插入图片描述](https://img-blog.csdnimg.cn/2021052009254766.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FxdTQxNQ==,size_16,color_FFFFFF,t_70)
  
+ 界面效果：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210520092451791.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FxdTQxNQ==,size_16,color_FFFFFF,t_70)

文件支持下载和搜索