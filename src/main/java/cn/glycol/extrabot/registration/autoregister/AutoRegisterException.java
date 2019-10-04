package cn.glycol.extrabot.registration.autoregister;

public class AutoRegisterException extends RuntimeException {
	
	private static final long serialVersionUID = -5430708488684346841L;

	public AutoRegisterException(String msg, Object...format) {
		super(String.format(msg, format));
	}
	
}
