package graphite.impl.gui.click.component;

import graphite.impl.gui.click.framework.Component;
import net.minecraft.client.gui.ScaledResolution;

public class WindowComponent extends Component {
    private boolean dragging;
    private double prevX, prevY;

    private boolean draggable;

    public WindowComponent(Component parent, double x, double y, double width, double height) {
        super(parent, x, y, width, height);
    }

    protected void onHandleDragging(final ScaledResolution resolution, final double mouseX, final double mouseY) {
        if (!this.isDraggable()) {
            this.dragging = false;
            return;
        }

        if (this.dragging) {
            this.setX(Math.max(0, Math.min(resolution.getScaledWidth() - getWidth(), mouseX - this.prevX)));
            this.setY(Math.max(0, Math.min(resolution.getScaledHeight() - getHeight(), mouseY - this.prevY)));
        }
    }

    protected void onStopDragging() {
        if (this.dragging) this.dragging = false;
    }

    protected void onStartDragging(final double mouseX, final double mouseY) {
        if (this.isDraggable() && !this.dragging) {
            this.dragging = true;
            this.prevX = mouseX - this.getX();
            this.prevY = mouseY - this.getY();
        }
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }
}