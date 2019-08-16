package cn.glycol.extrabot.bot.manager;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.accounts.AccountManager;
import cc.moecraft.icq.accounts.BotAccount;
import cn.glycol.extrabot.bot.MixinBot;
import cn.glycol.tutils.natives.TObject;
import lombok.Getter;

@Getter
public class MixinAccountManager extends AccountManager {

	protected final MixinBot bot;
	
	public MixinAccountManager(MixinBot bot) {
		this.bot = bot;
	}
	
	private Map<String, BotAccount> nameIndex;
	
	/**
	 * 通过QQ号获取机器人账号
	 * @param uid 请求的QQ号
	 * @return 机器人账号
	 */
	public BotAccount getAccountByUID(long uid) {
		return getAccountByUID(this, uid);
	}
	
	/**
	 * 通过注册时的名称获取机器人账号
	 * @param name 请求的名称
	 * @return 机器人账号
	 */
	public BotAccount getAccountByName(String name) {
		return getNameIndex().get(name);
	}
	
	@Override
	public void refreshCache() {
		super.refreshCache();

		nameIndex = new HashMap<>();
		
		for(BotAccount account : getAccounts()) {
			String name = account.getName();
			if(name == null) {
				bot.getLogger().warningf("[NameIndex] Ignored %s since name is null.", account);
			} else {
				nameIndex.put(name, account);
			}
		}
	}
	
	/**
	 * 通过QQ号获取机器人账号
	 * @param manager 账号管理器
	 * @param uid 请求的QQ号
	 * @return 机器人账号
	 */
	public static BotAccount getAccountByUID(AccountManager manager, long uid) {
		return manager.getIdIndex().get(uid);
	}
	
	/**
	 * 机器人账号检索获取器
	 * @author Taskeren
	 */
	public static class BotAccountFinder {
		
		private long uid;
		private String name;
		
		public BotAccountFinder by(@Nonnull Long uid) {
			this.uid = uid;
			return this;
		}
		
		public BotAccountFinder by(@Nonnull String name) {
			this.name = name;
			return this;
		}
		
		/**
		 * 尝试通过已有条件获取BotAccount。
		 * 
		 * <p>当机器人为PicqBotX时，只能通过QQ号检索。当机器人为MixinBot时，可以同时检索名称和QQ号，QQ号优先。
		 * 
		 * @param bot 机器人
		 * @return 机器人账号
		 */
		public BotAccount find(PicqBotX bot) {
			if(bot instanceof MixinBot) {
				MixinAccountManager mixin = MixinBot.of(bot).getAccountManager();
				return TObject.findNonNull(new BotAccount[] {
						mixin.getAccountByUID(uid),
						mixin.getAccountByName(name)
				});
			} else {
				return getAccountByUID(bot.getAccountManager(), uid);
			}
		}
	}
	
}
