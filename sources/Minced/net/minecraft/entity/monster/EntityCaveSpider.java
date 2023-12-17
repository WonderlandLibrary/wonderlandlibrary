// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nullable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityCaveSpider extends EntitySpider
{
    public EntityCaveSpider(final World worldIn) {
        super(worldIn);
        this.setSize(0.7f, 0.5f);
    }
    
    public static void registerFixesCaveSpider(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityCaveSpider.class);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0);
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        if (super.attackEntityAsMob(entityIn)) {
            if (entityIn instanceof EntityLivingBase) {
                int i = 0;
                if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                    i = 7;
                }
                else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                    i = 15;
                }
                if (i > 0) {
                    ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.POISON, i * 20, 0));
                }
            }
            return true;
        }
        return false;
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable final IEntityLivingData livingdata) {
        return livingdata;
    }
    
    @Override
    public float getEyeHeight() {
        return 0.45f;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_CAVE_SPIDER;
    }
}
