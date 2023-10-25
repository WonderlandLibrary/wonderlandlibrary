/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityAIMate
extends EntityAIBase {
    World theWorld;
    double moveSpeed;
    int spawnBabyDelay;
    private EntityAnimal targetMate;
    private EntityAnimal theAnimal;

    public EntityAIMate(EntityAnimal entityAnimal, double d) {
        this.theAnimal = entityAnimal;
        this.theWorld = entityAnimal.worldObj;
        this.moveSpeed = d;
        this.setMutexBits(3);
    }

    @Override
    public boolean continueExecuting() {
        return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
    }

    @Override
    public void resetTask() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    @Override
    public void updateTask() {
        this.theAnimal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0f, this.theAnimal.getVerticalFaceSpeed());
        this.theAnimal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;
        if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity(this.targetMate) < 9.0) {
            this.spawnBaby();
        }
    }

    private EntityAnimal getNearbyMate() {
        float f = 8.0f;
        List<?> list = this.theWorld.getEntitiesWithinAABB(this.theAnimal.getClass(), this.theAnimal.getEntityBoundingBox().expand(f, f, f));
        double d = Double.MAX_VALUE;
        EntityAnimal entityAnimal = null;
        for (EntityAnimal entityAnimal2 : list) {
            if (!this.theAnimal.canMateWith(entityAnimal2) || !(this.theAnimal.getDistanceSqToEntity(entityAnimal2) < d)) continue;
            entityAnimal = entityAnimal2;
            d = this.theAnimal.getDistanceSqToEntity(entityAnimal2);
        }
        return entityAnimal;
    }

    @Override
    public boolean shouldExecute() {
        if (!this.theAnimal.isInLove()) {
            return false;
        }
        this.targetMate = this.getNearbyMate();
        return this.targetMate != null;
    }

    private void spawnBaby() {
        EntityAgeable entityAgeable = this.theAnimal.createChild(this.targetMate);
        if (entityAgeable != null) {
            EntityPlayer entityPlayer = this.theAnimal.getPlayerInLove();
            if (entityPlayer == null && this.targetMate.getPlayerInLove() != null) {
                entityPlayer = this.targetMate.getPlayerInLove();
            }
            if (entityPlayer != null) {
                entityPlayer.triggerAchievement(StatList.animalsBredStat);
                if (this.theAnimal instanceof EntityCow) {
                    entityPlayer.triggerAchievement(AchievementList.breedCow);
                }
            }
            this.theAnimal.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            this.theAnimal.resetInLove();
            this.targetMate.resetInLove();
            entityAgeable.setGrowingAge(-24000);
            entityAgeable.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0f, 0.0f);
            this.theWorld.spawnEntityInWorld(entityAgeable);
            Random random = this.theAnimal.getRNG();
            int n = 0;
            while (n < 7) {
                double d = random.nextGaussian() * 0.02;
                double d2 = random.nextGaussian() * 0.02;
                double d3 = random.nextGaussian() * 0.02;
                double d4 = random.nextDouble() * (double)this.theAnimal.width * 2.0 - (double)this.theAnimal.width;
                double d5 = 0.5 + random.nextDouble() * (double)this.theAnimal.height;
                double d6 = random.nextDouble() * (double)this.theAnimal.width * 2.0 - (double)this.theAnimal.width;
                this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.theAnimal.posX + d4, this.theAnimal.posY + d5, this.theAnimal.posZ + d6, d, d2, d3, new int[0]);
                ++n;
            }
            if (this.theWorld.getGameRules().getBoolean("doMobLoot")) {
                this.theWorld.spawnEntityInWorld(new EntityXPOrb(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, random.nextInt(7) + 1));
            }
        }
    }
}

