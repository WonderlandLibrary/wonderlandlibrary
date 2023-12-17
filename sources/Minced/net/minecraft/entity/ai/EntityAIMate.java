// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.stats.StatList;
import net.minecraft.entity.EntityAgeable;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.entity.passive.EntityAnimal;

public class EntityAIMate extends EntityAIBase
{
    private final EntityAnimal animal;
    private final Class<? extends EntityAnimal> mateClass;
    World world;
    private EntityAnimal targetMate;
    int spawnBabyDelay;
    double moveSpeed;
    
    public EntityAIMate(final EntityAnimal animal, final double speedIn) {
        this(animal, speedIn, animal.getClass());
    }
    
    public EntityAIMate(final EntityAnimal p_i47306_1_, final double p_i47306_2_, final Class<? extends EntityAnimal> p_i47306_4_) {
        this.animal = p_i47306_1_;
        this.world = p_i47306_1_.world;
        this.mateClass = p_i47306_4_;
        this.moveSpeed = p_i47306_2_;
        this.setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.animal.isInLove()) {
            return false;
        }
        this.targetMate = this.getNearbyMate();
        return this.targetMate != null;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
    }
    
    @Override
    public void resetTask() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }
    
    @Override
    public void updateTask() {
        this.animal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0f, (float)this.animal.getVerticalFaceSpeed());
        this.animal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;
        if (this.spawnBabyDelay >= 60 && this.animal.getDistanceSq(this.targetMate) < 9.0) {
            this.spawnBaby();
        }
    }
    
    private EntityAnimal getNearbyMate() {
        final List<EntityAnimal> list = this.world.getEntitiesWithinAABB(this.mateClass, this.animal.getEntityBoundingBox().grow(8.0));
        double d0 = Double.MAX_VALUE;
        EntityAnimal entityanimal = null;
        for (final EntityAnimal entityanimal2 : list) {
            if (this.animal.canMateWith(entityanimal2) && this.animal.getDistanceSq(entityanimal2) < d0) {
                entityanimal = entityanimal2;
                d0 = this.animal.getDistanceSq(entityanimal2);
            }
        }
        return entityanimal;
    }
    
    private void spawnBaby() {
        final EntityAgeable entityageable = this.animal.createChild(this.targetMate);
        if (entityageable != null) {
            EntityPlayerMP entityplayermp = this.animal.getLoveCause();
            if (entityplayermp == null && this.targetMate.getLoveCause() != null) {
                entityplayermp = this.targetMate.getLoveCause();
            }
            if (entityplayermp != null) {
                entityplayermp.addStat(StatList.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(entityplayermp, this.animal, this.targetMate, entityageable);
            }
            this.animal.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            this.animal.resetInLove();
            this.targetMate.resetInLove();
            entityageable.setGrowingAge(-24000);
            entityageable.setLocationAndAngles(this.animal.posX, this.animal.posY, this.animal.posZ, 0.0f, 0.0f);
            this.world.spawnEntity(entityageable);
            final Random random = this.animal.getRNG();
            for (int i = 0; i < 7; ++i) {
                final double d0 = random.nextGaussian() * 0.02;
                final double d2 = random.nextGaussian() * 0.02;
                final double d3 = random.nextGaussian() * 0.02;
                final double d4 = random.nextDouble() * this.animal.width * 2.0 - this.animal.width;
                final double d5 = 0.5 + random.nextDouble() * this.animal.height;
                final double d6 = random.nextDouble() * this.animal.width * 2.0 - this.animal.width;
                this.world.spawnParticle(EnumParticleTypes.HEART, this.animal.posX + d4, this.animal.posY + d5, this.animal.posZ + d6, d0, d2, d3, new int[0]);
            }
            if (this.world.getGameRules().getBoolean("doMobLoot")) {
                this.world.spawnEntity(new EntityXPOrb(this.world, this.animal.posX, this.animal.posY, this.animal.posZ, random.nextInt(7) + 1));
            }
        }
    }
}
