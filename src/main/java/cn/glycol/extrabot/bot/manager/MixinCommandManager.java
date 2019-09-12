package cn.glycol.extrabot.bot.manager;

import static cn.glycol.extrabot.bot.manager.MixinArgsParser.*;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.command.CommandArgs;
import cc.moecraft.icq.command.CommandManager;
import cc.moecraft.icq.command.exceptions.CommandNotFoundException;
import cc.moecraft.icq.command.exceptions.NotACommandException;
import cc.moecraft.icq.command.interfaces.DiscussCommand;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.command.interfaces.GroupCommand;
import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.command.interfaces.PrivateCommand;
import cc.moecraft.icq.event.events.message.EventDiscussMessage;
import cc.moecraft.icq.event.events.message.EventGroupMessage;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.event.events.message.EventPrivateMessage;
import cc.moecraft.icq.user.Group;
import cc.moecraft.icq.user.User;
import cn.glycol.extrabot.bot.MixinBot;
import lombok.Getter;

public class MixinCommandManager extends CommandManager {

	protected final MixinBot bot;

	/** 是否取消指令前缀 */
	@Getter
	protected boolean prefixNeeded = true;
	
	/** 无需指令前缀的指令名称 */
	@Getter
	protected List<String> prefixUnneeded = Lists.newArrayList();

	public MixinCommandManager(MixinBot bot, String... prefixes) {
		super(bot, prefixes);
		this.bot = bot;
	}
	
	/**
	 * 设置是否需要前缀
	 */
	public void setPrefixNeeded(boolean needed) {
		prefixNeeded = needed;
	}
	
	/**
	 * 添加一个无需指令前缀的指令
	 * @param command
	 */
	public void addPrefixUnneeded(String command) {
		prefixUnneeded.add(command);
	}
	
	/**
	 * 获取特定指令是否需要指令前缀
	 * @param command 指令
	 * @return 是否需要前缀
	 */
	public boolean isPrefixUnneeded(String command) {
		return prefixUnneeded.contains(command);
	}

	@Override
	public void registerCommand(IcqCommand command) {
		if(bot.getBotTweaker().onAddCommand(bot, command)) {
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
			if(!result.contains(name))
				result.add(name);
		});
		return result;
	}

	/**
	 * 自动找到注册过的指令对象运行
	 * <p>
	 * 例子: !ecHO hi there
	 *
	 * @param event 事件
	 */
	@Override
	public void runCommand(EventMessage event) {
		PicqBotX bot = event.getBot();

		final boolean isGM = event instanceof EventGroupMessage;
		final boolean isDM = event instanceof EventDiscussMessage;
		final boolean isPM = event instanceof EventPrivateMessage;

		// 获取Args
		CommandArgs args;

		try {
			args = parse0(this, event.getMessage(), isDM || isGM);
		} catch(NotACommandException | CommandNotFoundException e) {
			return;
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}

		// 判断维护
		if(bot.getConfig().isMaintenanceMode()) {
			event.respond(bot.getConfig().getMaintenanceResponse());
			return;
		}

		// 获取发送者
		User user = bot.getUserManager().getUserFromID(event.getSenderId());

		// 获取群
		Group group = isGM ? bot.getGroupManager().getGroupFromID(((EventGroupMessage) event).getGroupId())
				: isDM ? bot.getGroupManager().getGroupFromID(((EventDiscussMessage) event).getDiscussId()) : null;

		// 调用指令执行方法
		IcqCommand runner = args.getCommandRunner();

		if(runner instanceof EverywhereCommand) {
			event.respond(((EverywhereCommand) runner).run(event, user, args.getCommandName(), args.getArgs()));
		} else if(isGM && runner instanceof GroupCommand) {
			event.respond(((GroupCommand) runner).groupMessage((EventGroupMessage) event, bot.getGroupUserManager().getUserFromID(user.getId(), group), group,
					args.getCommandName(), args.getArgs()));
		} else if(isDM && runner instanceof DiscussCommand) {
			event.respond(((DiscussCommand) runner).discussMessage((EventDiscussMessage) event, bot.getGroupUserManager().getUserFromID(user.getId(), group), group,
					args.getCommandName(), args.getArgs()));
		} else if(isPM && runner instanceof PrivateCommand) {
			event.respond(((PrivateCommand) runner).privateMessage((EventPrivateMessage) event, user, args.getCommandName(), args.getArgs()));
		}
	}

}
