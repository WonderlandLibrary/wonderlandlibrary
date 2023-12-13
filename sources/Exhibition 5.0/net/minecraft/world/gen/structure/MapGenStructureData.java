// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class MapGenStructureData extends WorldSavedData
{
    private NBTTagCompound field_143044_a;
    private static final String __OBFID = "CL_00000510";
    
    public MapGenStructureData(final String p_i43001_1_) {
        super(p_i43001_1_);
        this.field_143044_a = new NBTTagCompound();
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        this.field_143044_a = nbt.getCompoundTag("Features");
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        nbt.setTag("Features", this.field_143044_a);
    }
    
    public void func_143043_a(final NBTTagCompound p_143043_1_, final int p_143043_2_, final int p_143043_3_) {
        this.field_143044_a.setTag(func_143042_b(p_143043_2_, p_143043_3_), p_143043_1_);
    }
    
    public static String func_143042_b(final int p_143042_0_, final int p_143042_1_) {
        return "[" + p_143042_0_ + "," + p_143042_1_ + "]";
    }
    
    public NBTTagCompound func_143041_a() {
        return this.field_143044_a;
    }
}
