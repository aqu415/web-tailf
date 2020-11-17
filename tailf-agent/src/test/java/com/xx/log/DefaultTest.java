package com.xx.log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class DefaultTest {

	@Test
	public void readLastRowsTest() throws IOException {
		String res = com.xx.log.util.ContentUtil.readLastRows("D:/u1 - 副本.sql",  5);
		System.out.println(res);
	}
	
	@Test
	public void decodeTest() throws UnsupportedEncodingException {
		String info = "你好";
		System.out.println(new String(info.getBytes("gbk"),"gbk"));
	}
}