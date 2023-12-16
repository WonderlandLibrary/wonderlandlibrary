package org.luaj.vm2.customs.events;

import org.luaj.vm2.customs.EventHook;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventJump;
import wtf.expensive.events.impl.player.EventMotion;

public class EventJumpHook extends EventHook {

    private EventJump jump;

    public EventJumpHook(Event event) {
        super(event);
        this.jump = (EventJump) event;
    }




    @Override
    public String getName() {
        return "jump_event";
    }
}
