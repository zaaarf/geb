package ftbsc.geb.api;

/**
 * The common interface for all cancelable GEB events.
 * @since 0.1.0
 */
public interface IEventCancelable extends IEvent {
	/**
	 * Checks whether the event was canceled; any user-defined
	 * implementation will be ignored.
	 * @return whether the event was canceled
	 */
	default boolean isCanceled() {
		return false;
	}

	/**
	 * Cancels the event. Any user-defined implementation will
	 * be ignored.
	 */
	default void setCanceled() {}
}
