// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import net.minecraft.inventory.ContainerChest;
import vip.Resolute.events.Event;

public class EventOpenChest extends Event<EventOpenChest>
{
    ContainerChest chest;
    
    public EventOpenChest(final ContainerChest chest) {
        this.chest = chest;
    }
    
    public ContainerChest getChest() {
        return this.chest;
    }
    
    public void setChest(final ContainerChest chest) {
        this.chest = chest;
    }
}
