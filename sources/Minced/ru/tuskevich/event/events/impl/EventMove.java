// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import ru.tuskevich.event.events.Event;

public class EventMove implements Event
{
    private Vec3d from;
    private Vec3d to;
    private Vec3d motion;
    private Vec3d collisionOffset;
    private boolean toGround;
    private AxisAlignedBB aabbFrom;
    public boolean ignoreHorizontal;
    public boolean ignoreVertical;
    public boolean collidedHorizontal;
    public boolean collidedVertical;
    
    public EventMove(final Vec3d from, final Vec3d to, final Vec3d motion, final Vec3d collisionOffset, final boolean toGround, final boolean isCollidedHorizontal, final boolean isCollidedVertical, final AxisAlignedBB aabbFrom) {
        this.from = from;
        this.to = to;
        this.motion = motion;
        this.collisionOffset = collisionOffset;
        this.toGround = toGround;
        this.collidedHorizontal = isCollidedHorizontal;
        this.collidedVertical = isCollidedVertical;
        this.aabbFrom = aabbFrom;
    }
    
    public void setIgnoreHorizontalCollision() {
        this.ignoreHorizontal = true;
    }
    
    public void setIgnoreVerticalCollision() {
        this.ignoreVertical = true;
    }
    
    public boolean isIgnoreHorizontal() {
        return this.ignoreHorizontal;
    }
    
    public AxisAlignedBB getAABBFrom() {
        return this.aabbFrom;
    }
    
    public boolean isIgnoreVertical() {
        return this.ignoreVertical;
    }
    
    public boolean isCollidedHorizontal() {
        return this.collidedHorizontal;
    }
    
    public boolean isCollidedVertical() {
        return this.collidedVertical;
    }
    
    public boolean toGround() {
        return this.toGround;
    }
    
    public Vec3d from() {
        return this.from;
    }
    
    public Vec3d to() {
        return this.to;
    }
    
    public Vec3d motion() {
        return this.motion;
    }
    
    public Vec3d collisionOffset() {
        return this.collisionOffset;
    }
}
