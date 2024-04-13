package dev.tenacity.ui;

public final class Drag {

    private float x;
    private float y;
    private final float initialX;
    private final float initialY;
    private float startX, startY;
    private boolean dragging;

    public Drag(final float initialXVal, final float initialYVal) {
        this.initialX = initialXVal;
        this.initialY = initialYVal;
        this.x = initialXVal;
        this.y = initialYVal;
    }

    public void onDraw(final int mouseX, final int mouseY) {
        if (dragging) {
            x = (mouseX - startX);
            y = (mouseY - startY);
        }
    }

    public void onClick(final int mouseX, final int mouseY, final int button, final boolean canDrag) {
        if (button == 0 && canDrag) {
            dragging = true;
            startX = (int) (mouseX - x);
            startY = (int) (mouseY - y);
        }
    }

    public void onRelease(final int button) {
        if (button == 0) dragging = false;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getInitialX() {
        return initialX;
    }

    public float getInitialY() {
        return initialY;
    }

    public boolean isDragging() {
        return dragging;
    }
}
