package rip.helium.event.minecraft;

/**
 * @author antja03
 */
public class MouseClickEvent {
    private final int mouseButton;

    public MouseClickEvent(int mouseButton) {
        this.mouseButton = mouseButton;
    }

    public int getMouseButton() {
        return mouseButton;
    }
}
