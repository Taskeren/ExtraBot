package cn.glycol.extrabot.test.commands;

import java.util.ArrayList;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.user.User;
import cn.glycol.extrabot.component.extra.ComponentQRCode;

public class CommandQR implements EverywhereCommand {

	@Override
	public CommandProperties properties() {
		return new CommandProperties("qr");
	}
	
	@Override
	public String run(EventMessage event, User sender, String command, ArrayList<String> args) {
		try {
			if(args.size() > 0) {
				return new ComponentQRCode(args.get(0)).toString();
			}
			else {
				return "WA";
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
}
