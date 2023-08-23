package ftbsc.geb.api;

/**
 * The interface that the generated dispatchers will all use.
 * This interface isn't really meant to be used by humans, but it should work if your
 * use case requires it.
 * @param <E> the type of event this dispatcher handles
 * @since 0.1.1
 */
public interface IEventDispatcher {
	/**
	 * Calls all listeners for the given identifier.
	 * @param identifier the identifier of the bus that's calling this
	 * @param event the event to call
	 * @return the value {@link IBus#handleEvent(IEvent)} will return for this
	 */
	boolean callListeners(String identifier, IEvent event);

	/**
	 * @return the {@link Class} representing the event in question
	 */
	Class<? extends IEvent> eventType();
}
