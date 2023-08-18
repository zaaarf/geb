package ftbsc.geb.api;

/**
 * The common interface for all GEB events.
 * @since 0.1.0
 */
public interface IEvent {
	/**
	 * Any custom implementation of this method will be called just before the
	 * calls to listeners, but its return values will be ignored. You probably
	 * don't want to touch this.
	 * @param identifier the identifier of the bus that's calling this
	 * @return the value {@link IBus#handleEvent(IEvent)} will return for this
	 */
	default boolean callListeners(String identifier) {
		return false;
	}

	/**
	 * Fetches the {@link Class} containing the original implementation of this.
	 * If you wish to override this, make your override final.
	 * @return the class of the original, non-extended superclass
	 */
	default Class<? extends IEvent> getOriginalEvent() {
		return IEvent.class;
	}
}
