// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.world.EnumSkyBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.entity.EntityCreature;

public abstract class EntityMob extends EntityCreature implements IMob
{
    public EntityMob(final World worldIn) {
        super(worldIn);
        this.experienceValue = 5;
    }
    
    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }
    
    @Override
    public void onLivingUpdate() {
        this.updateArmSwingProgress();
        final float f = this.getBrightness();
        if (f > 0.5f) {
            this.idleTime += 2;
        }
        super.onLivingUpdate();
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }
    
    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_HOSTILE_SWIM;
    }
    
    @Override
    protected SoundEvent getSplashSound() {
        return SoundEvents.ENTITY_HOSTILE_SPLASH;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        return !this.isEntityInvulnerable(source) && super.attackEntityFrom(source, amount);
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_HOSTILE_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_HOSTILE_DEATH;
    }
    
    @Override
    protected SoundEvent getFallSound(final int heightIn) {
        return (heightIn > 4) ? SoundEvents.ENTITY_HOSTILE_BIG_FALL : SoundEvents.ENTITY_HOSTILE_SMALL_FALL;
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;
        if (entityIn instanceof EntityLivingBase) {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }
        final boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        if (flag) {
            if (i > 0 && entityIn instanceof EntityLivingBase) {
                ((EntityLivingBase)entityIn).knockBack(this, i * 0.5f, MathHelper.sin(this.rotationYaw * 0.017453292f), -MathHelper.cos(this.rotationYaw * 0.017453292f));
                this.motionX *= 0.6;
                this.motionZ *= 0.6;
            }
            final int j = EnchantmentHelper.getFireAspectModifier(this);
            if (j > 0) {
                entityIn.setFire(j * 4);
            }
            if (entityIn instanceof EntityPlayer) {
                final EntityPlayer entityplayer = (EntityPlayer)entityIn;
                final ItemStack itemstack = this.getHeldItemMainhand();
                final ItemStack itemstack2 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;
                if (!itemstack.isEmpty() && !itemstack2.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack2.getItem() == Items.SHIELD) {
                    final float f2 = 0.25f + EnchantmentHelper.getEfficiencyModifier(this) * 0.05f;
                    if (this.rand.nextFloat() < f2) {
                        entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }
            this.applyEnchantments(this, entityIn);
        }
        return flag;
    }
    
    @Override
    public float getBlockPathWeight(final BlockPos pos) {
        return 0.5f - this.world.getLightBrightness(pos);
    }
    
    protected boolean isValidLightLevel() {
        final BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (this.world.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32)) {
            return false;
        }
        int i = this.world.getLightFromNeighbors(blockpos);
        if (this.world.isThundering()) {
            final int j = this.world.getSkylightSubtracted();
            this.world.setSkylightSubtracted(10);
            i = this.world.getLightFromNeighbors(blockpos);
            this.world.setSkylightSubtracted(j);
        }
        return i <= this.rand.nextInt(8);
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
    }
    
    @Override
    protected boolean canDropLoot() {
        return true;
    }
    
    public boolean isPreventingPlayerRest(final EntityPlayer playerIn) {
        return true;
    }
}
