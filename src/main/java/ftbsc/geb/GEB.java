package ftbsc.geb;

import ftbsc.geb.api.IBus;
import ftbsc.geb.api.IEvent;

import java.lang.reflect.Constructor;
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
	 * A {@link Map} tying each user-defined event class to its machine-generated implementation.
	 * In practice, the {@link Class} of the original event is used as key and mapped to each
	 * class' generated {@link Constructor}.
	 */
	private final Map<Class<? extends IEvent>, Constructor<? extends IEvent>> eventMapper;

	/**
	 * The public constructor.
	 * @param identifier a {@link String} uniquely identifying this bus
	 */
	public GEB(String identifier) {
		this.identifier = identifier;
		this.eventMapper = new ConcurrentHashMap<>();
		for(IEvent event : ServiceLoader.load(IEvent.class)) {
			try {
				eventMapper.put(event.getOriginalEvent(), event.getClass().getConstructor(event.getClass()));
			} catch(NoSuchMethodException ignored) {} //should never happen, this code is machine-generated
		}
	}

	/**
	 * @return the identifier of this bus
	 */
	@Override
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Dispatches an event, calling all of its listeners that are subscribed to this bus.
	 * TODO: make this as efficient as possible
	 * @param event the event to fire
	 * @return true if the event was canceled, false otherwise
	 */
	@Override
	public boolean handleEvent(IEvent event) {
		try {
			return eventMapper.get(event.getClass()).newInstance(event).callListeners(this.getIdentifier());
		} catch(ReflectiveOperationException ignored) {} //should never happen
		return false;
	}
}
