package net.minecraft.entity.monster;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.MathHelper;

/*
 * Exception performing whole class analysis ignored.
 */
static class EntityGuardian.GuardianMoveHelper
extends EntityMoveHelper {
    private EntityGuardian entityGuardian;

    public EntityGuardian.GuardianMoveHelper(EntityGuardian guardian) {
        super((EntityLiving)guardian);
        this.entityGuardian = guardian;
    }

    public void onUpdateMoveHelper() {
        if (this.update && !this.entityGuardian.getNavigator().noPath()) {
            double d0 = this.posX - this.entityGuardian.posX;
            double d1 = this.posY - this.entityGuardian.posY;
            double d2 = this.posZ - this.entityGuardian.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            d3 = MathHelper.sqrt_double((double)d3);
            d1 /= d3;
            float f = (float)(MathHelper.atan2((double)d2, (double)d0) * 180.0 / Math.PI) - 90.0f;
            this.entityGuardian.renderYawOffset = this.entityGuardian.rotationYaw = this.limitAngle(this.entityGuardian.rotationYaw, f, 30.0f);
            float f1 = (float)(this.speed * this.entityGuardian.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
            this.entityGuardian.setAIMoveSpeed(this.entityGuardian.getAIMoveSpeed() + (f1 - this.entityGuardian.getAIMoveSpeed()) * 0.125f);
            double d4 = Math.sin((double)((double)(this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.5)) * 0.05;
            double d5 = Math.cos((double)(this.entityGuardian.rotationYaw * (float)Math.PI / 180.0f));
            double d6 = Math.sin((double)(this.entityGuardian.rotationYaw * (float)Math.PI / 180.0f));
            this.entityGuardian.motionX += d4 * d5;
            this.entityGuardian.motionZ += d4 * d6;
            d4 = Math.sin((double)((double)(this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.75)) * 0.05;
            this.entityGuardian.motionY += d4 * (d6 + d5) * 0.25;
            this.entityGuardian.motionY += (double)this.entityGuardian.getAIMoveSpeed() * d1 * 0.1;
            EntityLookHelper entitylookhelper = this.entityGuardian.getLookHelper();
            double d7 = this.entityGuardian.posX + d0 / d3 * 2.0;
            double d8 = (double)this.entityGuardian.getEyeHeight() + this.entityGuardian.posY + d1 / d3 * 1.0;
            double d9 = this.entityGuardian.posZ + d2 / d3 * 2.0;
            double d10 = entitylookhelper.getLookPosX();
            double d11 = entitylookhelper.getLookPosY();
            double d12 = entitylookhelper.getLookPosZ();
            if (!entitylookhelper.getIsLooking()) {
                d10 = d7;
                d11 = d8;
                d12 = d9;
            }
            this.entityGuardian.getLookHelper().setLookPosition(d10 + (d7 - d10) * 0.125, d11 + (d8 - d11) * 0.125, d12 + (d9 - d12) * 0.125, 10.0f, 40.0f);
            EntityGuardian.access$200((EntityGuardian)this.entityGuardian, (boolean)true);
        } else {
            this.entityGuardian.setAIMoveSpeed(0.0f);
            EntityGuardian.access$200((EntityGuardian)this.entityGuardian, (boolean)false);
        }
    }
}
