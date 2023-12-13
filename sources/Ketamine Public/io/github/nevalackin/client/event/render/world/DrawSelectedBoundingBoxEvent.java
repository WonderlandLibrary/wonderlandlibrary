package io.github.nevalackin.client.event.render.world;

import io.github.nevalackin.client.event.Event;

public final class DrawSelectedBoundingBoxEvent implements Event {

    private boolean filled = false;
    private float outlineWidth = 1;
    private int colour = 0x66000000;

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public float getOutlineWidth() {
        return outlineWidth;
    }

    public void setOutlineWidth(float outlineWidth) {
        this.outlineWidth = outlineWidth;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }
}
