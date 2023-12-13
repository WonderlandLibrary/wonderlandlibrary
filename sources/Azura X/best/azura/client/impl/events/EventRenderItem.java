package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;

public class EventRenderItem implements NamedEvent {
	public boolean isUsing;

	public EventRenderItem(boolean isUsing) {
		this.isUsing = isUsing;
	}

	@Override
	public String name() {
		return "renderItem";
	}
}