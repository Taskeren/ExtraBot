package cn.glycol.extrabot.test;

import java.io.File;

import org.junit.Test;

import cn.glycol.extrabot.util.QrCode;

public class TestQRCode {

	@Test
	public void test() {
		File file = QrCode.generate("https://donate.hcooh.top");
		System.out.println(file);
	}
	
}
