// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class WeightedSpawnerEntity extends WeightedRandom.Item
{
    private final NBTTagCompound nbt;
    
    public WeightedSpawnerEntity() {
        super(1);
        (this.nbt = new NBTTagCompound()).setString("id", "minecraft:pig");
    }
    
    public WeightedSpawnerEntity(final NBTTagCompound nbtIn) {
        this(nbtIn.hasKey("Weight", 99) ? nbtIn.getInteger("Weight") : 1, nbtIn.getCompoundTag("Entity"));
    }
    
    public WeightedSpawnerEntity(final int itemWeightIn, final NBTTagCompound nbtIn) {
        super(itemWeightIn);
        this.nbt = nbtIn;
    }
    
    public NBTTagCompound toCompoundTag() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        if (!this.nbt.hasKey("id", 8)) {
            this.nbt.setString("id", "minecraft:pig");
        }
        else if (!this.nbt.getString("id").contains(":")) {
            this.nbt.setString("id", new ResourceLocation(this.nbt.getString("id")).toString());
        }
        nbttagcompound.setTag("Entity", this.nbt);
        nbttagcompound.setInteger("Weight", this.itemWeight);
        return nbttagcompound;
    }
    
    public NBTTagCompound getNbt() {
        return this.nbt;
    }
}
