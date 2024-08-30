package ftbsc.geb;

import ftbsc.geb.api.IBus;
import ftbsc.geb.api.IEvent;
import ftbsc.geb.api.IEventDispatcher;
import ftbsc.geb.api.IListener;

import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The official GEB implementation of {@link IBus}.
 * @since 0.1.0
 */
public class GEB implements IBus {
	/**
	 * A {@link Map} tying each listener class to its instance.
	 */
	private final Map<Class<? extends IListener>, Set<IListener>> listenerMap;

	/**
	 * A {@link Map} tying each event class to the appropriate dispatcher.
	 */
	private final Map<Class<? extends IEvent>, IEventDispatcher> dispatchMap;

	/**
	 * The default public constructor.
	 */
	public GEB() {
		this.listenerMap = new ConcurrentHashMap<>();
		this.dispatchMap = new ConcurrentHashMap<>();
		for(IEventDispatcher dispatcher : ServiceLoader.load(IEventDispatcher.class))
			this.dispatchMap.put(dispatcher.eventType(), dispatcher);
	}

	/**
	 * Registers a new listener on the bus.
	 * @param listener the listener
	 */
	@Override
	public void registerListener(IListener listener) {
		this.listenerMap.putIfAbsent(
			listener.getClass(),
			ConcurrentHashMap.newKeySet()
		);

		this.listenerMap.get(listener.getClass()).add(listener);
	}

	/**
	 * Unregister a listener from the bus.
	 * @param listener the listener
	 */
	@Override
	public void unregisterListener(IListener listener) {
		this.listenerMap.computeIfPresent(
			listener.getClass(),
			(k, l) -> {
				l.remove(listener);
				return l;
			}
		);
	}

	@Override
	public boolean isRegistered(IListener listener) {
		Set<IListener> listeners = this.listenerMap.get(listener.getClass());
		return listeners != null && listeners.contains(listener);
	}

	/**
	 * Dispatches an event, calling all of its listeners that are subscribed to this bus.
	 * @param event the event to fire
	 * @return false if the event was canceled, true otherwise
	 */
	@Override
	public boolean handleEvent(IEvent event) {
		return Optional.ofNullable(this.dispatchMap.get(event.getClass()))
			.map(dispatcher -> dispatcher.callListeners(event, this.listenerMap))
			.orElse(true);
	}
}
