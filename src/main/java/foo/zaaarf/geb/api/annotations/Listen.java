package foo.zaaarf.geb.api.annotations;

import foo.zaaarf.geb.api.IBus;
import foo.zaaarf.geb.api.IEvent;
import foo.zaaarf.geb.api.IEventCancelable;
import foo.zaaarf.geb.api.IListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the method as a listener.
 * If the method is not static, its parent must implement the {@link IListener} interface
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
	 * Specifies the priority for this listener, defaulting to 0.
	 * A higher value means it's executed before, and it may be negative.
	 * @return an integer indicating priority level for the listener
	 */
	int priority() default 0;

	/**
	 * Flags this listener as inheritable.
	 * Classes extending this class with {@link Inherit} will also be called with this, provided
	 * that this listener is marked as inheritable.
	 * Static listeners will never be inherited.
	 * @return whether this listener is inheritable
	 */
	boolean inheritable() default true;
}
