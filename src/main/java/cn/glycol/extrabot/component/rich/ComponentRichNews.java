package cn.glycol.extrabot.component.rich;

import cn.glycol.extrabot.component.Component;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 网页组件
 * @author Taskeren
 *
 */
@AllArgsConstructor
@ToString
public class ComponentRichNews extends Component {

	/** 页面名称 */
	@Getter final String title;
	/** 页面描述 */
	@Getter final String desc;
	/** 页面预览 */
	@Getter final String preview;
	/** APP名称 */
	@Getter final String tag;
	/** 页面地址 */
	@Getter final String url;
	
}
