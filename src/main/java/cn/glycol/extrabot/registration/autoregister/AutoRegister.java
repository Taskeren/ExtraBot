package cn.glycol.extrabot.registration.autoregister;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.logger.HyLogger;
import cn.glycol.tutils.tools.timer.Timer;
import cn.hutool.core.exceptions.ExceptionUtil;

public class AutoRegister {

	final PicqBotX bot;
	final HyLogger logger;
	
	public AutoRegister(PicqBotX bot) {
		this.bot = bot;
		this.logger = bot.getLogger();
	}
	
	public void execute(Class<?> cls) {
		try {
			execute(cls, 0);
		} catch(Exception ex) {
			ExceptionUtil.wrapAndThrow(ex);
		}
	}
	
	public void execute(Class<?> cls, int unused) throws IllegalArgumentException, IllegalAccessException {
		HyLogger logger = bot.getLogger().getInstanceManager().getLoggerInstance("AutoRegister", false);
		Timer timer = new Timer();
		
		timer.start();
		logger.debugf("Loading class %s", cls.getName());
		Field[] fields = cls.getDeclaredFields();
		for(Field f : fields) {
			AutoReg ar = f.getAnnotation(AutoReg.class);
			if(ar == null) {
				continue;
			}
			
			// 检查变量是否是静态
			if(!Modifier.isStatic(f.getModifiers())) {
				throw new AutoRegisterException("%s is not a non-static field", f.getName());
			}
			
			final Object instance = f.get(null);
			if(IcqCommand.class.isAssignableFrom(instance.getClass())) {
				register((IcqCommand) instance);
			}
			
			else if(IcqListener.class.isAssignableFrom(instance.getClass())) {
				register((IcqListener) instance);
			}
				
			else {
				throw new AutoRegisterException("%s is not a command/listener instance", f.getName());
			}
		}
		timer.stop();
		logger.debugf("Done! Time: %sms.", timer.duration());
	}
	
	public void register(IcqCommand cmd) {
		bot.getCommandManager().registerCommand(cmd);
		logger.debugf("Added command %s", cmd.properties().getName());
	}
	
	public void register(IcqListener lsn) {
		bot.getEventManager().registerListener(lsn);
		logger.debugf("Added listener %s", lsn.getClass().getCanonicalName());
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface AutoReg {}
	
}
