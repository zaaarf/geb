package foo.zaaarf.geb;

import foo.zaaarf.geb.api.IBus;
import foo.zaaarf.geb.api.IEvent;
import foo.zaaarf.geb.api.IEventDispatcher;
import foo.zaaarf.geb.api.IListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The official GEB implementation of {@link IBus}.
 * This implementation has one additional stipulation: there can be at most one registered
 * instance of every listener class.
 * @since 0.1.0
 */
public class GEB implements IBus {
	/**
	 * An ordered {@link List} of the buses to call, including self and all sub buses.
	 */
	protected final List<IBus> busesToCall;

	/**
	 * A {@link Map} that keeps track of the priorities of sub-buses that were registered here.
	 */
	protected final Map<IBus, Integer> busPriorities;

	/**
	 * A {@link Map} tying each listener class to its instance.
	 */
	protected final Map<Class<? extends IListener>, Set<IListener>> listenerMap;

	/**
	 * A {@link Map} tying each event class to the appropriate dispatcher.
	 */
	protected final Map<Class<? extends IEvent>, IEventDispatcher<?>> dispatchMap;

	/**
	 * A flag, updated on {@link #registerSubBus(IBus, int)} and {@link #unregisterSubBus(IBus)},
	 * which avoids the (minor) overhead of sub-bus logic if it's not necessary.
	 */
	protected boolean hasSubBuses = false;

	/**
	 * The default public constructor.
	 */
	public GEB() {
		// sub-bus stuff
		this.busesToCall = new CopyOnWriteArrayList<>(); // fairly expensive to write, but writes should be rare
		this.busPriorities = new ConcurrentHashMap<>();

		// add self (to easily track priority)
		this.busesToCall.add(this);
		this.busPriorities.put(this, 0);

		// load event dispatchers
		this.listenerMap = new ConcurrentHashMap<>();
		this.dispatchMap = new ConcurrentHashMap<>();
		for(IEventDispatcher<?> dispatcher : ServiceLoader.load(IEventDispatcher.class)) {
			this.dispatchMap.put(dispatcher.eventType(), dispatcher);
		}
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
		if(this.hasSubBuses) {
			for(IBus bus : this.busesToCall) {
				if(
					(bus == this && !this.handleEventSingle(event))
						|| !bus.handleEvent(event)
				) {
					return false;
				}
			}

			return true;
		}

		// sane case with no sub buses
		return this.handleEventSingle(event);
	}

	/**
	 * Handles the event, only for this single bus.
	 * @param event the event to fire
	 * @return false if the event was canceled, true otherwise
	 */
	protected boolean handleEventSingle(IEvent event) {
		return Optional.ofNullable(this.dispatchMap.get(event.getClass()))
			.map(dispatcher -> this.handleDispatch(dispatcher, event))
			.orElse(true);
	}

	@SuppressWarnings("unchecked") // wild casts are bad but better than reflection
	private <T extends IEvent> boolean handleDispatch(IEventDispatcher<T> dispatcher, IEvent event) {
		return dispatcher.callListeners((T) event, this.listenerMap);
	}

	@Override
	public void registerSubBus(IBus subBus, int priority) {
		if(subBus == this) { // prevent user from doing something stupid
			return;
		}

		// prevent user from adding duplicates
		if(this.busPriorities.containsKey(subBus)) {
			this.unregisterSubBus(subBus);
		}

		this.busPriorities.put(subBus, priority);
		this.busesToCall.add(subBus);
		this.busesToCall.sort(Comparator.comparingInt(this.busPriorities::get));

		this.hasSubBuses = this.busesToCall.size() == 1;
	}

	@Override
	public void unregisterSubBus(IBus subBus) {
		if(subBus == this) { // prevent user from doing something stupid
			return;
		}

		this.busPriorities.remove(subBus);
		this.busesToCall.remove(subBus); // sort order should still be fine

		this.hasSubBuses = this.busesToCall.size() == 1;
	}

	@Override
	public boolean isRegistered(IBus subBus) {
		return this.busPriorities.containsKey(subBus);
	}
}
