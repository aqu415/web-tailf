package com.xx.log.test;

import com.xx.log.util.MixUtil;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileTest {

    @Test
    public void dataTest() throws Exception {
        //写入中文字符时解决中文乱码问题
        FileOutputStream fos = new FileOutputStream(new File("D:\\log/新建文本文档 (2).txt"));
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        PrintWriter pw = new PrintWriter(osw);

        pw.println("welcome!");

        Thread.sleep(2000);

        pw.println("I am kanan,today is 20210419");
        pw.flush();
        Thread.sleep(1000);

        pw.println("and are you ready?");
        pw.flush();
        Thread.sleep(1000);

        pw.println("欢迎使用，记得好评");
        pw.flush();
        Thread.sleep(1000);
    }

    @Test
    public void fileEncodingTest() {
//        String encoding = MixUtil.getFileOrIOEncode("E:\\workspace-gitee\\tailf\\tailf-web\\src\\main\\resources\\cpdetector_1.0.10_binary\\test.txt", "file");
//        System.out.println(encoding);
    }

    @Test
    public void readTest() throws Exception {
        FileInputStream fis = new FileInputStream("E:\\workspace-gitee\\tailf\\tailf-web\\src\\main\\resources\\cpdetector_1.0.10_binary\\test.txt");
        InputStreamReader isr = new InputStreamReader(fis, "utf-8");
        BufferedReader br = new BufferedReader(isr);

        String line = null;
        String[] arrs = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    @Test
    public void threadPoolTest() throws UnsupportedEncodingException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        String encoding = System.getProperty("file.encoding");
        System.out.println(encoding);

        byte[] unicodes = "uoooooooa".getBytes("Unicode");
        byte[] unicodes16 = "uoooooooa".getBytes(StandardCharsets.UTF_16);

        System.out.println(unicodes.length);
        System.out.println(unicodes16.length);

        printByte(unicodes);
        printByte(unicodes16);
    }

    private void printByte(byte[] bt) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bt.length; i++) {
            builder.append(bt[i]);
        }
        System.out.println(builder.toString());
    }
}
