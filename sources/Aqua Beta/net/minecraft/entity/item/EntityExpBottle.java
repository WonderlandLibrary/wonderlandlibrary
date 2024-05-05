package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityExpBottle
extends EntityThrowable {
    public EntityExpBottle(World worldIn) {
        super(worldIn);
    }

    public EntityExpBottle(World worldIn, EntityLivingBase p_i1786_2_) {
        super(worldIn, p_i1786_2_);
    }

    public EntityExpBottle(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    protected float getGravityVelocity() {
        return 0.07f;
    }

    protected float getVelocity() {
        return 0.7f;
    }

    protected float getInaccuracy() {
        return -20.0f;
    }

    protected void onImpact(MovingObjectPosition p_70184_1_) {
        if (!this.worldObj.isRemote) {
            int j;
            this.worldObj.playAuxSFX(2002, new BlockPos((Entity)this), 0);
            for (int i = 3 + this.worldObj.rand.nextInt(5) + this.worldObj.rand.nextInt(5); i > 0; i -= j) {
                j = EntityXPOrb.getXPSplit((int)i);
                this.worldObj.spawnEntityInWorld((Entity)new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
            }
            this.setDead();
        }
    }
}
