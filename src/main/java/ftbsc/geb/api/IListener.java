package ftbsc.geb.api;

/**
 * The common interface for all classes that contain GEB listeners.
 * @since 0.1.0
 */
public interface IListener {
	/**
	 * @return whether the listeners in this class should be considered to be active
	 */
	boolean isActive();
}
