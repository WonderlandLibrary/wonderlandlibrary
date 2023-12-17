// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityLiving;

public class EntityMoveHelper
{
    protected final EntityLiving entity;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected double speed;
    protected float moveForward;
    protected float moveStrafe;
    public Action action;
    
    public EntityMoveHelper(final EntityLiving entitylivingIn) {
        this.action = Action.WAIT;
        this.entity = entitylivingIn;
    }
    
    public boolean isUpdating() {
        return this.action == Action.MOVE_TO;
    }
    
    public double getSpeed() {
        return this.speed;
    }
    
    public void setMoveTo(final double x, final double y, final double z, final double speedIn) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.speed = speedIn;
        this.action = Action.MOVE_TO;
    }
    
    public void strafe(final float forward, final float strafe) {
        this.action = Action.STRAFE;
        this.moveForward = forward;
        this.moveStrafe = strafe;
        this.speed = 0.25;
    }
    
    public void read(final EntityMoveHelper that) {
        this.action = that.action;
        this.posX = that.posX;
        this.posY = that.posY;
        this.posZ = that.posZ;
        this.speed = Math.max(that.speed, 1.0);
        this.moveForward = that.moveForward;
        this.moveStrafe = that.moveStrafe;
    }
    
    public void onUpdateMoveHelper() {
        if (this.action == Action.STRAFE) {
            final float f = (float)this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
            float f2 = (float)this.speed * f;
            float f3 = this.moveForward;
            float f4 = this.moveStrafe;
            float f5 = MathHelper.sqrt(f3 * f3 + f4 * f4);
            if (f5 < 1.0f) {
                f5 = 1.0f;
            }
            f5 = f2 / f5;
            f3 *= f5;
            f4 *= f5;
            final float f6 = MathHelper.sin(this.entity.rotationYaw * 0.017453292f);
            final float f7 = MathHelper.cos(this.entity.rotationYaw * 0.017453292f);
            final float f8 = f3 * f7 - f4 * f6;
            final float f9 = f4 * f7 + f3 * f6;
            final PathNavigate pathnavigate = this.entity.getNavigator();
            if (pathnavigate != null) {
                final NodeProcessor nodeprocessor = pathnavigate.getNodeProcessor();
                if (nodeprocessor != null && nodeprocessor.getPathNodeType(this.entity.world, MathHelper.floor(this.entity.posX + f8), MathHelper.floor(this.entity.posY), MathHelper.floor(this.entity.posZ + f9)) != PathNodeType.WALKABLE) {
                    this.moveForward = 1.0f;
                    this.moveStrafe = 0.0f;
                    f2 = f;
                }
            }
            this.entity.setAIMoveSpeed(f2);
            this.entity.setMoveForward(this.moveForward);
            this.entity.setMoveStrafing(this.moveStrafe);
            this.action = Action.WAIT;
        }
        else if (this.action == Action.MOVE_TO) {
            this.action = Action.WAIT;
            final double d0 = this.posX - this.entity.posX;
            final double d2 = this.posZ - this.entity.posZ;
            final double d3 = this.posY - this.entity.posY;
            final double d4 = d0 * d0 + d3 * d3 + d2 * d2;
            if (d4 < 2.500000277905201E-7) {
                this.entity.setMoveForward(0.0f);
                return;
            }
            final float f10 = (float)(MathHelper.atan2(d2, d0) * 57.29577951308232) - 90.0f;
            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f10, 90.0f);
            this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
            if (d3 > this.entity.stepHeight && d0 * d0 + d2 * d2 < Math.max(1.0f, this.entity.width)) {
                this.entity.getJumpHelper().setJumping();
                this.action = Action.JUMPING;
            }
        }
        else if (this.action == Action.JUMPING) {
            this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
            if (this.entity.onGround) {
                this.action = Action.WAIT;
            }
        }
        else {
            this.entity.setMoveForward(0.0f);
        }
    }
    
    protected float limitAngle(final float sourceAngle, final float targetAngle, final float maximumChange) {
        float f = MathHelper.wrapDegrees(targetAngle - sourceAngle);
        if (f > maximumChange) {
            f = maximumChange;
        }
        if (f < -maximumChange) {
            f = -maximumChange;
        }
        float f2 = sourceAngle + f;
        if (f2 < 0.0f) {
            f2 += 360.0f;
        }
        else if (f2 > 360.0f) {
            f2 -= 360.0f;
        }
        return f2;
    }
    
    public double getX() {
        return this.posX;
    }
    
    public double getY() {
        return this.posY;
    }
    
    public double getZ() {
        return this.posZ;
    }
    
    public enum Action
    {
        WAIT, 
        MOVE_TO, 
        STRAFE, 
        JUMPING;
    }
}
