package koks.manager.event.impl;

import koks.manager.event.Event;

/**
 * @author lmao
 * @created on 18.09.2020 : 20:32
 */
public class EventVelocity extends Event {

    private int horizontal, vertical;

    public EventVelocity(int horizontal, int vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public int getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(int horizontal) {
        this.horizontal = horizontal;
    }

    public int getVertical() {
        return vertical;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    }

}