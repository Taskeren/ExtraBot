package cn.glycol.extrabot.test;

import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.event.IcqListener;
import cn.glycol.extrabot.registration.AutoRegister;
import cn.glycol.extrabot.registration.AutoRegister.Type;
import cn.glycol.extrabot.test.commands.CommandA;
import cn.glycol.extrabot.test.listeners.MessageListener;

public class Reference {
	
	@AutoRegister(Type.COMMAND)
	public static final IcqCommand A = new CommandA();
	
	@AutoRegister(Type.LISTENER)
	public static final IcqListener B = new MessageListener();
	
}
