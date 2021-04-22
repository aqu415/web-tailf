package com.xx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Test {

    @org.junit.Test
    public void dataTest() throws Exception {
        //写入中文字符时解决中文乱码问题
        FileOutputStream fos = new FileOutputStream(new File("D:\\log/新建文本文档 (2).txt"));
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
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
}
