package cn.glycol.extrabot.util;

import java.io.File;

import cn.glycol.tutils.constant.ColorCode;
import cn.hutool.core.io.FileUtil;
import lombok.Data;

@Data
public class MixinGlobalConfig {

	public static final MixinGlobalConfig instance = new MixinGlobalConfig();
	
	private MixinGlobalConfig() {}
	
	/** 缓存和临时文件夹 */
	public static final File CACHE_FOLDER = FileUtil.file("cache");
	
	//////////////////////// QRCode ////////////////////////
	/** 二维码图片前缀名 */
	public static final String QR_PREFIX = "QRCODE";
	
	/** 二维码图片后缀名 */
	public static final String QR_SUFFIX = ".png";
	
	/** 二维码图片默认前景颜色 */
	public static final int QR_FORECOLOR = ColorCode.PURPLE;
	
	static {
		CACHE_FOLDER.mkdirs();
	}
	
}
