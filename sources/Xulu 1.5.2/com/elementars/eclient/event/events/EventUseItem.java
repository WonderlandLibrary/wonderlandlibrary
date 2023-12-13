// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import net.minecraft.entity.player.EntityPlayer;
import com.elementars.eclient.event.Event;

public class EventUseItem extends Event
{
    EntityPlayer player;
    
    public EventUseItem(final EntityPlayer entityPlayer) {
        this.player = entityPlayer;
    }
    
    public EntityPlayer getPlayer() {
        return this.player;
    }
}
