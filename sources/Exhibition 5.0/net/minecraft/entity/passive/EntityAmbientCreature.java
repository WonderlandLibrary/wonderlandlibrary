// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;

public abstract class EntityAmbientCreature extends EntityLiving implements IAnimals
{
    private static final String __OBFID = "CL_00001636";
    
    public EntityAmbientCreature(final World worldIn) {
        super(worldIn);
    }
    
    @Override
    public boolean allowLeashing() {
        return false;
    }
    
    @Override
    protected boolean interact(final EntityPlayer p_70085_1_) {
        return false;
    }
}
