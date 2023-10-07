/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.event.impl;

import me.arithmo.event.Event;

public class EventMouse
extends Event {
    private int buttonID;
    private boolean mouseDown;

    public void fire(int buttonID, boolean mouseDown) {
        this.buttonID = buttonID;
        super.fire();
    }

    public int getButtonID() {
        return this.buttonID;
    }

    public void setButtonID(int buttonID) {
        this.buttonID = buttonID;
    }

    public boolean isMouseDown() {
        return this.mouseDown;
    }

    public boolean isMotionEvent() {
        return this.buttonID == -1;
    }

    public void setMouseDown(boolean mouseDown) {
        this.mouseDown = mouseDown;
    }
}

