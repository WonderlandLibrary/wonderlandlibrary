package net.caffeinemc.phosphor.api.event.events;

@SuppressWarnings("all")
public class MouseMoveEvent {

	private static final MouseMoveEvent INSTANCE = new MouseMoveEvent();

	public double mouseX, mouseY;

	public static MouseMoveEvent get(double mouseX, double mouseY) {
		INSTANCE.mouseX = mouseX;
		INSTANCE.mouseY = mouseY;
		return INSTANCE;
	}

}
