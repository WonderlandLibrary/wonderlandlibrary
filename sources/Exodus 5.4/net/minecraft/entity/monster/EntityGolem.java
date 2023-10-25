/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.monster;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.world.World;

public abstract class EntityGolem
extends EntityCreature
implements IAnimals {
    @Override
    protected String getHurtSound() {
        return "none";
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected String getDeathSound() {
        return "none";
    }

    @Override
    public int getTalkInterval() {
        return 120;
    }

    @Override
    public void fall(float f, float f2) {
    }

    public EntityGolem(World world) {
        super(world);
    }

    @Override
    protected String getLivingSound() {
        return "none";
    }
}

