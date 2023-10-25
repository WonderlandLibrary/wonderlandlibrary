/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class EntityCreature
extends EntityLiving {
    private float maximumHomeDistance = -1.0f;
    private boolean isMovementAITaskSet;
    public static final AttributeModifier FLEEING_SPEED_MODIFIER;
    public static final UUID FLEEING_SPEED_MODIFIER_UUID;
    private BlockPos homePosition = BlockPos.ORIGIN;
    private EntityAIBase aiBase = new EntityAIMoveTowardsRestriction(this, 1.0);

    public boolean isWithinHomeDistanceFromPosition(BlockPos blockPos) {
        return this.maximumHomeDistance == -1.0f ? true : this.homePosition.distanceSq(blockPos) < (double)(this.maximumHomeDistance * this.maximumHomeDistance);
    }

    static {
        FLEEING_SPEED_MODIFIER_UUID = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
        FLEEING_SPEED_MODIFIER = new AttributeModifier(FLEEING_SPEED_MODIFIER_UUID, "Fleeing speed bonus", 2.0, 2).setSaved(false);
    }

    public boolean isWithinHomeDistanceCurrentPosition() {
        return this.isWithinHomeDistanceFromPosition(new BlockPos(this));
    }

    protected void func_142017_o(float f) {
    }

    @Override
    public boolean getCanSpawnHere() {
        if (super.getCanSpawnHere()) {
            BlockPos blockPos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
            if (this.getBlockPathWeight(blockPos) >= 0.0f) {
                return true;
            }
        }
        return false;
    }

    public boolean hasHome() {
        return this.maximumHomeDistance != -1.0f;
    }

    public void detachHome() {
        this.maximumHomeDistance = -1.0f;
    }

    public float getMaximumHomeDistance() {
        return this.maximumHomeDistance;
    }

    public EntityCreature(World world) {
        super(world);
    }

    public BlockPos getHomePosition() {
        return this.homePosition;
    }

    public boolean hasPath() {
        return !this.navigator.noPath();
    }

    public float getBlockPathWeight(BlockPos blockPos) {
        return 0.0f;
    }

    public void setHomePosAndDistance(BlockPos blockPos, int n) {
        this.homePosition = blockPos;
        this.maximumHomeDistance = n;
    }

    @Override
    protected void updateLeashedState() {
        super.updateLeashedState();
        if (this.getLeashed() && this.getLeashedToEntity() != null && this.getLeashedToEntity().worldObj == this.worldObj) {
            Entity entity = this.getLeashedToEntity();
            this.setHomePosAndDistance(new BlockPos((int)entity.posX, (int)entity.posY, (int)entity.posZ), 5);
            float f = this.getDistanceToEntity(entity);
            if (this instanceof EntityTameable && ((EntityTameable)this).isSitting()) {
                if (f > 10.0f) {
                    this.clearLeashed(true, true);
                }
                return;
            }
            if (!this.isMovementAITaskSet) {
                this.tasks.addTask(2, this.aiBase);
                if (this.getNavigator() instanceof PathNavigateGround) {
                    ((PathNavigateGround)this.getNavigator()).setAvoidsWater(false);
                }
                this.isMovementAITaskSet = true;
            }
            this.func_142017_o(f);
            if (f > 4.0f) {
                this.getNavigator().tryMoveToEntityLiving(entity, 1.0);
            }
            if (f > 6.0f) {
                double d = (entity.posX - this.posX) / (double)f;
                double d2 = (entity.posY - this.posY) / (double)f;
                double d3 = (entity.posZ - this.posZ) / (double)f;
                this.motionX += d * Math.abs(d) * 0.4;
                this.motionY += d2 * Math.abs(d2) * 0.4;
                this.motionZ += d3 * Math.abs(d3) * 0.4;
            }
            if (f > 10.0f) {
                this.clearLeashed(true, true);
            }
        } else if (!this.getLeashed() && this.isMovementAITaskSet) {
            this.isMovementAITaskSet = false;
            this.tasks.removeTask(this.aiBase);
            if (this.getNavigator() instanceof PathNavigateGround) {
                ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
            }
            this.detachHome();
        }
    }
}

