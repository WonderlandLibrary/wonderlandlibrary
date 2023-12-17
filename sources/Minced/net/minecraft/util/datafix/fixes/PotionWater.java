// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class PotionWater implements IFixableData
{
    @Override
    public int getFixVersion() {
        return 806;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        final String s = compound.getString("id");
        if ("minecraft:potion".equals(s) || "minecraft:splash_potion".equals(s) || "minecraft:lingering_potion".equals(s) || "minecraft:tipped_arrow".equals(s)) {
            final NBTTagCompound nbttagcompound = compound.getCompoundTag("tag");
            if (!nbttagcompound.hasKey("Potion", 8)) {
                nbttagcompound.setString("Potion", "minecraft:water");
            }
            if (!compound.hasKey("tag", 10)) {
                compound.setTag("tag", nbttagcompound);
            }
        }
        return compound;
    }
}
