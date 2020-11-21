package com.xx.log;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class DefaultTest {

	@Test
	public void decodeTest() throws UnsupportedEncodingException {
		String info = "你好";
		System.out.println(new String(info.getBytes("gbk"),"gbk"));
	}
}