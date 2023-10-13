/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class EntityAIRunAroundLikeCrazy
extends EntityAIBase {
    private EntityHorse horseHost;
    private double field_111178_b;
    private double field_111179_c;
    private double field_111176_d;
    private double field_111177_e;
    private static final String __OBFID = "CL_00001612";

    public EntityAIRunAroundLikeCrazy(EntityHorse p_i1653_1_, double p_i1653_2_) {
        this.horseHost = p_i1653_1_;
        this.field_111178_b = p_i1653_2_;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.horseHost.isTame() && this.horseHost.riddenByEntity != null) {
            Vec3 var1 = RandomPositionGenerator.findRandomTarget(this.horseHost, 5, 4);
            if (var1 == null) {
                return false;
            }
            this.field_111179_c = var1.xCoord;
            this.field_111176_d = var1.yCoord;
            this.field_111177_e = var1.zCoord;
            return true;
        }
        return false;
    }

    @Override
    public void startExecuting() {
        this.horseHost.getNavigator().tryMoveToXYZ(this.field_111179_c, this.field_111176_d, this.field_111177_e, this.field_111178_b);
    }

    @Override
    public boolean continueExecuting() {
        return !this.horseHost.getNavigator().noPath() && this.horseHost.riddenByEntity != null;
    }

    @Override
    public void updateTask() {
        if (this.horseHost.getRNG().nextInt(50) == 0) {
            if (this.horseHost.riddenByEntity instanceof EntityPlayer) {
                int var1 = this.horseHost.getTemper();
                int var2 = this.horseHost.getMaxTemper();
                if (var2 > 0 && this.horseHost.getRNG().nextInt(var2) < var1) {
                    this.horseHost.setTamedBy((EntityPlayer)this.horseHost.riddenByEntity);
                    this.horseHost.worldObj.setEntityState(this.horseHost, (byte)7);
                    return;
                }
                this.horseHost.increaseTemper(5);
            }
            this.horseHost.riddenByEntity.mountEntity(null);
            this.horseHost.riddenByEntity = null;
            this.horseHost.makeHorseRearWithSound();
            this.horseHost.worldObj.setEntityState(this.horseHost, (byte)6);
        }
    }
}

