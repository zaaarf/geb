package ftbsc.geb.api;

/**
 * A generic interface for a bus that can work with this
 * event system.
 * @since 0.1.0
 */
public interface IBus {
	/**
	 * Registers a new listener on the bus.
	 * @param listener the listener
	 */
	void registerListener(IListener listener);

	/**
	 * Unregister a listener from the bus.
	 * While sensible implementations can get this quite fast, it's generally
	 * faster to use {@link IListener#isActive()}, so only use this if you
	 * *mean* to unregister for good.
	 * @param listener the listener
	 */
	void unregisterListener(IListener listener);

	/**
	 * Dispatches an event, calling all of its listeners that are subscribed to this bus.
	 * @param event the event to fire
	 * @return true if the event was canceled, false otherwise
	 */
	boolean handleEvent(IEvent event);
}
