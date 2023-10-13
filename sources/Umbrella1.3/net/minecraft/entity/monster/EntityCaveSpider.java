/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00001683";

    public EntityCaveSpider(World worldIn) {
        super(worldIn);
        this.setSize(0.7f, 0.5f);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12.0);
    }

    @Override
    public boolean attackEntityAsMob(Entity p_70652_1_) {
        if (super.attackEntityAsMob(p_70652_1_)) {
            if (p_70652_1_ instanceof EntityLivingBase) {
                int var2 = 0;
                if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) {
                    var2 = 7;
                } else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                    var2 = 15;
                }
                if (var2 > 0) {
                    ((EntityLivingBase)p_70652_1_).addPotionEffect(new PotionEffect(Potion.poison.id, var2 * 20, 0));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_) {
        return p_180482_2_;
    }

    @Override
    public float getEyeHeight() {
        return 0.45f;
    }
}

