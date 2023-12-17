// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityCreature;

public class EntityAIMoveTowardsRestriction extends EntityAIBase
{
    private final EntityCreature creature;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private final double movementSpeed;
    
    public EntityAIMoveTowardsRestriction(final EntityCreature creatureIn, final double speedIn) {
        this.creature = creatureIn;
        this.movementSpeed = speedIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.creature.isWithinHomeDistanceCurrentPosition()) {
            return false;
        }
        final BlockPos blockpos = this.creature.getHomePosition();
        final Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.creature, 16, 7, new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ()));
        if (vec3d == null) {
            return false;
        }
        this.movePosX = vec3d.x;
        this.movePosY = vec3d.y;
        this.movePosZ = vec3d.z;
        return true;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath();
    }
    
    @Override
    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
    }
}
