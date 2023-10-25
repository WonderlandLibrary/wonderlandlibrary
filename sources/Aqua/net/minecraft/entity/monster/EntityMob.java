package net.minecraft.entity.monster;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.ItemStack;
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

    public void onLivingUpdate() {
        this.updateArmSwingProgress();
        float f = this.getBrightness(1.0f);
        if (f > 0.5f) {
            this.entityAge += 2;
        }
        super.onLivingUpdate();
    }

    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }

    protected String getSwimSound() {
        return "game.hostile.swim";
    }

    protected String getSplashSound() {
        return "game.hostile.swim.splash";
    }

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

    protected String getHurtSound() {
        return "game.hostile.hurt";
    }

    protected String getDeathSound() {
        return "game.hostile.die";
    }

    protected String getFallSoundString(int damageValue) {
        return damageValue > 4 ? "game.hostile.hurt.fall.big" : "game.hostile.hurt.fall.small";
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag;
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int i = 0;
        if (entityIn instanceof EntityLivingBase) {
            f += EnchantmentHelper.getModifierForCreature((ItemStack)this.getHeldItem(), (EnumCreatureAttribute)((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier((EntityLivingBase)this);
        }
        if (flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), f)) {
            int j;
            if (i > 0) {
                entityIn.addVelocity((double)(-MathHelper.sin((float)(this.rotationYaw * (float)Math.PI / 180.0f)) * (float)i * 0.5f), 0.1, (double)(MathHelper.cos((float)(this.rotationYaw * (float)Math.PI / 180.0f)) * (float)i * 0.5f));
                this.motionX *= 0.6;
                this.motionZ *= 0.6;
            }
            if ((j = EnchantmentHelper.getFireAspectModifier((EntityLivingBase)this)) > 0) {
                entityIn.setFire(j * 4);
            }
            this.applyEnchantments((EntityLivingBase)this, entityIn);
        }
        return flag;
    }

    public float getBlockPathWeight(BlockPos pos) {
        return 0.5f - this.worldObj.getLightBrightness(pos);
    }

    protected boolean isValidLightLevel() {
        BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (this.worldObj.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32)) {
            return false;
        }
        int i = this.worldObj.getLightFromNeighbors(blockpos);
        if (this.worldObj.isThundering()) {
            int j = this.worldObj.getSkylightSubtracted();
            this.worldObj.setSkylightSubtracted(10);
            i = this.worldObj.getLightFromNeighbors(blockpos);
            this.worldObj.setSkylightSubtracted(j);
        }
        return i <= this.rand.nextInt(8);
    }

    public boolean getCanSpawnHere() {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
    }

    protected boolean canDropLoot() {
        return true;
    }
}
