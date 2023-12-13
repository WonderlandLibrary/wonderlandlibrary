// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import com.elementars.eclient.event.Event;

public class EventItemRender extends Event
{
    public Entity entity;
    public ICamera camera;
    public float n;
    
    public EventItemRender(final Entity entity, final ICamera camera, final float n) {
        this.entity = entity;
        this.camera = camera;
        this.n = n;
    }
}
