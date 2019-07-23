package cn.glycol.extrabot.component.rich;

import cn.glycol.extrabot.component.Component;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 音乐组件
 * @author Taskeren
 *
 */
@AllArgsConstructor
@ToString
public class ComponentRichMusic extends Component {

	/** 歌曲名称 */
	@Getter final String title;
	/** 歌曲描述（QQ音乐和网易云音乐显示的是作者） */
	@Getter final String desc;
	/** 歌曲封面 */
	@Getter final String preview;
	/** APP标识（“QQ音乐”，“网易云音乐”） */
	@Getter final String tag;
	/** 音乐文件地址 */
	@Getter final String urlMusic;
	/** 音乐网页 */
	@Getter final String urlPage;
	
}
