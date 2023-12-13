package io.github.nevalackin.client.ui.nl.components;

import io.github.nevalackin.client.ui.framework.Animated;
import io.github.nevalackin.client.ui.framework.Component;

public class WindowComponent extends Component implements Animated {

    private boolean dragging;
    private double prevX, prevY;

    protected double fadeInProgress;

    private final Decoration[] decorations;

    private boolean draggable, resizable;

    public WindowComponent(Component parent,
                           double x, double y, double width, double height,
                           Decoration... decorations) {
        super(parent, x, y, width, height);

        this.decorations = decorations;
    }

    protected void onHandleDragging(final double mouseX, final double mouseY) {
        if (!this.isDraggable()) {
            this.dragging = false;
            return;
        }

        if (this.dragging) {
            this.setX(mouseX - this.prevX);
            this.setY(mouseY - this.prevY);
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

    public Decoration[] getDecorations() {
        return decorations;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    @Override
    public void resetAnimationState() {
        this.fadeInProgress = 0.0;
    }

    public enum Decoration {
        CLOSE, DOCK
    }
}
