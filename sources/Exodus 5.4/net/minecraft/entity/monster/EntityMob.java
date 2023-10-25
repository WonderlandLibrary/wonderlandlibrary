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
    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        if (super.attackEntityFrom(damageSource, f)) {
            Entity entity = damageSource.getEntity();
            return this.riddenByEntity != entity && this.ridingEntity != entity ? true : true;
        }
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
    }

    @Override
    protected String getHurtSound() {
        return "game.hostile.hurt";
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        boolean bl;
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int n = 0;
        if (entity instanceof EntityLivingBase) {
            f += EnchantmentHelper.func_152377_a(this.getHeldItem(), ((EntityLivingBase)entity).getCreatureAttribute());
            n += EnchantmentHelper.getKnockbackModifier(this);
        }
        if (bl = entity.attackEntityFrom(DamageSource.causeMobDamage(this), f)) {
            int n2;
            if (n > 0) {
                entity.addVelocity(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0f) * (float)n * 0.5f, 0.1, MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f) * (float)n * 0.5f);
                this.motionX *= 0.6;
                this.motionZ *= 0.6;
            }
            if ((n2 = EnchantmentHelper.getFireAspectModifier(this)) > 0) {
                entity.setFire(n2 * 4);
            }
            this.applyEnchantments(this, entity);
        }
        return bl;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    protected String getSwimSound() {
        return "game.hostile.swim";
    }

    @Override
    public void onLivingUpdate() {
        this.updateArmSwingProgress();
        float f = this.getBrightness(1.0f);
        if (f > 0.5f) {
            this.entityAge += 2;
        }
        super.onLivingUpdate();
    }

    @Override
    public float getBlockPathWeight(BlockPos blockPos) {
        return 0.5f - this.worldObj.getLightBrightness(blockPos);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
    }

    @Override
    protected String getFallSoundString(int n) {
        return n > 4 ? "game.hostile.hurt.fall.big" : "game.hostile.hurt.fall.small";
    }

    public EntityMob(World world) {
        super(world);
        this.experienceValue = 5;
    }

    protected boolean isValidLightLevel() {
        BlockPos blockPos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (this.worldObj.getLightFor(EnumSkyBlock.SKY, blockPos) > this.rand.nextInt(32)) {
            return false;
        }
        int n = this.worldObj.getLightFromNeighbors(blockPos);
        if (this.worldObj.isThundering()) {
            int n2 = this.worldObj.getSkylightSubtracted();
            this.worldObj.setSkylightSubtracted(10);
            n = this.worldObj.getLightFromNeighbors(blockPos);
            this.worldObj.setSkylightSubtracted(n2);
        }
        return n <= this.rand.nextInt(8);
    }

    @Override
    protected String getSplashSound() {
        return "game.hostile.swim.splash";
    }

    @Override
    protected String getDeathSound() {
        return "game.hostile.die";
    }
}

