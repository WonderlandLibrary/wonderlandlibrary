/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityCaveSpider
extends EntitySpider {
    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (super.attackEntityAsMob(entity)) {
            if (entity instanceof EntityLivingBase) {
                int n = 0;
                if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) {
                    n = 7;
                } else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                    n = 15;
                }
                if (n > 0) {
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, n * 20, 0));
                }
            }
            return true;
        }
        return false;
    }

    public EntityCaveSpider(World world) {
        super(world);
        this.setSize(0.7f, 0.5f);
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultyInstance, IEntityLivingData iEntityLivingData) {
        return iEntityLivingData;
    }

    @Override
    public float getEyeHeight() {
        return 0.45f;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12.0);
    }
}

