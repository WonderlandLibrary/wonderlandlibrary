// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.util.Vec3;
import net.minecraft.entity.EntityCreature;

public class EntityAIPanic extends EntityAIBase
{
    private EntityCreature theEntityCreature;
    protected double speed;
    private double randPosX;
    private double randPosY;
    private double randPosZ;
    private static final String __OBFID = "CL_00001604";
    
    public EntityAIPanic(final EntityCreature p_i1645_1_, final double p_i1645_2_) {
        this.theEntityCreature = p_i1645_1_;
        this.speed = p_i1645_2_;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.theEntityCreature.getAITarget() == null && !this.theEntityCreature.isBurning()) {
            return false;
        }
        final Vec3 var1 = RandomPositionGenerator.findRandomTarget(this.theEntityCreature, 5, 4);
        if (var1 == null) {
            return false;
        }
        this.randPosX = var1.xCoord;
        this.randPosY = var1.yCoord;
        this.randPosZ = var1.zCoord;
        return true;
    }
    
    @Override
    public void startExecuting() {
        this.theEntityCreature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.theEntityCreature.getNavigator().noPath();
    }
}
