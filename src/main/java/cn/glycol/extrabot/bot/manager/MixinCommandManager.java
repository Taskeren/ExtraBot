package cn.glycol.extrabot.bot.manager;

import java.util.ArrayList;

import cc.moecraft.icq.command.CommandManager;
import cc.moecraft.icq.command.interfaces.IcqCommand;
import cn.glycol.extrabot.bot.MixinBot;

public class MixinCommandManager extends CommandManager {

	protected final MixinBot bot;

	public MixinCommandManager(MixinBot bot, String... prefixes) {
		super(bot, prefixes);
		this.bot = bot;
	}

	@Override
	public void registerCommand(IcqCommand command) {
		if (bot.getBotTweaker().onAddCommand(bot, command)) {
			super.registerCommand(command);
		} else {
			//
		}
	}

	/**
	 * 获取指令名列表，修复原版指令名重复的错误。
	 * 
	 * @return 指令名列表
	 */
	@Override
	public ArrayList<String> getCommandNameList() {
		ArrayList<String> result = new ArrayList<>();
		getCommandList().forEach(command -> {
			String name = command.properties().getName();
			if (!result.contains(name))
				result.add(name);
		});
		return result;
	}

}
