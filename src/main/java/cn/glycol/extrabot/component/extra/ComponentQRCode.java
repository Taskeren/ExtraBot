package cn.glycol.extrabot.component.extra;

import java.io.File;

import cn.glycol.extrabot.util.QrCode;
import cn.hutool.extra.qrcode.QrConfig;

/**
 * 二维码组件
 * @author Taskeren
 */
public class ComponentQRCode extends ComponentImage {
	
	public ComponentQRCode(String ctx) {
		this(QrCode.gen(ctx));
	}
	
	public ComponentQRCode(String ctx, QrConfig config) {
		this(QrCode.gen(ctx, config));
	}
	
	public ComponentQRCode(File qrfile) {
		super(qrfile);
	}
	
}
