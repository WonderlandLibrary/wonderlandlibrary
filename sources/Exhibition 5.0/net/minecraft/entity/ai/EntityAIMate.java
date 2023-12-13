// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.stats.StatList;
import net.minecraft.entity.EntityAgeable;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.entity.passive.EntityAnimal;

public class EntityAIMate extends EntityAIBase
{
    private EntityAnimal theAnimal;
    World theWorld;
    private EntityAnimal targetMate;
    int spawnBabyDelay;
    double moveSpeed;
    private static final String __OBFID = "CL_00001578";
    
    public EntityAIMate(final EntityAnimal p_i1619_1_, final double p_i1619_2_) {
        this.theAnimal = p_i1619_1_;
        this.theWorld = p_i1619_1_.worldObj;
        this.moveSpeed = p_i1619_2_;
        this.setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.theAnimal.isInLove()) {
            return false;
        }
        this.targetMate = this.getNearbyMate();
        return this.targetMate != null;
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
        final float var1 = 8.0f;
        final List var2 = this.theWorld.getEntitiesWithinAABB(this.theAnimal.getClass(), this.theAnimal.getEntityBoundingBox().expand(var1, var1, var1));
        double var3 = Double.MAX_VALUE;
        EntityAnimal var4 = null;
        for (final EntityAnimal var6 : var2) {
            if (this.theAnimal.canMateWith(var6) && this.theAnimal.getDistanceSqToEntity(var6) < var3) {
                var4 = var6;
                var3 = this.theAnimal.getDistanceSqToEntity(var6);
            }
        }
        return var4;
    }
    
    private void spawnBaby() {
        final EntityAgeable var1 = this.theAnimal.createChild(this.targetMate);
        if (var1 != null) {
            EntityPlayer var2 = this.theAnimal.func_146083_cb();
            if (var2 == null && this.targetMate.func_146083_cb() != null) {
                var2 = this.targetMate.func_146083_cb();
            }
            if (var2 != null) {
                var2.triggerAchievement(StatList.animalsBredStat);
                if (this.theAnimal instanceof EntityCow) {
                    var2.triggerAchievement(AchievementList.breedCow);
                }
            }
            this.theAnimal.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            this.theAnimal.resetInLove();
            this.targetMate.resetInLove();
            var1.setGrowingAge(-24000);
            var1.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0f, 0.0f);
            this.theWorld.spawnEntityInWorld(var1);
            final Random var3 = this.theAnimal.getRNG();
            for (int var4 = 0; var4 < 7; ++var4) {
                final double var5 = var3.nextGaussian() * 0.02;
                final double var6 = var3.nextGaussian() * 0.02;
                final double var7 = var3.nextGaussian() * 0.02;
                this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.theAnimal.posX + var3.nextFloat() * this.theAnimal.width * 2.0f - this.theAnimal.width, this.theAnimal.posY + 0.5 + var3.nextFloat() * this.theAnimal.height, this.theAnimal.posZ + var3.nextFloat() * this.theAnimal.width * 2.0f - this.theAnimal.width, var5, var6, var7, new int[0]);
            }
            if (this.theWorld.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
                this.theWorld.spawnEntityInWorld(new EntityXPOrb(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, var3.nextInt(7) + 1));
            }
        }
    }
}
