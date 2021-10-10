package com.xx.log.ssh2;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import io.netty.util.internal.StringUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Ssh2 工具
 */
@UtilityClass
@Slf4j
public class Ssh2Utils {

    private int SSH_PORT = 22;

    public static Connection getConnect(String user, String password, String ip) throws IOException {
        Connection conn = new Connection(ip, SSH_PORT);
        conn.connect();
        conn.authenticateWithPassword(user, password);
        log.debug("连接:{} success", ip);
        return conn;
    }

    public static String exeCmd(String cmd, String user, String password, String ip) {
        String line = null;
        Connection connection = null;
        Session session = null;
        try {
            connection = getConnect(user, password, ip);
            session = connection.openSession();
            session.execCommand(cmd);
            InputStream in = new StreamGobbler(session.getStdout());
            BufferedReader brs = new BufferedReader(new InputStreamReader(in));
            line = brs.readLine();
//	        logger.info(line);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            connection.close();
        }
        return line;
    }

    /**
     * upLoad to Linux
     *
     * @param remoteDirPath remoteDirPath
     * @param localFilePath localFilePath
     * @param userName      userName
     * @param password      password
     * @param ip            ip
     * @return Ssh2Result
     */
    public static Ssh2Result upLoad(String remoteDirPath, String localFilePath, String userName, String password, String ip) {
        if (StringUtil.isNullOrEmpty(remoteDirPath) || StringUtil.isNullOrEmpty(localFilePath) || StringUtil.isNullOrEmpty(userName) || StringUtil.isNullOrEmpty(ip)) {
            return Ssh2Result.builder().success(false).msg("远程目录|本地安装包路径|服务器用户名|IP 都不能为空").build();
        }
        Connection connection = null;
        Ssh2Result ssh2Result = null;
        try {
            connection = getConnect(userName, password, ip);
            SCPClient scpClient = connection.createSCPClient();
            scpClient.put(localFilePath, remoteDirPath);
            ssh2Result = Ssh2Result.success();
        } catch (IOException ioe) {
            log.error("upLoad exception:", ioe);
            ssh2Result = Ssh2Result.builder().success(false).msg(ioe.getLocalizedMessage()).build();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return ssh2Result;
    }

    // uploadFile to Linux
    public static boolean downLoad(String remoteFilePath, String localDirPath, String user, String password, String ip) {
        boolean bool = false;
        Connection connection = null;
        try {
            connection = getConnect(user, password, ip);
            SCPClient scpClient = connection.createSCPClient();
            scpClient.get(remoteFilePath, localDirPath);
            bool = true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            bool = false;
        } finally {
            connection.close();
        }
        return bool;
    }
}
