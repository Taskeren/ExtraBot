package cn.glycol.extrabot.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ComponentString extends Component {

	@Getter final String message;
	
}
