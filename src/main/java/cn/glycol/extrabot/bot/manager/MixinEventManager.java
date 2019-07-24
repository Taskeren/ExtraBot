package cn.glycol.extrabot.bot.manager;

import cc.moecraft.icq.event.EventManager;
import cc.moecraft.icq.event.IcqListener;
import cn.glycol.extrabot.bot.MixinBot;

public class MixinEventManager extends EventManager {

	protected final MixinBot bot;
	
	public MixinEventManager(MixinBot bot) {
		super(bot);
		this.bot = bot;
	}
	
	@Override
	public void registerListener(IcqListener listener) {
		if(bot.getBotTweaker().onAddEventListener(bot, listener)) {
			super.registerListener(listener);
		} else {
			//
		}
	}
	
}
