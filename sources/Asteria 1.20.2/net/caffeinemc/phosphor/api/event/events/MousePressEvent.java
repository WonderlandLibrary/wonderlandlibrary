package net.caffeinemc.phosphor.api.event.events;

@SuppressWarnings("all")
public class MousePressEvent {

	private static final MousePressEvent INSTANCE = new MousePressEvent();

	public int button, action;

	public static MousePressEvent get(int button, int action) {
		INSTANCE.button = button;
		INSTANCE.action = action;
		return INSTANCE;
	}

}
