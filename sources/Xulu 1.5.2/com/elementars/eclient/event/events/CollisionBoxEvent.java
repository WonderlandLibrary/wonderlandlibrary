// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import com.elementars.eclient.event.Event;

public class CollisionBoxEvent extends Event
{
    private final Block block;
    private final BlockPos pos;
    private AxisAlignedBB aabb;
    private final List<AxisAlignedBB> collidingBoxes;
    private final Entity entity;
    
    public CollisionBoxEvent(final Block block, final BlockPos pos, final AxisAlignedBB aabb, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entity) {
        this.block = block;
        this.pos = pos;
        this.aabb = aabb;
        this.collidingBoxes = collidingBoxes;
        this.entity = entity;
    }
    
    public void setAABB(final AxisAlignedBB aabb) {
        this.aabb = aabb;
    }
    
    public final Block getBlock() {
        return this.block;
    }
    
    public final BlockPos getPos() {
        return this.pos;
    }
    
    public final AxisAlignedBB getBoundingBox() {
        return this.aabb;
    }
    
    public final List<AxisAlignedBB> getCollidingBoxes() {
        return this.collidingBoxes;
    }
    
    public final Entity getEntity() {
        return this.entity;
    }
}
