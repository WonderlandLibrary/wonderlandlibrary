/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.monster;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public abstract class EntityMob
extends EntityCreature
implements IMob {
    public EntityMob(World worldIn) {
        super(worldIn);
        this.experienceValue = 5;
    }

    @Override
    public void onLivingUpdate() {
        this.updateArmSwingProgress();
        float f2 = this.getBrightness(1.0f);
        if (f2 > 0.5f) {
            this.entityAge += 2;
        }
        super.onLivingUpdate();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }

    @Override
    protected String getSwimSound() {
        return "game.hostile.swim";
    }

    @Override
    protected String getSplashSound() {
        return "game.hostile.swim.splash";
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (super.attackEntityFrom(source, amount)) {
            Entity entity = source.getEntity();
            return this.riddenByEntity != entity && this.ridingEntity != entity ? true : true;
        }
        return false;
    }

    @Override
    protected String getHurtSound() {
        return "game.hostile.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "game.hostile.die";
    }

    @Override
    protected String getFallSoundString(int damageValue) {
        return damageValue > 4 ? "game.hostile.hurt.fall.big" : "game.hostile.hurt.fall.small";
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag;
        float f2 = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int i2 = 0;
        if (entityIn instanceof EntityLivingBase) {
            f2 += EnchantmentHelper.func_152377_a(this.getHeldItem(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i2 += EnchantmentHelper.getKnockbackModifier(this);
        }
        if (flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f2)) {
            int j2;
            if (i2 > 0) {
                entityIn.addVelocity(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0f) * (float)i2 * 0.5f, 0.1, MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f) * (float)i2 * 0.5f);
                this.motionX *= 0.6;
                this.motionZ *= 0.6;
            }
            if ((j2 = EnchantmentHelper.getFireAspectModifier(this)) > 0) {
                entityIn.setFire(j2 * 4);
            }
            this.applyEnchantments(this, entityIn);
        }
        return flag;
    }

    @Override
    public float getBlockPathWeight(BlockPos pos) {
        return 0.5f - this.worldObj.getLightBrightness(pos);
    }

    protected boolean isValidLightLevel() {
        BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (this.worldObj.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32)) {
            return false;
        }
        int i2 = this.worldObj.getLightFromNeighbors(blockpos);
        if (this.worldObj.isThundering()) {
            int j2 = this.worldObj.getSkylightSubtracted();
            this.worldObj.setSkylightSubtracted(10);
            i2 = this.worldObj.getLightFromNeighbors(blockpos);
            this.worldObj.setSkylightSubtracted(j2);
        }
        return i2 <= this.rand.nextInt(8);
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }
}

