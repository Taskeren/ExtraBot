package cn.glycol.extrabot.test;

import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.event.IcqListener;
import cn.glycol.extrabot.registration.autoregister.AutoRegister.AutoReg;
import cn.glycol.extrabot.test.commands.CommandA;
import cn.glycol.extrabot.test.commands.CommandNative;
import cn.glycol.extrabot.test.commands.CommandQR;
import cn.glycol.extrabot.test.listeners.MessageListener;

public class Reference {
	
	@AutoReg
	public static final IcqCommand A = new CommandA();
	
	@AutoReg
	public static final EverywhereCommand QR = new CommandQR();
	
	@AutoReg
	public static final IcqListener B = new MessageListener();
	
	@AutoReg
	public static final IcqCommand NA = new CommandNative().toIcqCommand();
	
}
