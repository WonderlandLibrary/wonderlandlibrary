// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import java.util.Random;
import net.minecraft.util.math.Vec3d;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.EntityCreature;

public class EntityAIFleeSun extends EntityAIBase
{
    private final EntityCreature creature;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
    private final double movementSpeed;
    private final World world;
    
    public EntityAIFleeSun(final EntityCreature theCreatureIn, final double movementSpeedIn) {
        this.creature = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.world = theCreatureIn.world;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.world.isDaytime()) {
            return false;
        }
        if (!this.creature.isBurning()) {
            return false;
        }
        if (!this.world.canSeeSky(new BlockPos(this.creature.posX, this.creature.getEntityBoundingBox().minY, this.creature.posZ))) {
            return false;
        }
        if (!this.creature.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            return false;
        }
        final Vec3d vec3d = this.findPossibleShelter();
        if (vec3d == null) {
            return false;
        }
        this.shelterX = vec3d.x;
        this.shelterY = vec3d.y;
        this.shelterZ = vec3d.z;
        return true;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath();
    }
    
    @Override
    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }
    
    @Nullable
    private Vec3d findPossibleShelter() {
        final Random random = this.creature.getRNG();
        final BlockPos blockpos = new BlockPos(this.creature.posX, this.creature.getEntityBoundingBox().minY, this.creature.posZ);
        for (int i = 0; i < 10; ++i) {
            final BlockPos blockpos2 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (!this.world.canSeeSky(blockpos2) && this.creature.getBlockPathWeight(blockpos2) < 0.0f) {
                return new Vec3d(blockpos2.getX(), blockpos2.getY(), blockpos2.getZ());
            }
        }
        return null;
    }
}
