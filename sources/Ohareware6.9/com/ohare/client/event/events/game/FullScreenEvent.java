package com.ohare.client.event.events.game;

import com.ohare.client.event.Event;

/**
 * made by oHare for Client
 *
 * @since 5/25/2019
 **/
public class FullScreenEvent extends Event {
    private float width,height;

    public FullScreenEvent(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}