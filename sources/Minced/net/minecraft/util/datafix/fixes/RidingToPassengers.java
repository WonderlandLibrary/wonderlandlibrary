// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class RidingToPassengers implements IFixableData
{
    @Override
    public int getFixVersion() {
        return 135;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        while (compound.hasKey("Riding", 10)) {
            final NBTTagCompound nbttagcompound = this.extractVehicle(compound);
            this.addPassengerToVehicle(compound, nbttagcompound);
            compound = nbttagcompound;
        }
        return compound;
    }
    
    protected void addPassengerToVehicle(final NBTTagCompound p_188219_1_, final NBTTagCompound p_188219_2_) {
        final NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.appendTag(p_188219_1_);
        p_188219_2_.setTag("Passengers", nbttaglist);
    }
    
    protected NBTTagCompound extractVehicle(final NBTTagCompound p_188220_1_) {
        final NBTTagCompound nbttagcompound = p_188220_1_.getCompoundTag("Riding");
        p_188220_1_.removeTag("Riding");
        return nbttagcompound;
    }
}
