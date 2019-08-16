package cn.glycol.extrabot.registration;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.command.interfaces.DiscussCommand;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.command.interfaces.GroupCommand;
import cc.moecraft.icq.command.interfaces.PrivateCommand;
import cc.moecraft.icq.sender.returndata.returnpojo.get.RGroupMemberInfo;
import cc.moecraft.icq.sender.returndata.returnpojo.get.RStrangerInfo;
import cc.moecraft.icq.user.Group;
import cc.moecraft.icq.user.GroupUser;
import cc.moecraft.icq.user.User;
import lombok.Getter;

/**
 * 封装后的用户。
 * 
 * <p>
 * 使用 {@link #asStranger()} 获取 {@link EverywhereCommand} 和
 * {@link PrivateCommand} 的用户。
 * 
 * <p>
 * 使用 {@link #asGrouper()} 获取 {@link GroupCommand} 和 {@link DiscussCommand} 的用户。
 * 
 * @author Taskeren
 *
 */
@Getter
public class MixinUser {

	protected final PicqBotX bot;
	protected final long uid;

	public MixinUser(User user) {
		bot = user.getBot();
		uid = user.getId();
	}

	public MixinUser(GroupUser user) {
		bot = user.getBot();
		uid = user.getId();
	}

	public static MixinUser of(User user) {
		return new MixinUser(user);
	}

	public static MixinUser of(GroupUser user) {
		return new MixinUser(user);
	}

	public MixinUserStranger asStranger() {
		return this instanceof MixinUserStranger ? (MixinUserStranger) this : null;
	}

	public MixinUserGroup asGrouper() {
		return this instanceof MixinUserGroup ? (MixinUserGroup) this : null;
	}

	public static class MixinUserStranger extends MixinUser {

		protected final User user;

		public MixinUserStranger(User user) {
			super(user);
			this.user = user;
		}

		public RStrangerInfo getInfo() {
			return user.getInfo();
		}

	}

	public static class MixinUserGroup extends MixinUser {

		protected final GroupUser user;

		public MixinUserGroup(GroupUser user) {
			super(user);
			this.user = user;
		}

		public RGroupMemberInfo getInfo() {
			return user.getInfo();
		}

		public boolean isAdmin() {
			return user.isAdmin();
		}

		public Group getGroup() {
			return user.getGroup();
		}

	}

}
