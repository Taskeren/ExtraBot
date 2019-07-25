package cn.glycol.extrabot.test;

import java.util.Arrays;
import java.util.TimerTask;

import org.junit.Test;

import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.logger.environments.ColorSupportLevel;
import cn.glycol.extrabot.bot.BotTweaker;
import cn.glycol.extrabot.bot.MixinBot;
import cn.glycol.extrabot.bot.MixinBot.MixinBotConfiguration;
import cn.glycol.extrabot.bot.manager.MixinAccountManager.BotAccountFinder;
import cn.glycol.extrabot.timer.MixinTaskMessage;
import cn.glycol.extrabot.timer.MixinTaskMessage.MessageType;

public class TestMixin {

	@Test
	public void test() {
		
		MixinBot bot;
		bot = new MixinBot((MixinBotConfiguration) new MixinBotConfiguration(25561).setColorSupportLevel(ColorSupportLevel.DISABLED), tweaker);
		bot.addAccount("Geo", "127.0.0.1", 25560);
		bot.enableCommandManager("/");
		
		bot.doAutoRegister(Reference.class);

		TimerTask task0 = new TimerTask() {
			@Override
			public void run() {
				System.out.println("task0");
			}
		};
		
		MixinTaskMessage task1 = new MixinTaskMessage(bot, "Hello", 3070190799L, MessageType.PRIVATE, new BotAccountFinder().by("Geo"));
		
		bot.getTimer().schedule(task0, 0, 1000*10);
		bot.getTimer().schedule(task1, 0, 1000*10);
		
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
		public boolean onBotStart(MixinBot bot) {
			bot.getLogger().log("[机器人启动]");
			return BotTweaker.super.onBotStart(bot);
		}
		
		public boolean onBotStop(MixinBot bot) {
			bot.getLogger().log("[机器人关闭]");
			return BotTweaker.super.onBotStop(bot);
		};
		
	};
	
}
