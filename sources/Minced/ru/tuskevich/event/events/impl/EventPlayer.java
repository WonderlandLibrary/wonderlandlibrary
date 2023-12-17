// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import net.minecraft.network.play.server.SPacketPlayerListItem;
import ru.tuskevich.event.events.Event;

public class EventPlayer implements Event
{
    private final SPacketPlayerListItem.AddPlayerData addPlayerData;
    private final SPacketPlayerListItem.Action action;
    
    public EventPlayer(final SPacketPlayerListItem.AddPlayerData addPlayerData, final SPacketPlayerListItem.Action action) {
        this.addPlayerData = addPlayerData;
        this.action = action;
    }
    
    public SPacketPlayerListItem.AddPlayerData getPlayerData() {
        return this.addPlayerData;
    }
    
    public SPacketPlayerListItem.Action getAction() {
        return this.action;
    }
}
