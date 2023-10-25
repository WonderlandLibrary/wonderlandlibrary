/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.MathHelper;

public class EntityMoveHelper {
    protected final EntityLiving entity;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected double speed;
    protected float moveForward;
    protected float moveStrafe;
    public Action action = Action.WAIT;

    public EntityMoveHelper(EntityLiving entitylivingIn) {
        this.entity = entitylivingIn;
    }

    public boolean isUpdating() {
        return this.action == Action.MOVE_TO;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setMoveTo(double x, double y, double z, double speedIn) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.speed = speedIn;
        this.action = Action.MOVE_TO;
    }

    public void strafe(float forward, float strafe) {
        this.action = Action.STRAFE;
        this.moveForward = forward;
        this.moveStrafe = strafe;
        this.speed = 0.25;
    }

    public void read(EntityMoveHelper that) {
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
            NodeProcessor nodeprocessor;
            float f = (float)this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
            float f1 = (float)this.speed * f;
            float f2 = this.moveForward;
            float f3 = this.moveStrafe;
            float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);
            if (f4 < 1.0f) {
                f4 = 1.0f;
            }
            f4 = f1 / f4;
            float f5 = MathHelper.sin(this.entity.rotationYaw * ((float)Math.PI / 180));
            float f6 = MathHelper.cos(this.entity.rotationYaw * ((float)Math.PI / 180));
            float f7 = (f2 *= f4) * f6 - (f3 *= f4) * f5;
            float f8 = f3 * f6 + f2 * f5;
            PathNavigate pathnavigate = this.entity.getNavigator();
            if (pathnavigate != null && (nodeprocessor = pathnavigate.getNodeProcessor()) != null && nodeprocessor.getPathNodeType(this.entity.world, MathHelper.floor(this.entity.posX + (double)f7), MathHelper.floor(this.entity.posY), MathHelper.floor(this.entity.posZ + (double)f8)) != PathNodeType.WALKABLE) {
                this.moveForward = 1.0f;
                this.moveStrafe = 0.0f;
                f1 = f;
            }
            this.entity.setAIMoveSpeed(f1);
            this.entity.func_191989_p(this.moveForward);
            this.entity.setMoveStrafing(this.moveStrafe);
            this.action = Action.WAIT;
        } else if (this.action == Action.MOVE_TO) {
            this.action = Action.WAIT;
            double d0 = this.posX - this.entity.posX;
            double d2 = this.posY - this.entity.posY;
            double d1 = this.posZ - this.entity.posZ;
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;
            if (d3 < 2.500000277905201E-7) {
                this.entity.func_191989_p(0.0f);
                return;
            }
            float f9 = (float)(MathHelper.atan2(d1, d0) * 57.29577951308232) - 90.0f;
            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f9, 90.0f);
            this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
            if (d2 > (double)this.entity.stepHeight && d0 * d0 + d1 * d1 < (double)Math.max(1.0f, this.entity.width)) {
                this.entity.getJumpHelper().setJumping();
                this.action = Action.JUMPING;
            }
        } else if (this.action == Action.JUMPING) {
            this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
            if (this.entity.onGround) {
                this.action = Action.WAIT;
            }
        } else {
            this.entity.func_191989_p(0.0f);
        }
    }

    protected float limitAngle(float p_75639_1_, float p_75639_2_, float p_75639_3_) {
        float f1;
        float f = MathHelper.wrapDegrees(p_75639_2_ - p_75639_1_);
        if (f > p_75639_3_) {
            f = p_75639_3_;
        }
        if (f < -p_75639_3_) {
            f = -p_75639_3_;
        }
        if ((f1 = p_75639_1_ + f) < 0.0f) {
            f1 += 360.0f;
        } else if (f1 > 360.0f) {
            f1 -= 360.0f;
        }
        return f1;
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

    public static enum Action {
        WAIT,
        MOVE_TO,
        STRAFE,
        JUMPING;

    }
}

