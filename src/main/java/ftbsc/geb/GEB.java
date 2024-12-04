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
	private final Map<Class<? extends IEvent>, IEventDispatcher<?>> dispatchMap;

	/**
	 * The default public constructor.
	 */
	public GEB() {
		this.listenerMap = new ConcurrentHashMap<>();
		this.dispatchMap = new ConcurrentHashMap<>();
		for(IEventDispatcher<?> dispatcher : ServiceLoader.load(IEventDispatcher.class))
			this.dispatchMap.put(dispatcher.eventType(), dispatcher);
	}

	@Override
	public void registerListener(IListener listener) {
		this.listenerMap.putIfAbsent(
			listener.getClass(),
			ConcurrentHashMap.newKeySet()
		);

		this.listenerMap.get(listener.getClass()).add(listener);
	}

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

	@Override
	public boolean handleEvent(IEvent event) {
		return Optional.ofNullable(this.dispatchMap.get(event.getClass()))
			.map(dispatcher -> this.handleDispatch(dispatcher, event))
			.orElse(true);
	}

	@SuppressWarnings("unchecked") // wild casts are bad but better than reflection
	private <T extends IEvent> boolean handleDispatch(IEventDispatcher<T> dispatcher, IEvent event) {
		return dispatcher.callListeners((T) event, this.listenerMap);
	}
}
