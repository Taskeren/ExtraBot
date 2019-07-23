package cn.glycol.extrabot.test.listeners;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.utils.CQUtils;

public class MessageListener extends IcqListener {

	@EventHandler
	public void on(EventMessage evt) {
		System.out.println("[EventMessage] "+CQUtils.decodeMessage(evt.getRawMessage()));
	}
	
}
