package ftbsc.geb.api;

/**
 * The common interface for all cancelable GEB events.
 * @since 0.1.0
 */
public interface IEventCancelable extends IEvent {
	/**
	 * Checks whether the event was canceled.
	 * @return whether the event was canceled
	 */
	default boolean isCanceled() {
		return false;
	}

	/**
	 * Cancels the event.
	 */
	default void setCanceled() {
		this.setCanceled(true);
	}

	/**
	 * Cancels the event.
	 * @param canceled whether the event should be set to canceled
	 */
	default void setCanceled(boolean canceled) {}
}
