package cn.glycol.extrabot.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ComponentRecord extends Component {

	@Getter final String file;
	@Getter final boolean magic;
	
}
