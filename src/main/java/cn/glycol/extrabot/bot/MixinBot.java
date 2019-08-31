package cn.glycol.extrabot.bot;

import java.util.Timer;
import java.util.function.BiConsumer;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.PicqConfig;
import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.event.IcqListener;
import cn.glycol.extrabot.ExtraBot;
import cn.glycol.extrabot.bot.manager.MixinAccountManager;
import cn.glycol.extrabot.bot.manager.MixinCommandListener;
import cn.glycol.extrabot.bot.manager.MixinCommandManager;
import cn.glycol.extrabot.bot.manager.MixinEventManager;
import cn.glycol.extrabot.bot.server.MixinHttpServer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 高度自定义机器人
 * 
 * <p>强制性修改的管理器：{@linkplain MixinCommandManager 指令管理器}，{@linkplain MixinEventManager 事件管理器}，{@linkplain MixinAccountManager 账号管理器}，{@linkplain MixinHttpServer HttpServer}
 * 
 * @author Taskeren
 */
@Getter
public class MixinBot extends PicqBotX {

	protected final MixinBotConfiguration config;
	protected final BotTweaker botTweaker;
	
	/** Timer调度器 */
	private Timer timer;

	/**
	 * 使用 MixinBot 的配置档创建一个 MixinBot。
	 * 
	 * @param config MixinBot 配置
	 */
	public MixinBot(MixinBotConfiguration config) {
		this(config, BotTweaker.DEFAULT);
	}

	/**
	 * 使用 MixinBot 的配置档创建一个 MixinBot。
	 * 
	 * @param config  MixinBot 配置
	 * @param tweaker 机器人事件监听器
	 */
	public MixinBot(MixinBotConfiguration config, BotTweaker tweaker) {
		super(config);
		this.config = config;
		this.botTweaker = tweaker;
		this.timer = new Timer();
		init();
	}
	
	/**
	 * 强制将PicqBotX 转型为 MixinBot
	 * @param bot PicqBotX
	 * @return MixinBot
	 */
	public static MixinBot of(PicqBotX bot) {
		return (MixinBot) bot;
	}

	@Getter(AccessLevel.NONE)
	private boolean init;
	
	public void init() {
		if(init) {
			getLogger().error("初始化失败：不能多次初始化。");
			return;
		} else {
			init = true;
		}
		
		// 强制性修改，你莫得选择
		MixinBotInjector.setEventManager(this, new MixinEventManager(this)); // 修改事件管理器
		MixinBotInjector.setAccountManager(this, new MixinAccountManager(this)); // 修改账号管理器
		MixinBotInjector.setHttpServer(this, new MixinHttpServer(this, config.getSocketPort())); // 修改Http服务器
		
	}
	
	/**
	 * 获取账号管理器
	 */
	public MixinAccountManager getAccountManager() {
		return (MixinAccountManager) super.getAccountManager(); // 在初始化里已经强制修改为了 MixinAccountManager
	}
	
	/**
	 * 获取HTTP监听器
	 */
	public MixinHttpServer getHttpServer() {
		return (MixinHttpServer) super.getHttpServer();
	}
	
	/**
	 * 获取指令管理器
	 */
	public MixinCommandManager getCommandManager() {
		return (MixinCommandManager) super.getCommandManager();
	}
	
	@Override
	public void addAccount(String name, String postUrl, int postPort) {
		if (botTweaker.onAddAccount(this, name, postUrl, postPort)) {
			try {
				super.addAccount(name, postUrl, postPort);
			} catch(Exception ex) {
				// 如果有异常处理器则交给异常处理器处理，否则继续向上抛出异常
				if(config.getAddAccountExceptionHandler() != null) {
					config.getAddAccountExceptionHandler().accept(this, ex);
				} else {
					throw ex;
				}
			}
		} else {
			//
		}
	}

	@Override
	public void enableCommandManager(String... prefixes) {
		if (botTweaker.onEnableCommandManager(this, prefixes)) {
			MixinCommandManager manager = new MixinCommandManager(this, prefixes);
			MixinBotInjector.setCommandManager(this, manager); // 修改指令管理器
			getEventManager().registerListener(new MixinCommandListener(manager)); // 注册指令监听器
		} else {
			//
		}
	}

	/**
	 * 注册指令
	 * 
	 * @param commands 指令
	 */
	public void addCommand(IcqCommand command) {
		getCommandManager().registerCommand(command);
	}

	/**
	 * 注册事件监听器
	 * 
	 * @param listener 事件监听器
	 */
	public void addEventListenr(IcqListener listener) {
		getEventManager().registerListener(listener);
	}

	/**
	 * 调用 {@link ExtraBot#register(Class, PicqBotX)} 自动注册指令和事件监听器。
	 * 
	 * @param cls 存放指令和事件监听器的类。
	 */
	public void doAutoRegister(Class<?> cls) {
		ExtraBot.register(cls, this);
	}

	@Override
	public void startBot() {
		try {
			super.startBot();
		} catch(Exception ex) {
			if(config.getStartBotExceptionHandler() != null) {
				config.getStartBotExceptionHandler().accept(this, ex);
			} else {
				throw ex;
			}
		}
	}
	
	/**
	 * 关闭机器人
	 */
	public void stopBot() {
		getHttpServer().stop();
	}

	/**
	 * 用于存储其他的数据，原版配置在 {@link PicqConfig}。
	 * 
	 * @author Taskeren
	 */
	@Data
	@Accessors(chain = true)
	@EqualsAndHashCode(callSuper = true)
	public static class MixinBotConfiguration extends PicqConfig {

		public MixinBotConfiguration(int port) {
			super(port);
		}
		
		/** 异常处理，用于处理 {@link MixinBot#addAccount(String, String, int)} 抛出的异常，通常是用户添加失败（酷Q没开） */
		private BiConsumer<MixinBot, Exception> addAccountExceptionHandler;
		
		/** 异常处理，用于处理 {@link MixinBot#startBot()} 抛出的异常，通常是HTTPAPI版本验证问题 */
		private BiConsumer<MixinBot, Exception> startBotExceptionHandler;
		
		/** 是否使用 {@link MixinHttpServer} */
		private boolean useMixinHttpServer = true;
		
		/** 非酷Q请求时返回的欢迎语 */
		private String helloMessage = "Hey, MixinBot here!";

	}

}
