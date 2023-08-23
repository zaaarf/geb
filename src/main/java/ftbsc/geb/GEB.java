package ftbsc.geb;

import ftbsc.geb.api.IBus;
import ftbsc.geb.api.IEvent;
import ftbsc.geb.api.IEventDispatcher;
import ftbsc.geb.api.IListener;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The official GEB implementation of {@link IBus}.
 * @since 0.1.0
 */
public class GEB implements IBus {

	/**
	 * The identifier of this bus. Methods
	 */
	private final String identifier;

	/**
	 * A {@link Map} tying each listener class to its instance.
	 */
	private final Map<Class<? extends IListener>, IListener> listenerMap;

	/**
	 * A {@link Map} tying each event class to the appropriate dispatcher.
	 */
	private final Map<Class<? extends IEvent>, IEventDispatcher> dispatchMap;

	/**
	 * The public constructor.
	 * @param identifier a {@link String} uniquely identifying this bus
	 */
	public GEB(String identifier) {
		this.identifier = identifier;
		this.listenerMap = new ConcurrentHashMap<>();
		this.dispatchMap = new ConcurrentHashMap<>();
		for(IEventDispatcher dispatcher : ServiceLoader.load(IEventDispatcher.class))
			dispatchMap.put(dispatcher.eventType(), dispatcher);
	}

	/**
	 * Registers a new listener on the bus.
	 * @param listener the listener
	 */
	@Override
	public void registerListener(IListener listener) {
		this.listenerMap.put(listener.getClass(), listener);
	}

	/**
	 * Dispatches an event, calling all of its listeners that are subscribed to this bus.
	 * @param event the event to fire
	 * @return true if the event was canceled, false otherwise
	 */
	@Override
	public boolean handleEvent(IEvent event) {
		return this.dispatchMap.get(event.getClass()).callListeners(this.identifier, event, this.listenerMap);
	}
}
