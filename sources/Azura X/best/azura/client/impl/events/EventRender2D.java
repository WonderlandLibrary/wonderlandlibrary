package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;

public class EventRender2D implements NamedEvent {

	private final float renderPartialTicks;

	public EventRender2D(final float renderPartialTicks) {
		this.renderPartialTicks = renderPartialTicks;
	}

	public float getRenderPartialTicks() {
		return renderPartialTicks;
	}

	@Override
	public String name() {
		return "render2D";
	}
}
