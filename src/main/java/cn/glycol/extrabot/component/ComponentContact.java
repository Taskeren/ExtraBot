package cn.glycol.extrabot.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 联系人名片。讨论组因为结构特殊，它的名片是一个Rich组件。
 * 
 * @author Taskeren
 * 
 */
@AllArgsConstructor
@ToString
public class ComponentContact extends Component {

	@Getter final long id;
	@Getter final Type type;
	
	public static enum Type {
		QQ, GROUP;
		
		public static Type of(String s) {
			switch(s) {
			case "qq":    return QQ;
			case "group": return GROUP;
			default:      return null;
			}
		}
	}
	
}
