// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import vip.Resolute.events.Event;

public class EventUpdateModel extends Event<EventUpdateModel>
{
    public Entity entity;
    public ModelPlayer modelPlayer;
    
    public EventUpdateModel(final Entity entity, final ModelPlayer modelPlayer) {
        this.entity = entity;
        this.modelPlayer = modelPlayer;
    }
}
