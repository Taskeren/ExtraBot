package cn.glycol.extrabot.bot.manager;

import cc.moecraft.icq.command.CommandListener;

public class MixinCommandListener extends CommandListener {

	public MixinCommandListener(MixinCommandManager manager) {
		super(manager);
	}
	
}
