package net.caffeinemc.phosphor.api.event.events;

@SuppressWarnings("all")
public class KeyPressEvent {

	private static final KeyPressEvent INSTANCE = new KeyPressEvent();

	public int key, scanCode, action;
	public long window;

	public static KeyPressEvent get(int key, int scanCode, int action, long window) {
		INSTANCE.key = key;
		INSTANCE.scanCode = scanCode;
		INSTANCE.action = action;
		INSTANCE.window = window;
		return INSTANCE;
	}

}
