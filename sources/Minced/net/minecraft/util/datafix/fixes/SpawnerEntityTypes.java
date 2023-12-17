// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class SpawnerEntityTypes implements IFixableData
{
    @Override
    public int getFixVersion() {
        return 107;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        if (!"MobSpawner".equals(compound.getString("id"))) {
            return compound;
        }
        if (compound.hasKey("EntityId", 8)) {
            final String s = compound.getString("EntityId");
            final NBTTagCompound nbttagcompound = compound.getCompoundTag("SpawnData");
            nbttagcompound.setString("id", s.isEmpty() ? "Pig" : s);
            compound.setTag("SpawnData", nbttagcompound);
            compound.removeTag("EntityId");
        }
        if (compound.hasKey("SpawnPotentials", 9)) {
            final NBTTagList nbttaglist = compound.getTagList("SpawnPotentials", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(i);
                if (nbttagcompound2.hasKey("Type", 8)) {
                    final NBTTagCompound nbttagcompound3 = nbttagcompound2.getCompoundTag("Properties");
                    nbttagcompound3.setString("id", nbttagcompound2.getString("Type"));
                    nbttagcompound2.setTag("Entity", nbttagcompound3);
                    nbttagcompound2.removeTag("Type");
                    nbttagcompound2.removeTag("Properties");
                }
            }
        }
        return compound;
    }
}
