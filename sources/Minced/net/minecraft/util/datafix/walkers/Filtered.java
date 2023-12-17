// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.walkers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IDataWalker;

public abstract class Filtered implements IDataWalker
{
    private final ResourceLocation key;
    
    public Filtered(final Class<?> p_i47309_1_) {
        if (Entity.class.isAssignableFrom(p_i47309_1_)) {
            this.key = EntityList.getKey((Class<? extends Entity>)p_i47309_1_);
        }
        else if (TileEntity.class.isAssignableFrom(p_i47309_1_)) {
            this.key = TileEntity.getKey((Class<? extends TileEntity>)p_i47309_1_);
        }
        else {
            this.key = null;
        }
    }
    
    @Override
    public NBTTagCompound process(final IDataFixer fixer, NBTTagCompound compound, final int versionIn) {
        if (new ResourceLocation(compound.getString("id")).equals(this.key)) {
            compound = this.filteredProcess(fixer, compound, versionIn);
        }
        return compound;
    }
    
    abstract NBTTagCompound filteredProcess(final IDataFixer p0, final NBTTagCompound p1, final int p2);
}
