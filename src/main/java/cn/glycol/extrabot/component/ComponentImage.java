package cn.glycol.extrabot.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ComponentImage extends Component {

	@Getter final String file;
	@Getter final String url;
	
}
