package cn.glycol.extrabot.test.commands;

import java.util.ArrayList;

import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.user.Group;
import cn.glycol.extrabot.bot.MixinBot;
import cn.glycol.extrabot.registration.autoregister.MixinUser;
import cn.glycol.extrabot.registration.command.Command;
import cn.glycol.extrabot.registration.command.CommandType;

public class CommandNative extends Command {

	public CommandNative() {
		super(CommandType.EVERYWHERE, "native");
	}
	
	@Override
	public String run(EventMessage event, MixinUser sender, Group group, String command, ArrayList<String> args) {
		MixinBot.of(sender.getBot()).stopBot();
		return command;
	}
	
}
