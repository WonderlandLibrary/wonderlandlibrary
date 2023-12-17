// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import net.minecraft.client.entity.AbstractClientPlayer;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.events.callables.EventCancellable;

public class EventCustomModel extends EventCancellable implements Event
{
    private AbstractClientPlayer player;
    
    public EventCustomModel(final AbstractClientPlayer player) {
        this.player = player;
    }
    
    public void setPlayer(final AbstractClientPlayer player) {
        this.player = player;
    }
    
    public AbstractClientPlayer getPlayer() {
        return this.player;
    }
}
