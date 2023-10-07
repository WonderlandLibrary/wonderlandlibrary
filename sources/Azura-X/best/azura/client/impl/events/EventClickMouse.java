package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;
import net.minecraft.util.MovingObjectPosition;

public class EventClickMouse implements NamedEvent {
    private int button;
    private boolean cancelled;
    private MovingObjectPosition objectMouseOver;

    public EventClickMouse(int button, MovingObjectPosition mouseOver) {
        this.button = button;
        this.cancelled = false;
        this.objectMouseOver = mouseOver;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public MovingObjectPosition getObjectMouseOver() {
        return objectMouseOver;
    }

    public void setObjectMouseOver(MovingObjectPosition objectMouseOver) {
        this.objectMouseOver = objectMouseOver;
    }

    @Override
    public String name() {
        return "clickMouse";
    }
}