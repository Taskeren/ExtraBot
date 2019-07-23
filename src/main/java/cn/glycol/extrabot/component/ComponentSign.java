package cn.glycol.extrabot.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ComponentSign extends Component {

	@Getter final String location;
	@Getter final String title;
	@Getter final String image;
	
}
