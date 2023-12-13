package vip.radium.event.impl.render.framebuffer;

import vip.radium.event.Event;

public final class FrameBufferResizeEvent implements Event {

    private final int width;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public FrameBufferResizeEvent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private final int height;

}
