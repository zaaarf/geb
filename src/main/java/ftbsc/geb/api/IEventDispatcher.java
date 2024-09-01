package ftbsc.geb.api;

import java.util.Map;
import java.util.Set;

/**
 * The interface that the generated dispatchers will all use.
 * This interface isn't really meant to be used by humans, but it should work if your
 * use case requires it.
 * @param <T> the event this is for
 * @since 0.1.1
 */
public interface IEventDispatcher<T extends IEvent> {
	/**
	 * Calls all listeners for the given event.
	 * @param event the event to call
	 * @param listeners a map mapping each {@link IListener} class to its instances
	 * @return the value {@link IBus#handleEvent(IEvent)} will return for this
	 */
	boolean callListeners(T event, Map<Class<? extends IListener>, Set<IListener>> listeners);

	/**
	 * @return the {@link Class} representing the event this dispatcher works with
	 */
	Class<T> eventType();
}
