/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILandOnOwnersShoulder
extends EntityAIBase {
    private final EntityShoulderRiding field_192382_a;
    private EntityPlayer field_192383_b;
    private boolean field_192384_c;

    public EntityAILandOnOwnersShoulder(EntityShoulderRiding p_i47415_1_) {
        this.field_192382_a = p_i47415_1_;
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.field_192382_a.getOwner();
        boolean flag = entitylivingbase != null && !((EntityPlayer)entitylivingbase).isSpectator() && !((EntityPlayer)entitylivingbase).capabilities.isFlying && !entitylivingbase.isInWater();
        return !this.field_192382_a.isSitting() && flag && this.field_192382_a.func_191995_du();
    }

    @Override
    public boolean isInterruptible() {
        return !this.field_192384_c;
    }

    @Override
    public void startExecuting() {
        this.field_192383_b = (EntityPlayer)this.field_192382_a.getOwner();
        this.field_192384_c = false;
    }

    @Override
    public void updateTask() {
        if (!this.field_192384_c && !this.field_192382_a.isSitting() && !this.field_192382_a.getLeashed() && this.field_192382_a.getEntityBoundingBox().intersectsWith(this.field_192383_b.getEntityBoundingBox())) {
            this.field_192384_c = this.field_192382_a.func_191994_f(this.field_192383_b);
        }
    }
}

