package koks.manager.event.impl;

import koks.manager.event.Event;

/**
 * @author Kroko, Phantom, Deleteboys, Dirt
 * @created on 08.12.2020 : 17:15
 */
public class EventMoveWithHeading extends Event {

    float f4;

    public EventMoveWithHeading(float f4) {
        this.f4 = f4;
    }

    public float getF4() {
        return f4;
    }

    public void setF4(float f4) {
        this.f4 = f4;
    }
}
