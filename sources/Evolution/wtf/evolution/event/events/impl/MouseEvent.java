package wtf.evolution.event.events.impl;

import wtf.evolution.event.events.Event;

public class MouseEvent implements Event {

    public int button;

    public MouseEvent(int button) {
        this.button = button;
    }

}
