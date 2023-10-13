/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.passive;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class EntityAmbientCreature
extends EntityLiving
implements IAnimals {
    private static final String __OBFID = "CL_00001636";

    public EntityAmbientCreature(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean allowLeashing() {
        return false;
    }

    @Override
    protected boolean interact(EntityPlayer p_70085_1_) {
        return false;
    }
}

