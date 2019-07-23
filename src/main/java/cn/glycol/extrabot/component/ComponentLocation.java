package cn.glycol.extrabot.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 位置信息
 * 
 * @author Taskeren
 *
 */
@AllArgsConstructor
@ToString
public class ComponentLocation extends Component {

	/** 经度 */
	@Getter	final String lat;
	/** 纬度 */
	@Getter	final String lon;
	/** 粗略地名 */
	@Getter	final String title;
	/** 详细地址 */
	@Getter	final String content;
	/** 样式代码 */
	@Getter	final int style;

}
