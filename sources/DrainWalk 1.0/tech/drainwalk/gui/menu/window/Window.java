package tech.drainwalk.gui.menu.window;

import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.animation.Animation;
import tech.drainwalk.gui.menu.hovered.Hovered;

public class Window {
    @Getter
    @Setter
    private final Animation animation = new Animation();
    @Setter
    @Getter
    protected float windowX = 0, windowY = 0, windowWidth, windowHeight;
    @Getter
    @Setter
    protected boolean inWindowBound;
    @Setter
    @Getter
    protected boolean windowActive = false;
    @Setter
    @Getter
    private float activeX, activeY;
    @Setter
    @Getter
    private boolean activeDragging = false;
    @Setter
    @Getter
    private boolean canDragging = true;
    @Getter
    private final boolean windowDraggable;

    public Window(boolean windowDraggable) {
        this.windowDraggable = windowDraggable;
    }

    public void renderWindow(int mouseX, int mouseY, float partialTicks) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
    public void updateScreen(int mouseX, int mouseY) {
        setInWindowBound(Hovered.isHovered(mouseX, mouseY, windowX, windowY, windowWidth, windowHeight));
    }

}
