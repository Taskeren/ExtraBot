package cn.glycol.extrabot.test;

import org.junit.Test;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.PicqConfig;
import cc.moecraft.logger.environments.ColorSupportLevel;
import cn.glycol.extrabot.ExtraBot;

public class TestExtraBot {
	
	@Test
	public void testAutoRegister() throws Exception {
		
		PicqBotX bot;
		bot = new PicqBotX(new PicqConfig(25561).setColorSupportLevel(ColorSupportLevel.DISABLED));
		bot.enableCommandManager("/");
		bot.addAccount("null", "127.0.0.1", 25560);
		
		ExtraBot.register(Reference.class, bot);
		
		bot.startBot();
		
		while(true) {}
		
	}

}
