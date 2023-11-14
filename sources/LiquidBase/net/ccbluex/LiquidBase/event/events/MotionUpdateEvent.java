package net.ccbluex.LiquidBase.event.events;

import net.ccbluex.LiquidBase.event.Event;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
public class MotionUpdateEvent extends Event {

    private State state;

    public MotionUpdateEvent(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public enum State {
        PRE, POST;
    }
}