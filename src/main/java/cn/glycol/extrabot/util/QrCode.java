package cn.glycol.extrabot.util;

import java.io.File;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;

public class QrCode {

	/**
	 * 生成一个二维码图片
	 * @param ctx 二维码内容
	 * @return 二维码文件
	 */
	public static File generate(String ctx) {
		return generate(
			ctx,
			QrConfig
				.create()
				.setErrorCorrection(ErrorCorrectionLevel.M)
				.setForeColor(MixinGlobalConfig.QR_FORECOLOR)
		);
	}
	
	/**
	 * 生成一个二维码图片
	 * @param ctx 二维码内容
	 * @param config 二维码设置
	 * @return 二维码文件
	 */
	public static File generate(String ctx, QrConfig config) {
		return QrCodeUtil.generate(ctx, config, FileUtil.createTempFile(MixinGlobalConfig.QR_PREFIX, MixinGlobalConfig.QR_SUFFIX, MixinGlobalConfig.CACHE_FOLDER, true));
	}
	
}
