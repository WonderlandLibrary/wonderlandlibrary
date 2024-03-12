// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.render;

import net.minecraft.tileentity.TileEntityChest;
import dev.tenacity.event.Event;

public class RenderChestEvent extends Event
{
    private final TileEntityChest entity;
    private final Runnable chestRenderer;
    
    public RenderChestEvent(final TileEntityChest entity, final Runnable chestRenderer) {
        this.entity = entity;
        this.chestRenderer = chestRenderer;
    }
    
    public TileEntityChest getEntity() {
        return this.entity;
    }
    
    public void drawChest() {
        this.chestRenderer.run();
    }
}
