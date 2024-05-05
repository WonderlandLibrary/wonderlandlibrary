package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

static class EntityGhast.GhastMoveHelper
extends EntityMoveHelper {
    private EntityGhast parentEntity;
    private int courseChangeCooldown;

    public EntityGhast.GhastMoveHelper(EntityGhast ghast) {
        super((EntityLiving)ghast);
        this.parentEntity = ghast;
    }

    public void onUpdateMoveHelper() {
        if (this.update) {
            double d0 = this.posX - this.parentEntity.posX;
            double d1 = this.posY - this.parentEntity.posY;
            double d2 = this.posZ - this.parentEntity.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (this.courseChangeCooldown-- <= 0) {
                this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                if (this.isNotColliding(this.posX, this.posY, this.posZ, d3 = (double)MathHelper.sqrt_double((double)d3))) {
                    this.parentEntity.motionX += d0 / d3 * 0.1;
                    this.parentEntity.motionY += d1 / d3 * 0.1;
                    this.parentEntity.motionZ += d2 / d3 * 0.1;
                } else {
                    this.update = false;
                }
            }
        }
    }

    private boolean isNotColliding(double x, double y, double z, double p_179926_7_) {
        double d0 = (x - this.parentEntity.posX) / p_179926_7_;
        double d1 = (y - this.parentEntity.posY) / p_179926_7_;
        double d2 = (z - this.parentEntity.posZ) / p_179926_7_;
        AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();
        int i = 1;
        while ((double)i < p_179926_7_) {
            if (!this.parentEntity.worldObj.getCollidingBoundingBoxes((Entity)this.parentEntity, axisalignedbb = axisalignedbb.offset(d0, d1, d2)).isEmpty()) {
                return false;
            }
            ++i;
        }
        return true;
    }
}
