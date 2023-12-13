// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import net.minecraft.entity.player.EntityPlayer;
import com.elementars.eclient.event.Event;

public class EventTotemPop extends Event
{
    EntityPlayer player;
    
    public EventTotemPop(final EntityPlayer entityPlayerIn) {
        this.player = entityPlayerIn;
    }
    
    public EntityPlayer getPlayer() {
        return this.player;
    }
}
