// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import ru.tuskevich.event.events.Event;

public class EventAction implements Event
{
    public boolean sprintState;
    
    public EventAction(final boolean sprintState) {
        this.sprintState = sprintState;
    }
}
