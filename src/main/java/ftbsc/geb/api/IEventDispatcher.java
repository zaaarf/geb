package ftbsc.geb.api;

import java.util.Map;

/**
 * The interface that the generated dispatchers will all use.
 * This interface isn't really meant to be used by humans, but it should work if your
 * use case requires it.
 * @since 0.1.1
 */
public interface IEventDispatcher {
	/**
	 * Calls all listeners for the given event.
	 * @param event the event to call
	 * @param listeners a map mapping each {@link IListener} class to its instance
	 * @return the value {@link IBus#handleEvent(IEvent)} will return for this
	 */
	boolean callListeners(IEvent event, Map<Class<? extends IListener>, IListener> listeners);

	/**
	 * @return the {@link Class} representing the event this dispatcher works with
	 */
	Class<? extends IEvent> eventType();
}
