package cn.glycol.extrabot;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.logger.HyLogger;
import cn.glycol.extrabot.registration.AutoRegister;
import cn.glycol.extrabot.registration.AutoRegister.Type;
import cn.glycol.extrabot.registration.AutoRegisterException;
import cn.glycol.tutils.tools.timer.Timer;

public class ExtraBot {

	/**
	 * 注册目标类中带有 {@code @AutoRegister} 的变量到目标机器人中。
	 * @param cls 目标类
	 * @param bot 目标机器人
	 */
	public static void register(Class<?> cls, PicqBotX bot) {
		try {
			register(cls, bot, 0);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static void register(Class<?> cls, PicqBotX bot, int unused) throws IllegalArgumentException, IllegalAccessException {
		HyLogger logger = bot.getLogger().getInstanceManager().getLoggerInstance("AutoRegister", false);
		Timer timer = new Timer();
		
		timer.start();
		logger.logf("Loading class %s", cls.getName());
		Field[] fields = cls.getDeclaredFields();
		for(Field f : fields) {
			AutoRegister ar = f.getAnnotation(AutoRegister.class);
			if(ar == null) {
				continue;
			}
			
			// 检查变量是否是静态
			if(!Modifier.isStatic(f.getModifiers())) {
				throw new AutoRegisterException("%s is not a non-static field", f.getName());
			}
			
			if(ar.value() == Type.COMMAND) {
				// 检查变量是不是指令的实例
				Object instance = f.get(null);
				if(IcqCommand.class.isAssignableFrom(instance.getClass())) {
					IcqCommand command = (IcqCommand) f.get(null);
					bot.getCommandManager().registerCommand(command);
					logger.debugf("Added command %s", f.getName());
				} else {
					throw new AutoRegisterException("%s is not a command instance", f.getName());
				}
			}
			
			if(ar.value() == Type.LISTENER) {
				// 检查变量是不是监听器的实例
				Object instance = f.get(null);
				if(IcqListener.class.isAssignableFrom(instance.getClass())) {
					IcqListener listener = (IcqListener) instance;
					bot.getEventManager().registerListener(listener);
					logger.debugf("Added listener %s", f.getName());
				} else {
					throw new AutoRegisterException("%s is not a event listener instance", f.getName());	
				}
			}
		}
		timer.stop();
		logger.debugf("Done! Time: %sms.", timer.duration());
	}
	
}
