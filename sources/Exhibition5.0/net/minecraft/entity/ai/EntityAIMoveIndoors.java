// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.entity.EntityCreature;

public class EntityAIMoveIndoors extends EntityAIBase
{
    private EntityCreature entityObj;
    private VillageDoorInfo doorInfo;
    private int insidePosX;
    private int insidePosZ;
    private static final String __OBFID = "CL_00001596";
    
    public EntityAIMoveIndoors(final EntityCreature p_i1637_1_) {
        this.insidePosX = -1;
        this.insidePosZ = -1;
        this.entityObj = p_i1637_1_;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        final BlockPos var1 = new BlockPos(this.entityObj);
        if ((this.entityObj.worldObj.isDaytime() && (!this.entityObj.worldObj.isRaining() || this.entityObj.worldObj.getBiomeGenForCoords(var1).canSpawnLightningBolt())) || this.entityObj.worldObj.provider.getHasNoSky()) {
            return false;
        }
        if (this.entityObj.getRNG().nextInt(50) != 0) {
            return false;
        }
        if (this.insidePosX != -1 && this.entityObj.getDistanceSq(this.insidePosX, this.entityObj.posY, this.insidePosZ) < 4.0) {
            return false;
        }
        final Village var2 = this.entityObj.worldObj.getVillageCollection().func_176056_a(var1, 14);
        if (var2 == null) {
            return false;
        }
        this.doorInfo = var2.func_179863_c(var1);
        return this.doorInfo != null;
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.entityObj.getNavigator().noPath();
    }
    
    @Override
    public void startExecuting() {
        this.insidePosX = -1;
        final BlockPos var1 = this.doorInfo.func_179856_e();
        final int var2 = var1.getX();
        final int var3 = var1.getY();
        final int var4 = var1.getZ();
        if (this.entityObj.getDistanceSq(var1) > 256.0) {
            final Vec3 var5 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3, new Vec3(var2 + 0.5, var3, var4 + 0.5));
            if (var5 != null) {
                this.entityObj.getNavigator().tryMoveToXYZ(var5.xCoord, var5.yCoord, var5.zCoord, 1.0);
            }
        }
        else {
            this.entityObj.getNavigator().tryMoveToXYZ(var2 + 0.5, var3, var4 + 0.5, 1.0);
        }
    }
    
    @Override
    public void resetTask() {
        this.insidePosX = this.doorInfo.func_179856_e().getX();
        this.insidePosZ = this.doorInfo.func_179856_e().getZ();
        this.doorInfo = null;
    }
}
