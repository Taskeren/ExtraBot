package cn.glycol.extrabot.timer;

import cc.moecraft.icq.sender.IcqHttpApi;
import cn.glycol.extrabot.bot.MixinBot;
import cn.glycol.extrabot.bot.manager.MixinAccountManager;
import cn.glycol.extrabot.bot.manager.MixinAccountManager.BotAccountFinder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 消息发送任务模板
 * @author Taskeren
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class MixinTaskMessage extends MixinTask {

	public MixinTaskMessage(MixinBot bot) {
		super(bot);
	}
	
	public MixinTaskMessage(MixinBot bot, String message, long to, MessageType type, BotAccountFinder finder) {
		super(bot);
		this.message = message;
		this.messageTo = to;
		this.messageType = type;
		this.accountFinder = finder;
	}
	
	/* 消息三要素 */
	protected String message;
	protected MessageType messageType;
	protected long messageTo;
	
	/* HTTPAPI获取 */
	protected MixinAccountManager.BotAccountFinder accountFinder;
	
	@Override
	public void run() {
		IcqHttpApi api = accountFinder.find(bot).getHttpApi();
		
		if(MessageType.PRIVATE == messageType) {
			api.sendPrivateMsg(messageTo, message);
		}
		else if(MessageType.GROUP == messageType) {
			api.sendGroupMsg(messageTo, message);
		}
		else if(MessageType.DISCUSS == messageType) {
			api.sendDiscussMsg(messageTo, message);
		}
		else {
			
		}
	}
	
	public static enum MessageType {
		PRIVATE, GROUP, DISCUSS;
	}
	
}
