package cn.glycol.extrabot.bot;

import java.lang.reflect.Field;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.accounts.AccountManager;
import cc.moecraft.icq.command.CommandManager;
import cc.moecraft.icq.event.EventManager;
import cc.moecraft.icq.receiver.PicqHttpServer;
import cc.moecraft.icq.user.GroupManager;
import cc.moecraft.icq.user.GroupUserManager;
import cc.moecraft.icq.user.UserManager;
import cc.moecraft.logger.LoggerInstanceManager;

/**
 * PicqBotX数据注入器
 * @author Taskeren
 * @version 4.10.1.928
 */
public class MixinBotInjector {

	/*                         变量名称                           PicqBotX中的变量名称 */
	private static final String HTTP_SERVER        = "httpServer";
	private static final String EVENT_MANAGER      = "eventManager";
	private static final String ACCOUNT_MANAGER    = "accountManager";
	private static final String USER_MANAGER       = "userManager";
	private static final String GROUP_MANAGER      = "groupManager";
	private static final String GROUP_USER_MANAGER = "groupUserManager";
	private static final String COMMAND_MANAGER    = "commandManager";
	private static final String LOGGER_MANAGER     = "loggerInstanceManager";
	
	private static void replace(PicqBotX bot, String name, Object value) {
		try {
			Field field = PicqBotX.class.getDeclaredField(name);
			field.setAccessible(true);
			field.set(bot, value);
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static void setHttpServer(PicqBotX bot, PicqHttpServer server) {
		replace(bot, HTTP_SERVER, server);
	}
	
	public static void setEventManager(PicqBotX bot, EventManager manager) {
		replace(bot, EVENT_MANAGER, manager);
	}
	
	public static void setAccountManager(PicqBotX bot, AccountManager manager) {
		replace(bot, ACCOUNT_MANAGER, manager);
	}
	
	public static void setUserManager(PicqBotX bot, UserManager manager) {
		replace(bot, USER_MANAGER, manager);
	}
	
	public static void setGroupManager(PicqBotX bot, GroupManager manager) {
		replace(bot, GROUP_MANAGER, manager);
	}
	
	public static void setGroupUserManager(PicqBotX bot, GroupUserManager manager) {
		replace(bot, GROUP_USER_MANAGER, manager);
	}
	
	public static void setCommandManager(PicqBotX bot, CommandManager manager) {
		replace(bot, COMMAND_MANAGER, manager);
	}
	
	public static void setLoggerInstanceManager(PicqBotX bot, LoggerInstanceManager manager) {
		replace(bot, LOGGER_MANAGER, manager);
	}
	
}
