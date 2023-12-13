package io.github.nevalackin.client.event.render.game;

import io.github.nevalackin.client.event.Event;

public final class UpdateFramebufferSizeEvent implements Event {

    private final int width, height;

    public UpdateFramebufferSizeEvent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
