package cn.glycol.extrabot.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ComponentRPS extends Component {

	@Getter final RSP type;
	
	public static enum RSP {
		ROCK, PAPER, SCISSORS;
		
		public static RSP of(int i) {
			switch(i) {
			case 1: return ROCK;
			case 2: return SCISSORS;
			case 3: return PAPER;
			default: return null;
			}
		}
	}
	
}
