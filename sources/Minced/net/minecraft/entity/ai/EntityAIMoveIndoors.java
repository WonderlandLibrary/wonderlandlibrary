// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Village;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.entity.EntityCreature;

public class EntityAIMoveIndoors extends EntityAIBase
{
    private final EntityCreature entity;
    private VillageDoorInfo doorInfo;
    private int insidePosX;
    private int insidePosZ;
    
    public EntityAIMoveIndoors(final EntityCreature entityIn) {
        this.insidePosX = -1;
        this.insidePosZ = -1;
        this.entity = entityIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        final BlockPos blockpos = new BlockPos(this.entity);
        if ((this.entity.world.isDaytime() && (!this.entity.world.isRaining() || this.entity.world.getBiome(blockpos).canRain())) || !this.entity.world.provider.hasSkyLight()) {
            return false;
        }
        if (this.entity.getRNG().nextInt(50) != 0) {
            return false;
        }
        if (this.insidePosX != -1 && this.entity.getDistanceSq(this.insidePosX, this.entity.posY, this.insidePosZ) < 4.0) {
            return false;
        }
        final Village village = this.entity.world.getVillageCollection().getNearestVillage(blockpos, 14);
        if (village == null) {
            return false;
        }
        this.doorInfo = village.getDoorInfo(blockpos);
        return this.doorInfo != null;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return !this.entity.getNavigator().noPath();
    }
    
    @Override
    public void startExecuting() {
        this.insidePosX = -1;
        final BlockPos blockpos = this.doorInfo.getInsideBlockPos();
        final int i = blockpos.getX();
        final int j = blockpos.getY();
        final int k = blockpos.getZ();
        if (this.entity.getDistanceSq(blockpos) > 256.0) {
            final Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, 14, 3, new Vec3d(i + 0.5, j, k + 0.5));
            if (vec3d != null) {
                this.entity.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0);
            }
        }
        else {
            this.entity.getNavigator().tryMoveToXYZ(i + 0.5, j, k + 0.5, 1.0);
        }
    }
    
    @Override
    public void resetTask() {
        this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
        this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
        this.doorInfo = null;
    }
}
