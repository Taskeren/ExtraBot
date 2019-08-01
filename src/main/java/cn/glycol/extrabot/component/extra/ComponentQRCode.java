package cn.glycol.extrabot.component.extra;

import java.io.File;

import cc.moecraft.icq.sender.message.MessageComponent;
import cc.moecraft.icq.sender.message.components.ComponentImageBase64;
import cn.glycol.extrabot.util.QrCode;
import cn.hutool.core.codec.Base64;
import cn.hutool.extra.qrcode.QrConfig;

/**
 * 二维码组件
 * @author Taskeren
 */
public class ComponentQRCode extends MessageComponent {

	protected final File qrfile;
	
	public ComponentQRCode(String ctx) {
		this(QrCode.gen(ctx));
	}
	
	public ComponentQRCode(String ctx, QrConfig config) {
		this(QrCode.gen(ctx, config));
	}
	
	public ComponentQRCode(File qrfile) {
		this.qrfile = qrfile;
	}
	
	@Override
	public String toString() {
		return new ComponentImageBase64(Base64.encode(qrfile)).toString();
	}
	
}
