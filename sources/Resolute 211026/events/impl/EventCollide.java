// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import vip.Resolute.events.Event;

public class EventCollide extends Event<EventCollide>
{
    private AxisAlignedBB axisalignedbb;
    private final Block block;
    private final Entity collidingEntity;
    private final int x;
    private final int y;
    private final int z;
    
    public EventCollide(final Entity collidingEntity, final int x, final int y, final int z, final AxisAlignedBB axisalignedbb, final Block block) {
        this.collidingEntity = collidingEntity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.axisalignedbb = axisalignedbb;
        this.block = block;
    }
    
    public AxisAlignedBB getBoundingBox() {
        return this.axisalignedbb;
    }
    
    public Entity getCollidingEntity() {
        return this.collidingEntity;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public void setBoundingBox(final AxisAlignedBB object) {
        this.axisalignedbb = object;
    }
}
