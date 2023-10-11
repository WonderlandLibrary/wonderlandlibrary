package com.initial.events.impl;

import com.initial.events.*;

public class EventRenderScoreboard extends Event
{
    State state;
    
    public EventRenderScoreboard(final State state) {
        this.state = state;
    }
    
    public State getState() {
        return this.state;
    }
    
    @Override
    public boolean isPre() {
        return this.state == State.PRE;
    }
    
    public void setState(final State state) {
        this.state = state;
    }
}
