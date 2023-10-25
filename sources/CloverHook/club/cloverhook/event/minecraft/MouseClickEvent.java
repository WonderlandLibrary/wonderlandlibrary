package club.cloverhook.event.minecraft;

/**
 * @author antja03
 */
public class MouseClickEvent {
    private int mouseButton;

    public MouseClickEvent(int mouseButton) {
        this.mouseButton = mouseButton;
    }

    public int getMouseButton() {
        return mouseButton;
    }
}
