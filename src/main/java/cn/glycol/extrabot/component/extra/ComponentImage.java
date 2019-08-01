package cn.glycol.extrabot.component.extra;

import java.io.File;

import cc.moecraft.icq.sender.message.MessageComponent;
import cc.moecraft.icq.sender.message.components.ComponentImageBase64;
import cn.hutool.core.codec.Base64;

/**
 * 图片消息组件
 * @author Taskeren
 */
public class ComponentImage extends MessageComponent {

	protected final File imgfile;
	
	public ComponentImage(File imgfile) {
		this.imgfile = imgfile;
	}
	
	public ComponentImageBase64 toComponentImageBase64() {
		return new ComponentImageBase64(Base64.encode(imgfile));
	}
	
	@Override
	public String toString() {
		return toComponentImageBase64().toString();
	}
	
}
