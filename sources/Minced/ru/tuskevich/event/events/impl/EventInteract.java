// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import net.minecraft.entity.Entity;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.events.callables.EventCancellable;

public class EventInteract extends EventCancellable implements Event
{
    public Entity entity;
    
    public EventInteract(final Entity entity) {
        this.entity = entity;
    }
}
