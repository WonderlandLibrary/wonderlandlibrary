// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.Vec3i;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;

public abstract class EntityCreature extends EntityLiving
{
    public static final UUID FLEEING_SPEED_MODIFIER_UUID;
    public static final AttributeModifier FLEEING_SPEED_MODIFIER;
    private BlockPos homePosition;
    private float maximumHomeDistance;
    private final float restoreWaterCost;
    
    public EntityCreature(final World worldIn) {
        super(worldIn);
        this.homePosition = BlockPos.ORIGIN;
        this.maximumHomeDistance = -1.0f;
        this.restoreWaterCost = PathNodeType.WATER.getPriority();
    }
    
    public float getBlockPathWeight(final BlockPos pos) {
        return 0.0f;
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && this.getBlockPathWeight(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) >= 0.0f;
    }
    
    public boolean hasPath() {
        return !this.navigator.noPath();
    }
    
    public boolean isWithinHomeDistanceCurrentPosition() {
        return this.isWithinHomeDistanceFromPosition(new BlockPos(this));
    }
    
    public boolean isWithinHomeDistanceFromPosition(final BlockPos pos) {
        return this.maximumHomeDistance == -1.0f || this.homePosition.distanceSq(pos) < this.maximumHomeDistance * this.maximumHomeDistance;
    }
    
    public void setHomePosAndDistance(final BlockPos pos, final int distance) {
        this.homePosition = pos;
        this.maximumHomeDistance = (float)distance;
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
        if (this.getLeashed() && this.getLeashHolder() != null && this.getLeashHolder().world == this.world) {
            final Entity entity = this.getLeashHolder();
            this.setHomePosAndDistance(new BlockPos((int)entity.posX, (int)entity.posY, (int)entity.posZ), 5);
            final float f = this.getDistance(entity);
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
            }
            else if (f > 6.0f) {
                final double d0 = (entity.posX - this.posX) / f;
                final double d2 = (entity.posY - this.posY) / f;
                final double d3 = (entity.posZ - this.posZ) / f;
                this.motionX += d0 * Math.abs(d0) * 0.4;
                this.motionY += d2 * Math.abs(d2) * 0.4;
                this.motionZ += d3 * Math.abs(d3) * 0.4;
            }
            else {
                this.tasks.enableControlFlag(1);
                final float f2 = 2.0f;
                final Vec3d vec3d = new Vec3d(entity.posX - this.posX, entity.posY - this.posY, entity.posZ - this.posZ).normalize().scale(Math.max(f - 2.0f, 0.0f));
                this.getNavigator().tryMoveToXYZ(this.posX + vec3d.x, this.posY + vec3d.y, this.posZ + vec3d.z, this.followLeashSpeed());
            }
        }
    }
    
    protected double followLeashSpeed() {
        return 1.0;
    }
    
    protected void onLeashDistance(final float p_142017_1_) {
    }
    
    static {
        FLEEING_SPEED_MODIFIER_UUID = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
        FLEEING_SPEED_MODIFIER = new AttributeModifier(EntityCreature.FLEEING_SPEED_MODIFIER_UUID, "Fleeing speed bonus", 2.0, 2).setSaved(false);
    }
}
