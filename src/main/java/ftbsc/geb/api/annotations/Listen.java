package ftbsc.geb.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks the method as a listener.
 * Its parent must implement the {@link ftbsc.geb.api.IListener} interface.
 * The method should only take a single input value, an event.
 */
@Target(ElementType.METHOD)
public @interface Listen {
	int priority() default 0;
}
