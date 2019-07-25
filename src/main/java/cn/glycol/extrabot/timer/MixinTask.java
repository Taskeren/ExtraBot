package cn.glycol.extrabot.timer;

import java.util.TimerTask;

import cn.glycol.extrabot.bot.MixinBot;

public abstract class MixinTask extends TimerTask {

	protected final MixinBot bot;

	public MixinTask(MixinBot bot) {
		this.bot = bot;
	}
	
	public abstract void run();
	
}
