package cn.glycol.extrabot.test;

import java.util.Arrays;

import org.junit.Test;

import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.logger.environments.ColorSupportLevel;
import cn.glycol.extrabot.bot.BotTweaker;
import cn.glycol.extrabot.bot.MixinBot;
import cn.glycol.extrabot.bot.MixinBot.MixinBotConfiguration;

public class TestMixin {

	@Test
	public void test() {
		
		MixinBot bot;
		bot = new MixinBot((MixinBotConfiguration) new MixinBotConfiguration(25560).setColorSupportLevel(ColorSupportLevel.DISABLED), tweaker);
		bot.addAccount("Geo", "127.0.0.1", 25561);
		bot.enableCommandManager("/");
		
		bot.getCommandManager().setPrefixNeeded(false);
		
		bot.doAutoRegister(Reference.class);
		
		bot.startBot();
		
		while(true) {}
	}
	
	public static final BotTweaker tweaker = new BotTweaker() {
		
		@Override
		public boolean onAddAccount(MixinBot bot, String name, String postUrl, int postPort) {
			bot.getLogger().logf("[添加账号] 名称：%s 地址：%s 端口：%s", name, postUrl, postPort);
			return BotTweaker.super.onAddAccount(bot, name, postUrl, postPort);
		}
		
		@Override
		public boolean onEnableCommandManager(MixinBot bot, String...prefixes) {
			bot.getLogger().logf("[注册指令管理器] 前缀：%s", Arrays.toString(prefixes));
			return BotTweaker.super.onEnableCommandManager(bot, prefixes);
		};
		
		@Override
		public boolean onAddCommand(MixinBot bot, IcqCommand command) {
			bot.getLogger().logf("[注册指令] 指令：%s(%s)", command.properties().getName(), command.getClass().getCanonicalName());
			return BotTweaker.super.onAddCommand(bot, command);
		}
		
		public boolean onAddEventListener(MixinBot bot, cc.moecraft.icq.event.IcqListener listener) {
			bot.getLogger().logf("[注册监听器] 监听器：%s", listener.getClass().getCanonicalName());
			return BotTweaker.super.onAddEventListener(bot, listener);
		};
		
		@Override
		public void onBotStarted(MixinBot bot) {
			bot.getLogger().log("[机器人启动]");
		}
		
		public void onBotStopped(MixinBot bot) {
			bot.getLogger().log("[机器人关闭]");
		};
		
	};
	
}
