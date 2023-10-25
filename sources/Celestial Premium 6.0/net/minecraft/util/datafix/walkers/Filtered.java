/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.datafix.walkers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;

public abstract class Filtered
implements IDataWalker {
    private final ResourceLocation key;

    public Filtered(Class<?> p_i47309_1_) {
        this.key = Entity.class.isAssignableFrom(p_i47309_1_) ? EntityList.getKey(p_i47309_1_) : (TileEntity.class.isAssignableFrom(p_i47309_1_) ? TileEntity.func_190559_a(p_i47309_1_) : null);
    }

    @Override
    public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
        if (new ResourceLocation(compound.getString("id")).equals(this.key)) {
            compound = this.filteredProcess(fixer, compound, versionIn);
        }
        return compound;
    }

    abstract NBTTagCompound filteredProcess(IDataFixer var1, NBTTagCompound var2, int var3);
}

