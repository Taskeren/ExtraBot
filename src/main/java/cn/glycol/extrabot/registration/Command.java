package cn.glycol.extrabot.registration;

import static cn.glycol.extrabot.registration.CommandType.DISCUSS;
import static cn.glycol.extrabot.registration.CommandType.EVERYWHERE;
import static cn.glycol.extrabot.registration.CommandType.GROUP;
import static cn.glycol.extrabot.registration.CommandType.PRIVATE;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import cc.moecraft.icq.command.CommandProperties;
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
import cc.moecraft.icq.user.GroupUser;
import cc.moecraft.icq.user.User;

public abstract class Command {

	protected final CommandType type;
	protected final String name;
	protected final ArrayList<String> alias;

	/**
	 * 构建一个 Command 实例。
	 * <p>
	 * 使用此方法构造时，请使用 {@link Command#toIcqCommand(CommandType)} 转换。
	 * 
	 * @param name  指令名称
	 * @param alias 指令别名
	 */
	public Command(String name, String... alias) {
		this(null, name, alias);
	}

	/**
	 * 构建一个 Command 实例。
	 * 
	 * @param type  指令类型
	 * @param name  指令名称
	 * @param alias 指令别名
	 */
	public Command(CommandType type, String name, String... alias) {
		this.type = type;
		this.name = name;
		this.alias = Lists.newArrayList(alias);
	}

	/**
	 * 指令执行方法
	 * 
	 * @param event   消息事件
	 * @param sender  发信人
	 * @param group   发信群（如果类型为{@link EverywhereCommand}和{@link PrivateCommand}则为{@code null}）
	 * @param command 指令
	 * @param args    参数
	 * @return 返回的消息，{@code null}则不返回消息
	 */
	public abstract String run(EventMessage event, MixinUser sender, Group group, String command, ArrayList<String> args);

	/**
	 * 转换为 {@link IcqCommand}
	 */
	public IcqCommand toIcqCommand() {
		return toIcqCommand(type);
	}

	/**
	 * 转换为 {@link IcqCommand}
	 * 
	 * @param type 转换类型
	 */
	public IcqCommand toIcqCommand(CommandType type) {

		if(type == null) {
			throw new NullPointerException("type");
		}

		else if(type == EVERYWHERE) {
			return new EverywhereCommand() {
				@Override
				public CommandProperties properties() {
					return new CommandProperties(name, alias);
				}

				@Override
				public String run(EventMessage event, User sender, String command, ArrayList<String> args) {
					return Command.this.run(event, MixinUser.of(sender), null, command, args);
				}
			};
		}

		else if(type == GROUP) {
			return new GroupCommand() {
				@Override
				public CommandProperties properties() {
					return new CommandProperties(name, alias);
				}

				@Override
				public String groupMessage(EventGroupMessage event, GroupUser sender, Group group, String command, ArrayList<String> args) {
					return Command.this.run(event, MixinUser.of(sender), group, command, args);
				}
			};
		}

		else if(type == PRIVATE) {
			return new PrivateCommand() {
				@Override
				public CommandProperties properties() {
					return new CommandProperties(name, alias);
				}

				@Override
				public String privateMessage(EventPrivateMessage event, User sender, String command, ArrayList<String> args) {
					return Command.this.run(event, MixinUser.of(sender), null, command, args);
				}
			};
		}

		else if(type == DISCUSS) {
			return new DiscussCommand() {
				@Override
				public CommandProperties properties() {
					return new CommandProperties(name, alias);
				}

				@Override
				public String discussMessage(EventDiscussMessage event, GroupUser sender, Group discuss, String command, ArrayList<String> args) {
					return Command.this.run(event, MixinUser.of(sender), discuss, command, args);
				}
			};
		}

		else {
			throw new UnsupportedOperationException("Unknown type of command: " + type);
		}

	}

}
