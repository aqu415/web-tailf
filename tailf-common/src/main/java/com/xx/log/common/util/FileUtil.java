package com.xx.log.common.util;

import java.io.*;
import java.nio.charset.Charset;

public class FileUtil {

    public static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];

        try (FileInputStream in = new FileInputStream(file)) {
            in.read(filecontent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * https://blog.csdn.net/mz4138/article/details/81396610
     *
     * @param chars
     * @return
     */
    public static byte[] getBytes(char[] chars) {
        byte[] result = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            result[i] = (byte) chars[i];
        }
        return result;
    }

    /**
     * 读取文件最后n行 <br>
     * 相当于Linux系统中的tail命令 读取大小限制是2GB
     *
     * @param filePath 文件名
     * @param rows     读取行数
     * @throws IOException
     */
    public static String readLastRows(String filePath, int rows) throws IOException {
        try (RandomAccessFile rf = new RandomAccessFile(filePath, "r")) {
            byte[] c = new byte[1];
            // 在获取到指定行数和读完文档之前,从文档末尾向前移动指针,遍历文档每一个字节
            for (long pointer = rf.length(), lineSeparatorNum = 0; pointer >= 0 && lineSeparatorNum < rows; ) {
                // 移动指针
                rf.seek(pointer--);
                // 读取数据
                int readLength = rf.read(c);
                // 换行符
                if (readLength != -1 && c[0] == 10) {
                    lineSeparatorNum++;
                }
                // 扫描完依然没有找到足够的行数,将指针归0
                if (pointer == -1 && lineSeparatorNum < rows) {
                    rf.seek(0);
                }
            }
            byte[] tempbytes = new byte[(int) (rf.length() - rf.getFilePointer())];
            rf.readFully(tempbytes);
            return new String(tempbytes, Charset.defaultCharset());
        }
    }

    /**
     * 获得最后n行（并根据关键字过滤）
     * @param filePath
     * @param rows
     * @param searchKey
     * @return
     * @throws IOException
     */
    public static String readLastRows(String filePath, int rows, String searchKey) throws IOException {
        try (RandomAccessFile rf = new RandomAccessFile(filePath, "r")) {
            // 返回值
            StringBuilder info = new StringBuilder();

            long lineEnd = rf.length();

            // 在获取到指定行数和读完文档之前,从文档末尾向前移动指针,遍历文档每一个字节
            byte[] c = new byte[1];
            for (long pointer = rf.length(), lineSeparatorNum = 0; pointer >= 0 && lineSeparatorNum < rows; ) {
                // 移动指针
                rf.seek(pointer--);
                // 读取数据
                int readLength = rf.read(c);
                // 换行符
                if (readLength != -1 && c[0] == 10) {
                    lineSeparatorNum++;
                    // 读取文本
                    long cur = rf.getFilePointer();
                    String line = getLine(rf, cur, lineEnd);
                    if (searchKey == null || searchKey.length() == 0 || line.contains(searchKey)) {
                        info.insert(0, line);
                    }
                    lineEnd = cur - 1;
                }
                // 扫描完依然没有找到足够的行数,将指针归0
                if (pointer == -1 && lineSeparatorNum < rows) {
                    rf.seek(0);
                    //读取文本
                    String line = getLine(rf, 0, lineEnd);
                    if (searchKey == null || searchKey.length() == 0 || line.contains(searchKey)) {
                        info.insert(0, line);
                    }
                }
            }
            return info.toString();
        }
    }

    private static String getLine(RandomAccessFile rf, long start, long end) throws IOException {
        byte[] tempBytes = new byte[(int) (end - start)];
        rf.readFully(tempBytes);
        return new String(tempBytes, "utf-8");
    }
}
