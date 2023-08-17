package ftbsc.geb.api;

public interface IEvent {
	default boolean isCanceled() {
		return false;
	}

	void dispatch();
}
