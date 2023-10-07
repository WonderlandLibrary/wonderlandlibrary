package sudo.events;

import sudo.core.event.Event;

public class EventMouseButton extends Event {

    private int button;
    private ClickType clickType;

    public EventMouseButton(int button, ClickType clickType) {
        this.button = button;
        this.clickType = clickType;
    }

    public int getButton() {
        return button;
    }

    public ClickType getClickType() {
        return clickType;
    }

    public enum ClickType {
        IN_GAME,
        IN_MENU
    }
}