package ftbsc.geb.api.annotations;

import ftbsc.geb.api.IBus;
import ftbsc.geb.api.IEvent;
import ftbsc.geb.api.IEventCancelable;
import ftbsc.geb.api.IListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the method as a listener. Its parent must implement the {@link IListener} interface
 * and be registered an at least one GEB instance with {@link IBus#registerListener(IListener)}.
 * The annotated method should only take a single input value, an instance of {@link IEvent} or
 * {@link IEventCancelable}.
 * For the annotation to work, you must be using the GEB annotation processor or an equivalent.
 * @since 0.1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Listen {
	/**
	 * @return an integer indicating priority level for the listener, defaulting to 0;
	 * 				 a higher value means it's executed before; it may be negative
	 */
	int priority() default 0;
}
