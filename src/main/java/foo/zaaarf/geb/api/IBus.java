package foo.zaaarf.geb.api;

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
	 * This will not recursively unregister it from eventual sub-buses.
	 * @param listener the listener
	 */
	void unregisterListener(IListener listener);

	/**
	 * Checks whether a listener is currently registered on this bus (excluding sub-buses).
	 * @param listener the listener to check
	 * @return true if the listener is registered
	 */
	boolean isRegistered(IListener listener);

	/**
	 * Dispatches an event, calling all of its listeners that are subscribed to this bus,
	 * and forwarding to all sub buses, if present.
	 * @param event the event to fire
	 * @return false if the event was canceled, true otherwise
	 */
	boolean handleEvent(IEvent event);

	/**
	 * Registers a sub-bus that will also receive this bus' events.
	 * @param subBus the bus to register
	 * @param priority the priority of the sub-bus, will be used to determine whether it is called
	 *                 before or after the parent one, assuming the parent has priority 0
	 */
	void registerSubBus(IBus subBus, int priority);

	/**
	 * Unregisters a sub-bus from this bus.
	 * @param subBus the bus to register
	 */
	void unregisterSubBus(IBus subBus);

	/**
	 * Checks whether a sub-bus is currently registered.
	 * @param subBus the sub-bus to check
	 * @return true if the sub-bus is registered
	 */
	boolean isRegistered(IBus subBus);
}
