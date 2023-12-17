// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityWitherSkeleton extends AbstractSkeleton
{
    public EntityWitherSkeleton(final World worldIn) {
        super(worldIn);
        this.setSize(0.7f, 2.4f);
        this.isImmuneToFire = true;
    }
    
    public static void registerFixesWitherSkeleton(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityWitherSkeleton.class);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_WITHER_SKELETON;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_WITHER_SKELETON_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITHER_SKELETON_DEATH;
    }
    
    @Override
    SoundEvent getStepSound() {
        return SoundEvents.ENTITY_WITHER_SKELETON_STEP;
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        super.onDeath(cause);
        if (cause.getTrueSource() instanceof EntityCreeper) {
            final EntityCreeper entitycreeper = (EntityCreeper)cause.getTrueSource();
            if (entitycreeper.getPowered() && entitycreeper.ableToCauseSkullDrop()) {
                entitycreeper.incrementDroppedSkulls();
                this.entityDropItem(new ItemStack(Items.SKULL, 1, 1), 0.0f);
            }
        }
    }
    
    @Override
    protected void setEquipmentBasedOnDifficulty(final DifficultyInstance difficulty) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
    }
    
    @Override
    protected void setEnchantmentBasedOnDifficulty(final DifficultyInstance difficulty) {
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable final IEntityLivingData livingdata) {
        final IEntityLivingData ientitylivingdata = super.onInitialSpawn(difficulty, livingdata);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0);
        this.setCombatTask();
        return ientitylivingdata;
    }
    
    @Override
    public float getEyeHeight() {
        return 2.1f;
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        if (!super.attackEntityAsMob(entityIn)) {
            return false;
        }
        if (entityIn instanceof EntityLivingBase) {
            ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.WITHER, 200));
        }
        return true;
    }
    
    @Override
    protected EntityArrow getArrow(final float p_190726_1_) {
        final EntityArrow entityarrow = super.getArrow(p_190726_1_);
        entityarrow.setFire(100);
        return entityarrow;
    }
}
