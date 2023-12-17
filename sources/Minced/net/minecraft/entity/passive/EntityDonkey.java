// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityDonkey extends AbstractChestHorse
{
    public EntityDonkey(final World worldIn) {
        super(worldIn);
    }
    
    public static void registerFixesDonkey(final DataFixer fixer) {
        AbstractChestHorse.registerFixesAbstractChestHorse(fixer, EntityDonkey.class);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_DONKEY;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.ENTITY_DONKEY_AMBIENT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.ENTITY_DONKEY_DEATH;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        super.getHurtSound(damageSourceIn);
        return SoundEvents.ENTITY_DONKEY_HURT;
    }
    
    @Override
    public boolean canMateWith(final EntityAnimal otherAnimal) {
        return otherAnimal != this && (otherAnimal instanceof EntityDonkey || otherAnimal instanceof EntityHorse) && this.canMate() && ((AbstractHorse)otherAnimal).canMate();
    }
    
    @Override
    public EntityAgeable createChild(final EntityAgeable ageable) {
        final AbstractHorse abstracthorse = (ageable instanceof EntityHorse) ? new EntityMule(this.world) : new EntityDonkey(this.world);
        this.setOffspringAttributes(ageable, abstracthorse);
        return abstracthorse;
    }
}
