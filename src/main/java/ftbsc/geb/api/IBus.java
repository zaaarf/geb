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
	 * Unregisters a listener from the bus.
	 * @param listener the listener
	 */
	void unregisterListener(IListener listener);

	/**
	 * Tells you whether a listener is currently registered.
	 * Ideally this should be efficient.
	 * @return true if the listener is registered
	 */
	boolean isRegistered(IListener listener);

	/**
	 * Dispatches an event, calling all of its listeners that are subscribed to this bus.
	 * @param event the event to fire
	 * @return false if the event was canceled, true otherwise
	 */
	boolean handleEvent(IEvent event);
}
