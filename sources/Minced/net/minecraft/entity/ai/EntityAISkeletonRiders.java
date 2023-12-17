// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.passive.EntitySkeletonHorse;

public class EntityAISkeletonRiders extends EntityAIBase
{
    private final EntitySkeletonHorse horse;
    
    public EntityAISkeletonRiders(final EntitySkeletonHorse horseIn) {
        this.horse = horseIn;
    }
    
    @Override
    public boolean shouldExecute() {
        return this.horse.world.isAnyPlayerWithinRangeAt(this.horse.posX, this.horse.posY, this.horse.posZ, 10.0);
    }
    
    @Override
    public void updateTask() {
        final DifficultyInstance difficultyinstance = this.horse.world.getDifficultyForLocation(new BlockPos(this.horse));
        this.horse.setTrap(false);
        this.horse.setHorseTamed(true);
        this.horse.setGrowingAge(0);
        this.horse.world.addWeatherEffect(new EntityLightningBolt(this.horse.world, this.horse.posX, this.horse.posY, this.horse.posZ, true));
        final EntitySkeleton entityskeleton = this.createSkeleton(difficultyinstance, this.horse);
        entityskeleton.startRiding(this.horse);
        for (int i = 0; i < 3; ++i) {
            final AbstractHorse abstracthorse = this.createHorse(difficultyinstance);
            final EntitySkeleton entityskeleton2 = this.createSkeleton(difficultyinstance, abstracthorse);
            entityskeleton2.startRiding(abstracthorse);
            abstracthorse.addVelocity(this.horse.getRNG().nextGaussian() * 0.5, 0.0, this.horse.getRNG().nextGaussian() * 0.5);
        }
    }
    
    private AbstractHorse createHorse(final DifficultyInstance p_188515_1_) {
        final EntitySkeletonHorse entityskeletonhorse = new EntitySkeletonHorse(this.horse.world);
        entityskeletonhorse.onInitialSpawn(p_188515_1_, null);
        entityskeletonhorse.setPosition(this.horse.posX, this.horse.posY, this.horse.posZ);
        entityskeletonhorse.hurtResistantTime = 60;
        entityskeletonhorse.enablePersistence();
        entityskeletonhorse.setHorseTamed(true);
        entityskeletonhorse.setGrowingAge(0);
        entityskeletonhorse.world.spawnEntity(entityskeletonhorse);
        return entityskeletonhorse;
    }
    
    private EntitySkeleton createSkeleton(final DifficultyInstance p_188514_1_, final AbstractHorse p_188514_2_) {
        final EntitySkeleton entityskeleton = new EntitySkeleton(p_188514_2_.world);
        entityskeleton.onInitialSpawn(p_188514_1_, null);
        entityskeleton.setPosition(p_188514_2_.posX, p_188514_2_.posY, p_188514_2_.posZ);
        entityskeleton.hurtResistantTime = 60;
        entityskeleton.enablePersistence();
        if (entityskeleton.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            entityskeleton.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        }
        entityskeleton.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, EnchantmentHelper.addRandomEnchantment(entityskeleton.getRNG(), entityskeleton.getHeldItemMainhand(), (int)(5.0f + p_188514_1_.getClampedAdditionalDifficulty() * entityskeleton.getRNG().nextInt(18)), false));
        entityskeleton.setItemStackToSlot(EntityEquipmentSlot.HEAD, EnchantmentHelper.addRandomEnchantment(entityskeleton.getRNG(), entityskeleton.getItemStackFromSlot(EntityEquipmentSlot.HEAD), (int)(5.0f + p_188514_1_.getClampedAdditionalDifficulty() * entityskeleton.getRNG().nextInt(18)), false));
        entityskeleton.world.spawnEntity(entityskeleton);
        return entityskeleton;
    }
}
