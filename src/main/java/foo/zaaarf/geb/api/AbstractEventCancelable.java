package foo.zaaarf.geb.api;

/**
 * Default implementation of {@link IEventCancelable}.
 * @since 0.4.1
 */
public abstract class AbstractEventCancelable implements IEventCancelable {
	/**
	 * Flag that determines whether the event is canceled.
	 */
	protected boolean canceled;

	/**
	 * The default constructor.
	 */
	protected AbstractEventCancelable() {
		this.canceled = false;
	}

	@Override
	public boolean isCanceled() {
		return this.canceled;
	}

	@Override
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}
