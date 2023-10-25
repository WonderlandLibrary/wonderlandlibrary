/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityCreature
extends EntityLiving {
    public static final UUID FLEEING_SPEED_MODIFIER_UUID = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
    public static final AttributeModifier FLEEING_SPEED_MODIFIER = new AttributeModifier(FLEEING_SPEED_MODIFIER_UUID, "Fleeing speed bonus", 2.0, 2).setSaved(false);
    private BlockPos homePosition = BlockPos.ORIGIN;
    private float maximumHomeDistance = -1.0f;
    private final float restoreWaterCost = PathNodeType.WATER.getPriority();

    public EntityCreature(World worldIn) {
        super(worldIn);
    }

    public float getBlockPathWeight(BlockPos pos) {
        return 0.0f;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean getCanSpawnHere() {
        if (!super.getCanSpawnHere()) return false;
        BlockPos blockPos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (!(this.getBlockPathWeight(blockPos) >= 0.0f)) return false;
        return true;
    }

    public boolean hasPath() {
        return !this.navigator.noPath();
    }

    public boolean isWithinHomeDistanceCurrentPosition() {
        return this.isWithinHomeDistanceFromPosition(new BlockPos(this));
    }

    public boolean isWithinHomeDistanceFromPosition(BlockPos pos) {
        if (this.maximumHomeDistance == -1.0f) {
            return true;
        }
        return this.homePosition.distanceSq(pos) < (double)(this.maximumHomeDistance * this.maximumHomeDistance);
    }

    public void setHomePosAndDistance(BlockPos pos, int distance) {
        this.homePosition = pos;
        this.maximumHomeDistance = distance;
    }

    public BlockPos getHomePosition() {
        return this.homePosition;
    }

    public float getMaximumHomeDistance() {
        return this.maximumHomeDistance;
    }

    public void detachHome() {
        this.maximumHomeDistance = -1.0f;
    }

    public boolean hasHome() {
        return this.maximumHomeDistance != -1.0f;
    }

    @Override
    protected void updateLeashedState() {
        super.updateLeashedState();
        if (this.getLeashed() && this.getLeashedToEntity() != null && this.getLeashedToEntity().world == this.world) {
            Entity entity = this.getLeashedToEntity();
            this.setHomePosAndDistance(new BlockPos((int)entity.posX, (int)entity.posY, (int)entity.posZ), 5);
            float f = this.getDistanceToEntity(entity);
            if (this instanceof EntityTameable && ((EntityTameable)this).isSitting()) {
                if (f > 10.0f) {
                    this.clearLeashed(true, true);
                }
                return;
            }
            this.onLeashDistance(f);
            if (f > 10.0f) {
                this.clearLeashed(true, true);
                this.tasks.disableControlFlag(1);
            } else if (f > 6.0f) {
                double d0 = (entity.posX - this.posX) / (double)f;
                double d1 = (entity.posY - this.posY) / (double)f;
                double d2 = (entity.posZ - this.posZ) / (double)f;
                this.motionX += d0 * Math.abs(d0) * 0.4;
                this.motionY += d1 * Math.abs(d1) * 0.4;
                this.motionZ += d2 * Math.abs(d2) * 0.4;
            } else {
                this.tasks.enableControlFlag(1);
                float f1 = 2.0f;
                Vec3d vec3d = new Vec3d(entity.posX - this.posX, entity.posY - this.posY, entity.posZ - this.posZ).normalize().scale(Math.max(f - 2.0f, 0.0f));
                this.getNavigator().tryMoveToXYZ(this.posX + vec3d.x, this.posY + vec3d.y, this.posZ + vec3d.z, this.func_190634_dg());
            }
        }
    }

    protected double func_190634_dg() {
        return 1.0;
    }

    protected void onLeashDistance(float p_142017_1_) {
    }
}

