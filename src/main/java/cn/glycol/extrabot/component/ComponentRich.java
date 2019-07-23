package cn.glycol.extrabot.component;

import cn.glycol.extrabot.component.rich.ComponentRichMusic;
import cn.glycol.extrabot.component.rich.ComponentRichNews;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 这是一个很神奇的组件，它包含了其他APP的分享，讨论组的
 * 分享，群投票什么玩意的。有些时候给的是text，有些时候给
 * 的是content，您们自己看看选着用。
 * @author Taskeren
 * @see ComponentRichMusic
 * @see ComponentRichNews
 */
@AllArgsConstructor
@ToString
public class ComponentRich extends Component {

	@Getter final String title;
	@Getter final String text;
	@Getter final String content;
	
}
