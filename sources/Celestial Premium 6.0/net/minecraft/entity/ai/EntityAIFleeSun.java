/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAIFleeSun
extends EntityAIBase {
    private final EntityCreature theCreature;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
    private final double movementSpeed;
    private final World theWorld;

    public EntityAIFleeSun(EntityCreature theCreatureIn, double movementSpeedIn) {
        this.theCreature = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.theWorld = theCreatureIn.world;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.theWorld.isDaytime()) {
            return false;
        }
        if (!this.theCreature.isBurning()) {
            return false;
        }
        if (!this.theWorld.canSeeSky(new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ))) {
            return false;
        }
        if (!this.theCreature.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            return false;
        }
        Vec3d vec3d = this.findPossibleShelter();
        if (vec3d == null) {
            return false;
        }
        this.shelterX = vec3d.x;
        this.shelterY = vec3d.y;
        this.shelterZ = vec3d.z;
        return true;
    }

    @Override
    public boolean continueExecuting() {
        return !this.theCreature.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.theCreature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    @Nullable
    private Vec3d findPossibleShelter() {
        Random random = this.theCreature.getRNG();
        BlockPos blockpos = new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ);
        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (this.theWorld.canSeeSky(blockpos1) || !(this.theCreature.getBlockPathWeight(blockpos1) < 0.0f)) continue;
            return new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
        }
        return null;
    }
}

