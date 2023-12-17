// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.util.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.EntityCreature;

public abstract class EntityGolem extends EntityCreature implements IAnimals
{
    public EntityGolem(final World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return null;
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }
    
    @Override
    public int getTalkInterval() {
        return 120;
    }
    
    @Override
    protected boolean canDespawn() {
        return false;
    }
}
