package cn.glycol.extrabot.test.commands;

import java.util.ArrayList;
import java.util.Arrays;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.user.User;
import cn.glycol.extrabot.component.Component;
import cn.glycol.extrabot.component.ComponentAt;
import cn.glycol.extrabot.component.ComponentImage;

public class CommandA implements EverywhereCommand {

	@Override
	public CommandProperties properties() {
		return new CommandProperties("a");
	}
	
	@Override
	public String run(EventMessage event, User sender, String command, ArrayList<String> args) {
		try {
			System.out.println(Arrays.toString(args.toArray()));
			ArrayList<Component> components = Component.parseComponents(args);
			System.out.println(Arrays.toString(components.toArray()));
			Component c = components.get(0);
			if(c instanceof ComponentAt) {
				return String.valueOf(((ComponentAt) c).getQQ());
			}
			if(c instanceof ComponentImage) {
				return ((ComponentImage) c).getUrl();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		String content = "";
		for(String arg : args) {
			content += arg + " ";
		}
		content = content.trim();
		
		System.out.println(content);
		return content;
	}
	
}
