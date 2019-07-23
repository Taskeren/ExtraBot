package cn.glycol.extrabot.registration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoRegister {
	
	public enum Type {
		COMMAND, LISTENER;
	}
	
	Type value();
	
}
