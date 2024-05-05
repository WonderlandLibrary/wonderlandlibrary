package com.initial.events.impl;

import com.initial.events.*;

public class EventSafewalk extends Event
{
    private boolean safewalk;
    
    public boolean isSafewalk() {
        return this.safewalk;
    }
    
    public void setSafewalk(final boolean safewalk) {
        this.safewalk = safewalk;
    }
}
